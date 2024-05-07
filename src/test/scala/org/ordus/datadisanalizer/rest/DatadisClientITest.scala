package org.ordus.datadisanalizer.rest

import org.ordus.datadisanalizer.DatadisAnalyzerUnitSpec
import org.ordus.datadisanalizer.domain.DatadisParams
import org.ordus.datadisanalizer.domain.datadis._

// scalastyle:off
class DatadisClientITest extends DatadisAnalyzerUnitSpec {
  "a DatadisClient" when {
    "login" should {
      "return a token when ok" in {
        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.login(datadisParams)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe DatadisServerResponses.Token
      }
    }

    "getSupplies" should {
      "return a list of supplies when ok" in {
        val expected = List(Supply("this street", "648376872634W", "28001", "MADRID", "MADRID-MADRID", "UFD", "2024/01/01", "", 4, "5"))
        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getSupplies(datadisParams, DatadisServerResponses.Token)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getContractDetail" should {
      "return a list of contracts when ok" in {
        val expected = List(Contract("648376872634W", "UFD", "My marketer", "Baja 230", "2.0TD PEAJE ATR", "MADRID", "MADRID-MADRID", "28001", List(3.4, 3.4), "", "", "2024/01/01", "", "2T", None, None, None, None, None, None, None))
        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getContractDetail(datadisParams, DatadisServerResponses.Token, "648376872634W", "5")

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getConsumptionData" should {
      "return a list of consumptions when ok" in {
        val expected = List(Consumption("648376872634W", "2024/01/01", "24:00", 0.021, "Real", 0.0),
          Consumption("648376872634W", "2024/01/02", "01:00", 0.025, "Real", 0.0))

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getConsumptionData(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2020/01/01", "2020/01/02", "0", "4")

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getMaxPower" should {
      "return a list of consumptions when ok" in {
        val expected = List(MaxPower("648376872634W", "2023/12/07", "12:45", 2.62, "1"),
          MaxPower("648376872634W", "2023/12/13", "08:45", 2.646, "2"),
          MaxPower("648376872634W", "2023/12/09", "12:15", 3.823, "3"))

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getMaxPower(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2023/10/01", "2024/01/02")

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getSuppliesV2" should {
      "return a list of supplies when ok" in {
        val expected = SupplyV2Response(List(SupplyV2("this street", "648376872634W", "28001", "MADRID", "MADRID-MADRID", "UFD", "2024/01/01", None, None, 4, "5", None, None, Some("https://www.ufd.es/"), false)), "902", "", "UFD")
        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getSuppliesV2(datadisParams, DatadisServerResponses.Token)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getContractDetailV2" should {
      "return a list of contracts when ok" in {
        val expected = ContractV2Response(List(ContractV2("648376872634W", "UFD", "My marketer", "Baja 230", "2.0TD PEAJE ATR", "2T", "", "MADRID", "28", "MADRID-MADRID", "28001", List(3.4, 3.4), 0, "", "2024/01/01", "", 0, "https://www.ufd.es/", None, None, None, None, None, None, None)))
        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getContractDetailV2(datadisParams, DatadisServerResponses.Token, "648376872634W", "5")

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getTimeCurveDataHours" should {
      "return a list of consumptions when ok" in {
        val expected = ConsumptionV2HourlyResponse(List(ConsumptionV2Hourly("648376872634W", "2024/01/01", "24:00", 0.021, 1, 0, "Real", 0.0, "LLANO"),
          ConsumptionV2Hourly("648376872634W", "2024/01/02", "01:00", 0.025, 1, 0, "Real", 0.0, "VALLE")), None, None)

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getTimeCurveDataHours(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2020/01/01", "2020/01/02", "0", "4", false, "28", "2T", None)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getTimeCurveDataWeek" should {
      "return a list of consumptions when ok" in {
        val expected = ConsumptionV2WeeklyResponse(List(ConsumptionV2Weekly("648376872634W", "2024/01/01", 7.6850000000000005, 0.0, "Estimada", Some(0.547), Some(2.581), Some(4.557), None, None, None, None, None, None, None),
          ConsumptionV2Weekly("648376872634W", "2024/01/02", 2.337, 0.0, "Estimada", Some(2.337), None, None, None, None, None, None, None, None, None)),
          None, None, None, None, None, None, None, None, None, None, None, None, 1, None, None, 0)

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getTimeCurveDataWeek(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2020/01/01", "2020/01/02", "0", "4", false, "28", "2T", None)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getTimeCurveDataMonth" should {
      "return a list of consumptions when ok" in {
        val expected = ConsumptionV2MonthlyResponse(List(ConsumptionV2Monthly("648376872634W", "2023/12", 0.105, 0.0, "Estimada", Some(0.105), None, None, None, None, None, None, None, None, None),
          ConsumptionV2Monthly("648376872634W", "2024/01", 58.479, 0.0, "Estimada", Some(12.815), Some(17.018), Some(28.646), None, None, None, None, None, None, None)),
          None, None, None, None, None, None, None, None, None, None, None, None, 1, None, None, 0)

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getTimeCurveDataMonth(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2020/01/01", "2020/01/02", "0", "4", false, "28", "2T", None)

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }

    "getMaxPowerV2" should {
      "return a list of consumptions when ok" in {
        val expected = MaxPowerV2Response(List(MaxPowerV2("648376872634W", "2023/12/07", "12:45", 2.62, "1", 0),
          MaxPowerV2("648376872634W", "2023/12/13", "08:45", 2.646, "2", 0),
          MaxPowerV2("648376872634W", "2023/12/09", "12:15", 3.823, "3", 0)))

        val server = new DatadisServer()

        val client = new DatadisClient {}
        val datadisParams = DatadisParams(s"http://localhost:${server.httpServer.localAddress.getPort}", "657847A", "password", None)

        val result = client.getMaxPowerV2(datadisParams, DatadisServerResponses.Token, "648376872634W", "5", "2023/10/01", "2024/01/02")

        server.tearDownHttpServer()

        result.isRight shouldBe true
        result.toOption.get.get shouldBe expected
      }
    }
  }
}
