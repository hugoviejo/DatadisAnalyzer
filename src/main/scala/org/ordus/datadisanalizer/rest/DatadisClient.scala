package org.ordus.datadisanalizer.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods}
import org.ordus.datadisanalizer.constants.DatadisAnalyzerConstants
import org.ordus.datadisanalizer.domain.DatadisParams
import org.ordus.datadisanalizer.domain.datadis._
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.rest.DatadisProtocol._
import org.slf4j.{Logger, LoggerFactory}


trait DatadisClient extends ApiClient {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
   * Login to Datadis
   *
   * @param datadisParams Datadis params
   * @return The token
   */
  def login(datadisParams: DatadisParams): Either[DatadisAnalyzerError, Option[String]] = {
    logger.info("DATADIS - Login")
    executeRequest[String](s"${datadisParams.url}/nikola-auth/tokens/login?username=${datadisParams.user}&password=${datadisParams.password}",
      HttpMethods.POST, HttpEntity.Empty, "", datadisParams.proxyParams)
  }

  /**
   * Get Supplies
   *
   * @param datadisParams Datadis params
   * @param token         The token
   * @return The list of supplies
   */
  def getSupplies(datadisParams: DatadisParams, token: String): Either[DatadisAnalyzerError, Option[List[Supply]]] = {
    logger.info("DATADIS - Get Supplies")
    executeRequest[List[Supply]](s"${datadisParams.url}/api-private/api/get-supplies",
      HttpMethods.GET, HttpEntity.Empty, token, datadisParams.proxyParams)
  }

  /**
   * Get Contract Detail
   *
   * @param datadisParams   Datadis params
   * @param token           The token
   * @param cups            The cups
   * @param distributorCode The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @return The contract detail
   */
  def getContractDetail(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String):
  Either[DatadisAnalyzerError, Option[List[Contract]]] = {
    logger.info("DATADIS - Get Contract Detail")
    executeRequest[List[Contract]](s"${datadisParams.url}/api-private/api/get-contract-detail?cups=$cups&distributorCode=$distributorCode",
      HttpMethods.GET, HttpEntity.Empty, token, datadisParams.proxyParams)
  }

  /**
   * Get Consumption Data
   *
   * @param datadisParams   Datadis params
   * @param token           The token
   * @param cups            The cups
   * @param distributorCode The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate       The start date (yyyy/MM)
   * @param endDate         The end date (yyyy/MM)
   * @param measurementType The measurement type (0: Hourly, 1: Quarter Hourly) (Quarter Hourly only supported for pointType 1 and 2 Or if "E-distribución" is the distributor pointType 3)
   * @param pointType       The point type (1, 2, 3, 4, 5)
   * @return The consumption data
   */
  def getConsumptionData(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String,
                         measurementType: String, pointType: String): Either[DatadisAnalyzerError, Option[List[Consumption]]] = {
    logger.info("DATADIS - Get Consumption Data")
    executeRequest[List[Consumption]](s"${datadisParams.url}/api-private/api/get-consumption-data?cups=$cups&distributorCode=$distributorCode" +
      s"&startDate=$startDate&endDate=$endDate&measurementType=$measurementType&pointType=$pointType",
      HttpMethods.GET, HttpEntity.Empty, token, datadisParams.proxyParams)
  }

  /**
   * Get Max Power
   *
   * @param datadisParams   Datadis params
   * @param token           The token
   * @param cups            The cups
   * @param distributorCode The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate       The start date (yyyy/MM)
   * @param endDate         The end date (yyyy/MM)
   * @return The max power
   */
  def getMaxPower(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String):
  Either[DatadisAnalyzerError, Option[List[MaxPower]]] = {
    logger.info("DATADIS - Get Max Power")
    executeRequest[List[MaxPower]](s"${datadisParams.url}/api-private/api/get-max-power?cups=$cups&distributorCode=$distributorCode" +
      s"&startDate=$startDate&endDate=$endDate",
      HttpMethods.GET, HttpEntity.Empty, token, datadisParams.proxyParams)
  }

  // V2 methods

