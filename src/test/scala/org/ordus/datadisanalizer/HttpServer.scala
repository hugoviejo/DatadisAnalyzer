package org.ordus.datadisanalizer

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

trait HttpServer {
  /**
   * Create an HTTP server and stars it
   *
   * @param port The listen port
   * @return the HTTP server
   */
  def getHttpServer(port: Int, verbose: Boolean = false): WireMockServer = {
    val wiremockServer = new WireMockServer(wireMockConfig().port(port)
      .notifier(new Slf4jNotifier(verbose))
    )
    wiremockServer.start()
    wiremockServer
  }

  /**
   * Stop the HTTP server
   *
   * @param server the server
   */
  def stopHttpServer(server: WireMockServer): Unit = {
    server.stop()
  }

  /**
   * Creates an endpoint that returns the file passed by parameter in the http body response.
   *
   * @param endPoint Http endpoint to access file
   * @param body     text returned on the body request.
   */
  def loadTextWireMock(server: WireMockServer, endPoint: String, body: String, method: String = "GET"): Unit = {
    val stub = method match {
      case "GET" => WireMock.get(urlEqualTo(endPoint))
      case "POST" => WireMock.post(urlEqualTo(endPoint))
    }
    server.stubFor(
      stub.willReturn(ok().withBody(body)))
  }
}
