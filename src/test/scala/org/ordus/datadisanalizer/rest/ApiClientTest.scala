package org.ordus.datadisanalizer.rest

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import org.ordus.datadisanalizer.DatadisAnalyzerUnitSpec
import org.ordus.datadisanalizer.constants.ErrorCodeConstants
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

class ApiClientTest extends DatadisAnalyzerUnitSpec {
  "an ApiClient" when {
    "execute executeRequest" should {
      "return the body of the response when all ok" in {
        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest[String](s"http://localhost:${testHttpServer.localAddress.getPort}/goodPath", HttpMethods.POST, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Right[_, _]]
        result.toOption.get shouldBe Some("{OK}")
      }

      "return a case Class of the response when all ok and a case Class" in {
        val expected = TestClass("OK", 5)
        implicit val fooFormat: RootJsonFormat[TestClass] = jsonFormat(TestClass, "type", "numVal")

        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest[TestClass](s"http://localhost:${testHttpServer.localAddress.getPort}/goodEntity", HttpMethods.GET, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Right[_, _]]
        result.toOption.get shouldBe Some(expected)
      }

      "return a case Class of the response when all ok and a case Class and some field is missing" in {
        val expected = TestClassExtra("OK", 5, None)
        implicit val fooFormat: RootJsonFormat[TestClassExtra] = jsonFormat(TestClassExtra, "type", "numVal", "other")

        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest[TestClassExtra](s"http://localhost:${testHttpServer.localAddress.getPort}/goodEntity", HttpMethods.GET, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Right[_, _]]
        result.toOption.get shouldBe Some(expected)
      }

      "return error when case Class and some required field is missing" in {
        implicit val fooFormat: RootJsonFormat[TestClassExtra] = jsonFormat(TestClassExtra, "type", "numVal", "other")

        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest[TestClassExtra](s"http://localhost:${testHttpServer.localAddress.getPort}/goodPath", HttpMethods.GET, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseUnmarshalError
      }

      "return None when 404 response" in {
        implicit val fooFormat: RootJsonFormat[TestClass] = jsonFormat(TestClass, "type", "numVal")

        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest[TestClass](s"http://localhost:${testHttpServer.localAddress.getPort}/notFound", HttpMethods.GET, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Right[_, _]]
        result.toOption.get shouldBe None
      }

      "return error when bad credentials" in {
        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest(s"http://localhost:${testHttpServer.localAddress.getPort}/badCreds", HttpMethods.GET, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }

      "return error when error response" in {
        val testHttpServer = setUpTestServer()

        val client = new ApiClient() {}

        val result = client.executeRequest(s"http://localhost:${testHttpServer.localAddress.getPort}/notAccepted", HttpMethods.POST, HttpEntity("content"), "", None)

        tearDownTestServer(testHttpServer)

        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }

      "return error when no connection to host" in {
        val client = new ApiClient() {}

        val result = client.executeRequest(s"http://badhost_bad/bad", HttpMethods.POST, HttpEntity("content"), "", None)

        result shouldBe a[Left[_, _]]
        val logger = LoggerFactory.getLogger(classOf[ApiClient])
        result.left.get.code shouldBe ErrorCodeConstants.HttpError
      }
    }
  }

  def setUpTestServer(): Http.ServerBinding = {
    implicit lazy val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "MockTestServer")
    implicit lazy val executionContext: ExecutionContextExecutor = system.executionContext

    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(_, Uri.Path("/goodPath"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "{OK}"), status = StatusCodes.Accepted)
      case HttpRequest(_, Uri.Path("/goodEntity"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "{\"type\":\"OK\",\"numVal\":5}"), status = StatusCodes.OK)
      case HttpRequest(_, Uri.Path("/badCreds"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "{bad credentials}"), status = StatusCodes.Forbidden)
      case HttpRequest(_, Uri.Path("/notAccepted"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "{bad request}"), status = StatusCodes.BadRequest)
      case HttpRequest(_, Uri.Path("/notFound"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "{not found}"), status = StatusCodes.NotFound)
    }
    val f = Http().newServerAt("localhost", 0).bindSync(requestHandler)
    f.onComplete({
      case Success(newServer) => newServer
      case Failure(exception) => throw exception
    })
    Await.result(f, Duration.Inf)
  }

  def tearDownTestServer(server: Http.ServerBinding): Unit = {
    server.terminate(Duration(10, TimeUnit.SECONDS))
  }
}

case class TestClass(value: String, numVal: Int)

case class TestClassExtra(value: String, numVal: Int, other: Option[String])