  /**
   * Get Supplies V2
   *
   * @param datadisParams Datadis params
   * @param token         The token
   * @return The list of supplies
   */
  def getSuppliesV2(datadisParams: DatadisParams, token: String): Either[DatadisAnalyzerError, Option[SupplyV2Response]] = {
    logger.info("DATADIS - Get Supplies V2")
    executeRequest[SupplyV2Response](s"${datadisParams.url}/api-private/getSupplies",
      HttpMethods.GET, HttpEntity.Empty, token, datadisParams.proxyParams)
  }

  /**
   * Get Contract Detail V2
   *
   * @param datadisParams   Datadis params
   * @param token           The token
   * @param cups            The cups
   * @param distributorCode The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @return
   */
  def getContractDetailV2(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String)
  : Either[DatadisAnalyzerError, Option[ContractV2Response]] = {
    logger.info("DATADIS - Get Contract Detail V2")
    val data = s"""{"cups":["$cups"],"distributor":"$distributorCode"}"""
    executeRequest[ContractV2Response](s"${datadisParams.url}/api-private/supply-data/contractual-data",
      HttpMethods.POST, HttpEntity(ContentTypes.`application/json`, data), token, datadisParams.proxyParams)
  }

  /**
   * Get Consumption Data V2 Hourly
   *
   * @param datadisParams           Datadis params
   * @param token                   The token
   * @param cups                    The cups
   * @param distributorCode         The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate               The start date (yyyy/MM/dd)
   * @param endDate                 The end date (yyyy/MM/dd)
   * @param measurementType         The measurement type (0: Hourly, 1: Quarter Hourly) (Quarter Hourly only supported for pointType 1 and 2 Or if "E-distribución" is the distributor pointType 3)
   * @param pointType               The point type (1, 2, 3, 4, 5)
   * @param hasSelfConsumption      If the supply has self consumption
   * @param provinceCode            The province code
   * @param accessFareCode          The access fare code
   * @param selfConsumptionTypeCode The self consumption type code (Codes from CNMC). Only when self consumption.
   * @return
   */
  def getTimeCurveDataHours(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String,
                            measurementType: String, pointType: String, hasSelfConsumption: Boolean, provinceCode: String, accessFareCode: String,
                            selfConsumptionTypeCode: Option[String]): Either[DatadisAnalyzerError, Option[ConsumptionV2HourlyResponse]] = {
    logger.info("DATADIS - Get Time Curve Data Hours")
    val data =
      s"""{"fechaInicial":"$startDate","fechaFinal":"$endDate","cups":["$cups"],"distributor":"$distributorCode","fraccion":$measurementType,
         |"hasAutoConsumo":$hasSelfConsumption,"provinceCode":"$provinceCode","tarifaCode":"$accessFareCode","tipoPuntoMedida":$pointType,
         |"tipoAutoConsumo":${selfConsumptionTypeCode.orNull}}""".stripMargin
    executeRequest[ConsumptionV2HourlyResponseWrapper](s"${datadisParams.url}/api-private/supply-data/v2/time-curve-data/hours",
      HttpMethods.POST, HttpEntity(ContentTypes.`application/json`, data), token, datadisParams.proxyParams) match {
      case Right(Some(response)) => Right(Some(response.response))
      case Right(None) => Right(None)
      case Left(error) => Left(error)
    }
  }

