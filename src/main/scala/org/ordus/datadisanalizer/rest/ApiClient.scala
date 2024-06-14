package org.ordus.datadisanalizer.rest

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{OAuth2BearerToken, RawHeader}
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.http.scaladsl.{ClientTransport, ConnectionContext, Http}
import org.ordus.datadisanalizer.constants.{DatadisAnalyzerConstants, ErrorCodeConstants}
import org.ordus.datadisanalizer.domain.ProxyParams
import org.ordus.datadisanalizer.error.{DatadisAnalyzerError, HttpError}
import org.slf4j.{Logger, LoggerFactory}

import java.net.InetSocketAddress
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.{KeyManager, SSLContext, X509TrustManager}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success, Try}

trait ApiClient {
  private val logger: Logger = LoggerFactory.getLogger(classOf[ApiClient])

  implicit lazy val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "ApiClient")
  implicit lazy val executionContext: ExecutionContextExecutor = system.executionContext

  def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])
                       (implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
    val uri = Uri(url)
    logger.info(s"Executing request for: ${uri.path}")
    Try {
      val trustSslContext: SSLContext = {
        object NoCheckX509TrustManager extends X509TrustManager {
          override def checkClientTrusted(chain: Array[X509Certificate], authType: String): Unit = ()

          override def checkServerTrusted(chain: Array[X509Certificate], authType: String): Unit = ()

          override def getAcceptedIssuers: Array[X509Certificate] = Array[X509Certificate]()
        }

        val context = SSLContext.getInstance("TLS")
        context.init(Array[KeyManager](), Array(NoCheckX509TrustManager), new SecureRandom())
        context
      }
      val context = ConnectionContext.httpsClient(trustSslContext)
      Http().setDefaultClientHttpsContext(context)


      val settings = if (proxyParams.isDefined) {
        val httpsProxyTransport = ClientTransport.httpsProxy(InetSocketAddress.createUnresolved(proxyParams.get.host, proxyParams.get.port))
        ConnectionPoolSettings(system).withConnectionSettings(ClientConnectionSettings(system).withTransport(httpsProxyTransport))
      } else {
        ConnectionPoolSettings(system).withConnectionSettings(ClientConnectionSettings(system))
      }

      val req = Http().singleRequest(
        HttpRequest(method = method, uri = uri, entity = body)
          .addCredentials(OAuth2BearerToken(token)).addHeader(RawHeader("Accept", "application/json")), settings = settings
      )
      Await.result(req, Duration(DatadisAnalyzerConstants.HttpRequestTimeoutSeconds, TimeUnit.SECONDS))
    } match {
      case Success(response) =>
        if (response.status.isSuccess()) {
          Try {
            Await.result(Unmarshal(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, response.entity.dataBytes))).to[T],
              Duration(DatadisAnalyzerConstants.HttpRequestTimeoutSeconds, TimeUnit.SECONDS))
          } match {
            case Success(resultValue) =>
              logger.debug(s"Result:||$resultValue")
              Right(Some(resultValue))
            case Failure(exception) => Left(HttpError(s"Error unmarshalling response", ErrorCodeConstants.HttpResponseUnmarshalError, Some(exception)))
          }
        } else {
          val resultValue = Await.result(Unmarshal(response).to[String], Duration(DatadisAnalyzerConstants.HttpRequestTimeoutSeconds, TimeUnit.SECONDS))
          logger.debug(s"Result:||$resultValue")
          if (response.status == StatusCodes.NotFound) {
            Right(None)
          } else {
            Left(HttpError(s"Error ${response.status.value} - $resultValue", ErrorCodeConstants.HttpResponseError, None))
          }
        }
      case Failure(exception) => Left(HttpError(s"Error executing request with url =  $url", ErrorCodeConstants.HttpError,
        Some(exception)))
    }
  }
}
