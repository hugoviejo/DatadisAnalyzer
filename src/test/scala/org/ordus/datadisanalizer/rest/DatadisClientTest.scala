package org.ordus.datadisanalizer.rest

import akka.http.scaladsl.model.{HttpMethod, HttpResponse, RequestEntity}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import org.ordus.datadisanalizer.DatadisAnalyzerUnitSpec
import org.ordus.datadisanalizer.constants.ErrorCodeConstants
import org.ordus.datadisanalizer.domain.datadis.{Consumption, ConsumptionV2Hourly, ConsumptionV2HourlyResponse, ConsumptionV2Monthly, ConsumptionV2MonthlyResponse, ConsumptionV2Weekly, ConsumptionV2WeeklyResponse, Contract, MaxPower, MaxPowerV2, MaxPowerV2Response, Supply, SupplyV2}
import org.ordus.datadisanalizer.domain.{DatadisParams, ProxyParams}
import org.ordus.datadisanalizer.error.{DatadisAnalyzerError, HttpError}

// scalastyle:off
class DatadisClientTest extends DatadisAnalyzerUnitSpec {
  "a DatadisClient" when {
    "login" should {
      "return a token wen ok" in {
        val expectedToken = "liuqhe44398qhnfjkdvnb9q84hiungerhiuh"
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(Some(expectedToken.asInstanceOf[T]))
          }
        }

        val datadisParams = DatadisParams("https://localhost", "goodNif", "goodPassword")
        val result = client.login(datadisParams)
        result.isRight shouldBe true
        result.toOption.get shouldBe Some(expectedToken)
      }