  /**
   * Get Consumption Data V2 Weekly
   *
   * @param datadisParams           Datadis params
   * @param token                   The token
   * @param cups                    The cups
   * @param distributorCode         The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate               The start date (yyyy/MM/dd)
   * @param endDate                 The end date (yyyy/MM/dd)
   * @param measurementType         The measurement type (0: Hourly, 1: Quarter Hourly) (Quarter Hourly only supported for pointType 1 and 2 Or if "E-distribución" is the distributor pointType 3)
   * @param pointType               The point type (1, 2, 3, 4, 5)
   * @param hasSelfConsumption      If the supply has self consumption
   * @param provinceCode            The province code
   * @param accessFareCode          The access fare code
   * @param selfConsumptionTypeCode The self consumption type code (Codes from CNMC). Only when self consumption.
   * @return
   */
  def getTimeCurveDataWeek(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String,
                            measurementType: String, pointType: String, hasSelfConsumption: Boolean, provinceCode: String, accessFareCode: String,
                            selfConsumptionTypeCode: Option[String]): Either[DatadisAnalyzerError, Option[ConsumptionV2WeeklyResponse]] = {
    logger.info("DATADIS - Get Time Curve Data Week")
    val data =
      s"""{"fechaInicial":"$startDate","fechaFinal":"$endDate","cups":["$cups"],"distributor":"$distributorCode","fraccion":$measurementType,
         |"hasAutoConsumo":$hasSelfConsumption,"provinceCode":"$provinceCode","tarifaCode":"$accessFareCode","tipoPuntoMedida":$pointType,
         |"tipoAutoConsumo":${selfConsumptionTypeCode.orNull}}""".stripMargin
    executeRequest[ConsumptionV2WeeklyResponseWrapper](s"${datadisParams.url}/api-private/supply-data/v2/time-curve-data/week",
      HttpMethods.POST, HttpEntity(ContentTypes.`application/json`, data), token, datadisParams.proxyParams) match {
      case Right(Some(response)) => Right(Some(response.response))
      case Right(None) => Right(None)
      case Left(error) => Left(error)
    }
  }

  /**
   * Get Consumption Data V2 Weekly
   *
   * @param datadisParams           Datadis params
   * @param token                   The token
   * @param cups                    The cups
   * @param distributorCode         The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate               The start date (yyyy/MM/dd)
   * @param endDate                 The end date (yyyy/MM/dd)
   * @param measurementType         The measurement type (0: Hourly, 1: Quarter Hourly) (Quarter Hourly only supported for pointType 1 and 2 Or if "E-distribución" is the distributor pointType 3)
   * @param pointType               The point type (1, 2, 3, 4, 5)
   * @param hasSelfConsumption      If the supply has self consumption
   * @param provinceCode            The province code
   * @param accessFareCode          The access fare code
   * @param selfConsumptionTypeCode The self consumption type code (Codes from CNMC). Only when self consumption.
   * @return
   */
  def getTimeCurveDataMonth(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String,
                            measurementType: String, pointType: String, hasSelfConsumption: Boolean, provinceCode: String, accessFareCode: String,
                            selfConsumptionTypeCode: Option[String]): Either[DatadisAnalyzerError, Option[ConsumptionV2MonthlyResponse]] = {
    logger.info("DATADIS - Get Time Curve Data Month")
    val data =
      s"""{"fechaInicial":"$startDate","fechaFinal":"$endDate","cups":["$cups"],"distributor":"$distributorCode","fraccion":$measurementType,
         |"hasAutoConsumo":$hasSelfConsumption,"provinceCode":"$provinceCode","tarifaCode":"$accessFareCode","tipoPuntoMedida":$pointType,
         |"tipoAutoConsumo":${selfConsumptionTypeCode.orNull}}""".stripMargin
    executeRequest[ConsumptionV2MonthlyResponseWrapper](s"${datadisParams.url}/api-private/supply-data/v2/time-curve-data/month",
      HttpMethods.POST, HttpEntity(ContentTypes.`application/json`, data), token, datadisParams.proxyParams) match {
      case Right(Some(response)) => Right(Some(response.response))
      case Right(None) => Right(None)
      case Left(error) => Left(error)
    }
  }


  /**
   * Get Max Power
   *
   * @param datadisParams   Datadis params
   * @param token           The token
   * @param cups            The cups
   * @param distributorCode The distributor code (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
   * @param startDate       The start date (yyyy/MM)
   * @param endDate         The end date (yyyy/MM)
   * @return The max power
   */
  def getMaxPowerV2(datadisParams: DatadisParams, token: String, cups: String, distributorCode: String, startDate: String, endDate: String):
  Either[DatadisAnalyzerError, Option[MaxPowerV2Response]] = {
    logger.info("DATADIS - Get Max Power v2")
    val data =
      s"""{"fechaInicial":"$startDate","fechaFinal":"$endDate","cups":["$cups"],"distributor":"$distributorCode"}""".stripMargin
    executeRequest[MaxPowerV2Response](s"${datadisParams.url}/api-private/supply-data/max-power",
      HttpMethods.POST, HttpEntity(ContentTypes.`application/json`, data), token, datadisParams.proxyParams)
  }
}
