package org.ordus.datadisanalizer.rest

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.ordus.datadisanalizer.rest.DatadisServerResponses._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

// scalastyle:off
class DatadisServer {
  val httpServer: Http.ServerBinding = startUpDatadisServer()

  private def startUpDatadisServer(): Http.ServerBinding = {
    implicit lazy val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "DatadisClientTestServer")
    implicit lazy val executionContext: ExecutionContextExecutor = system.executionContext

    val route: Route = {
      concat(
        // POST login
        ///?username=${datadisParams.user}&password=${datadisParams.password}
        post {
          path("nikola-auth" / "tokens" / "login") {
            complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, Token))
          }
        },
        // GET Supplies
        get {
          path("api-private" / "api" / "get-supplies") {
            complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, SupplyR))
          }
        },
        // GET Contracts
        get {
          path("api-private" / "api" / "get-contract-detail") {
            complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ContractR))
          }
        },
        // GET Consumption
        get {
          path("api-private" / "api" / "get-consumption-data") {
            complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ConsumptionR))
          }
        },
        // GET MaxPower
        get {
          path("api-private" / "api" / "get-max-power") {
            complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, MaxPowerR))
          }
        },
        // GET Supplies V2
        get {
          path("api-private" / "getSupplies") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, SupplyV2ResponseR))
            }
          }
        },
        // POST Contracts V2
        post {
          path("api-private" / "supply-data" / "contractual-data") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ContractV2ResponseR))
            }
          }
        },
        // POST Consumption V2 Hourly
        post {
          path("api-private" / "supply-data" / "v2" / "time-curve-data" / "hours") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ConsumptionV2HourlyResponseR))
            }
          }
        },
        // POST Consumption V2 Weekly
        post {
          path("api-private" / "supply-data" / "v2" / "time-curve-data" / "week") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ConsumptionV2WeeklyResponseR))
            }
          }
        },
        // POST Consumption V2 Monthly
        post {
          path("api-private" / "supply-data" / "v2" / "time-curve-data" / "month") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ConsumptionV2MonthlyResponseR))
            }
          }
        },
        // POST Max Power V2
        post {
          path("api-private" / "supply-data" / "max-power") {
            entity(as[String]) { _ =>
              complete(status = StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, MaxPowerV2ResponseR))
            }
          }
        },

        pathPrefix("dbc") {
          complete(status = StatusCodes.Forbidden, HttpEntity(ContentTypes.`application/json`, "{bad credentials}"))
        },
        pathPrefix("dse") {
          complete(status = StatusCodes.BadRequest, HttpEntity(ContentTypes.`application/json`, "{bad request}"))
        }
      )
    }

    val f = Http().newServerAt("localhost", 0).bind(route)
    f.onComplete({
      case Success(newServer) => newServer
      case Failure(exception) => throw exception
    })
    Await.result(f, Duration.Inf)
  }

  def tearDownHttpServer(): Unit = {
    httpServer.terminate(Duration(10, TimeUnit.SECONDS))
  }
}