      "return an error when bad login" in {
        val serverResult = "{\"timestamp\":\"2024-01-15T11:20:46.814+0000\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Bad credentials\",\"path\":\"/nikola-auth/tokens/login\"}"

        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error - $serverResult", ErrorCodeConstants.HttpResponseError, None))
          }
        }

        val datadisParams = DatadisParams("https://localhost", "badnif", "badpassword")
        val result = client.login(datadisParams)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getSupplies" should {
      "return a list of supplies when ok" in {
        val expectedSupplies = Some(List(Supply("One Street", "8764534WS", "28000", "Madrid", "Madrid", "UFD", "2024/01/01", "", 1, "5")))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedSupplies.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSupplies(datadisParams, token)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedSupplies
      }

      "return a empty list of supplies when no supplies" in {
        val expectedSupplies = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedSupplies.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSupplies(datadisParams, token)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedSupplies
      }

      "return None when no supplies" in {
        val expectedSupplies = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedSupplies.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSupplies(datadisParams, token)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedSupplies
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSupplies(datadisParams, token)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getContractDetail" should {
      "return a contract detail when ok" in {
        val expectedContractDetail = Some(List(Contract("t5424yhwt", "dist", "mark", "Baja 220", "2.0T", "Madrid", "Madrid", "28000", List(4.6, 3.4), "", "", "2024/01/01", "", "2T", None, None, None, None, None, None, None)))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedContractDetail.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "t54424yhwt"
        val distributorCode = "5"

        val result = client.getContractDetail(datadisParams, token, cups, distributorCode)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedContractDetail
      }

      "return None when no contract detail" in {
        val expectedContractDetail = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedContractDetail.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"
        val cups = "8764534WS"
        val distributorCode = "6"

        val result = client.getContractDetail(datadisParams, token, cups, distributorCode)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedContractDetail
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"
        val cups = "8764534WS"
        val distributorCode = "6"

        val result = client.getContractDetail(datadisParams, token, cups, distributorCode)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getConsumptionData" should {
      "return a consumption data when ok" in {
        val expectedConsumptionData = Some(List(Consumption("8764534WS", "2024/01", "01:00", 0.127, "Estimada", 0.0)))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"

        val result = client.getConsumptionData(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return a empty list of consumption when no consumption" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"

        val result = client.getConsumptionData(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no consumption" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"

        val result = client.getConsumptionData(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"

        val result = client.getConsumptionData(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getMaxPower" should {
      "return a max power when ok" in {
        val expectedMaxPower = Some(List(MaxPower("8764534WS", "2024/01/01","12:45", 0.127, "1")))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String,
                                         proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedMaxPower.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPower(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedMaxPower
      }

      "return a empty list of max power when no max power" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPower(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no max power" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPower(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPower(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getSuppliesV2" should {
      "return a list of supplies when ok" in {
        val expectedSupplies = Some(List(SupplyV2("One Street", "8764534WS", "28000", "Madrid", "Madrid", "UFD", "2024/01/01", None, None, 4, "5", None, None, None, false)))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedSupplies.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSuppliesV2(datadisParams, token)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedSupplies
      }

      "return None when no supplies" in {
        val expectedSupplies = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedSupplies.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSuppliesV2(datadisParams, token)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedSupplies
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val result = client.getSuppliesV2(datadisParams, token)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getContractDetailV2" should {
      "return a contract detail when ok" in {
        val expectedContractDetail = Some(List(Contract("t5424yhwt", "dist", "mark", "Baja 220", "2.0T", "Madrid", "Madrid", "28000", List(4.6, 3.4), "", "", "2024/01/01", "", "2T", None, None, None, None, None, None, None)))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedContractDetail.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "t54424yhwt"
        val distributorCode = "5"

        val result = client.getContractDetailV2(datadisParams, token, cups, distributorCode)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedContractDetail
      }

      "return None when no contract detail" in {
        val expectedContractDetail = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedContractDetail.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"
        val cups = "8764534WS"
        val distributorCode = "6"

        val result = client.getContractDetailV2(datadisParams, token, cups, distributorCode)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedContractDetail
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"
        val cups = "8764534WS"
        val distributorCode = "6"

        val result = client.getContractDetailV2(datadisParams, token, cups, distributorCode)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getTimeCurveDataHours" should {
      "return a consumption data when ok" in {
        val expectedConsumptionData = Some(ConsumptionV2HourlyResponse(List(ConsumptionV2Hourly("8764534WS", "2024/01/01", "01:00", 0.127, 1, 0, "Estimada", 0.0, "Valle")), None, None))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataHours(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return a empty list of consumption when no consumption" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataHours(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no consumption" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataHours(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)


        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"

        val result = client.getConsumptionData(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getTimeCurveDataWeek" should {
      "return a consumption data when ok" in {
        val expectedConsumptionData = Some(ConsumptionV2WeeklyResponse(List(ConsumptionV2Weekly("8764534WS", "2024/01/01", 0.127, 0.0, "Estimada", Some(0.127), None, None, None, None, None, None, None, None, None)),
          None, None, None, None, None, None, None, None, None, None, None, None, 1, None, None, 0))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataWeek(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return a empty list of consumption when no consumption" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataWeek(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no consumption" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataWeek(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)


        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataWeek(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getTimeCurveDataMonth" should {
      "return a consumption data when ok" in {
        val expectedConsumptionData = Some(ConsumptionV2MonthlyResponse(List(ConsumptionV2Monthly("8764534WS", "2024/01/01", 0.127, 0.0, "Estimada", Some(0.127), None, None, None, None, None, None, None, None, None)),
          None, None, None, None, None, None, None, None, None, None, None, None, 1, None, None, 0))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataMonth(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return a empty list of consumption when no consumption" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataMonth(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no consumption" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataMonth(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)


        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))

          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"
        val measurementType = "0"
        val pointType = "4"
        val hasSelfConsumption = false
        val provinceCode = "28"
        val accessFareCode = "2T"

        val result = client.getTimeCurveDataMonth(datadisParams, token, cups, distributorCode, startDate, endDate, measurementType, pointType, hasSelfConsumption, provinceCode, accessFareCode, None)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }

    "getMaxPowerV2" should {
      "return a max power when ok" in {
        val expectedMaxPower = Some(MaxPowerV2Response(List(MaxPowerV2("8764534WS", "2024/01/01","12:45", 0.127, "1", 0))))
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String,
                                         proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedMaxPower.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")

        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPowerV2(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedMaxPower
      }

      "return a empty list of max power when no max power" in {
        val expectedConsumptionData = Some(List())
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPowerV2(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return None when no max power" in {
        val expectedConsumptionData = None
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Right(expectedConsumptionData.asInstanceOf[Option[T]])
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPowerV2(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isRight shouldBe true
        result.toOption.get shouldBe expectedConsumptionData
      }

      "return error when error in request" in {
        val client = new DatadisClient {
          override def executeRequest[T](url: String, method: HttpMethod, body: RequestEntity, token: String, proxyParams: Option[ProxyParams])(implicit um: Unmarshaller[HttpResponse, T]): Either[DatadisAnalyzerError, Option[T]] = {
            Left(HttpError(s"Error 500 Internal Server Error", ErrorCodeConstants.HttpResponseError, None))
          }
        }

        val datadisParams = DatadisParams("https://localhost.es", "384756", "passwod")
        val token = "laurhglaiurhgliurh"

        val cups = "8764534WS"
        val distributorCode = "5"
        val startDate = "2024/01"
        val endDate = "2024/01"

        val result = client.getMaxPowerV2(datadisParams, token, cups, distributorCode, startDate, endDate)

        result.isLeft shouldBe true
        result.left.get.code shouldBe ErrorCodeConstants.HttpResponseError
      }
    }
  }
}
