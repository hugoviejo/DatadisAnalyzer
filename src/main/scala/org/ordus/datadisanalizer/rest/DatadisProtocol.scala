package org.ordus.datadisanalizer.rest

import org.ordus.datadisanalizer.domain.datadis._
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, jsonReader}

object DatadisProtocol extends DefaultJsonProtocol {
  implicit val supplyFormat: RootJsonFormat[Supply] = jsonFormat10(Supply)
  implicit val contractFormat: RootJsonFormat[Contract] = jsonFormat21(Contract)
  implicit val consumptionFormat: RootJsonFormat[Consumption] = jsonFormat6(Consumption)
  implicit val maxPowerFormat: RootJsonFormat[MaxPower] = jsonFormat5(MaxPower)

  // V2 formats
  implicit val supplyV2Format: RootJsonFormat[SupplyV2] = jsonFormat(SupplyV2, "direccion", "cups", "codigoPostal", "provincia", "municipio", "distribuidora",
    "fechaVigenciaDesde", "fechaVigenciaHasta", "error", "tipoPunto", "cod_disitribuidora", "startDate", "endDate", "url", "hasAutoconsumo")
  implicit val supplyV2ResponseFormat: RootJsonFormat[SupplyV2Response] = jsonFormat(SupplyV2Response, "response", "CodError", "DistributorsError",
    "Distributors")

  implicit object contractV2Format extends RootJsonFormat[ContractV2] {
    def write(c: ContractV2): JsObject = JsObject(
      "cups" -> JsString(c.cups),
      "distribuidor" -> JsString(c.distributor),
      "comercializador" -> JsString(c.marketer),
      "tension" -> JsString(c.tension),
      "tarifaAcceso" -> JsString(c.accessFare),
      "tarifaAccesoCode" -> JsString(c.accessFareCode),
      "discriminacionHoraria" -> JsString(c.timeDiscrimination),
      "provincia" -> JsString(c.province),
      "provinciaCode" -> JsString(c.provinceCode),
      "municipio" -> JsString(c.municipality),
      "codigoPostal" -> JsString(c.postalCode),
      "potenciaContratada" -> JsArray(c.contractedPowerkW.map(JsNumber(_)).toVector),
      "modoControlPotencia" -> JsNumber(c.modePowerControl),
      "modoControlPotenciaName" -> JsString(c.modePowerControlName),
      "fechaInicio" -> JsString(c.startDate),
      "fechaFin" -> JsString(c.endDate),
      "error" -> JsNumber(c.errorCode),
      "url" -> JsString(c.url),
      "potenciaTotalGenerable" -> JsNumber(c.installedCapacityKW.getOrElse(0.0)),
      "tipoAutoConsumo" -> JsString(c.selfConsumptionTypeCode.getOrElse("")),
      "descTipoAutoConsumo" -> JsString(c.selfConsumptionTypeDesc.getOrElse("")),
      "seccion" -> JsString(c.section.getOrElse("")),
      "subseccion" -> JsString(c.subsection.getOrElse("")),
      "coeficienteReparto" -> JsNumber(c.partitionCoefficient.getOrElse(0.0)),
      "cau" -> JsString(c.cau.getOrElse(""))
    )

    def read(value: JsValue): ContractV2 = {
      val cups = fromField[String](value, "cups")
      val distributor = fromField[String](value, "distribuidor")
      val marketer = fromField[String](value, "comercializador")
      val tension = fromField[String](value, "tension")
      val accessFare = fromField[String](value, "tarifaAcceso")
      val accessFareCode = fromField[String](value, "tarifaAccesoCode")
      val timeDiscrimination = fromField[String](value, "discriminacionHoraria")
      val province = fromField[String](value, "provincia")
      val provinceCode = fromField[String](value, "provinciaCode")
      val municipality = fromField[String](value, "municipio")
      val postalCode = fromField[String](value, "codigoPostal")
      val contractPower = fromField[List[Double]](value, "potenciaContratada")
      val modePowerControl = fromField[Int](value, "modoControlPotencia")
      val modePowerControlName = fromField[String](value, "modoControlPotenciaName")
      val startDate = fromField[String](value, "fechaInicio")
      val endDate = fromField[String](value, "fechaFin")
      val errorCode = fromField[Int](value, "error")
      val url = fromField[String](value, "url")
      val installedCapacityKW = fromField[Option[Double]](value, "potenciaTotalGenerable")
      val selfConsumptionTypeCode = fromField[Option[String]](value, "tipoAutoConsumo")
      val selfConsumptionTypeDesc = fromField[Option[String]](value, "descTipoAutoConsumo")
      val section = fromField[Option[String]](value, "seccion")
      val subsection = fromField[Option[String]](value, "subseccion")
      val partitionCoefficient = fromField[Option[Double]](value, "coeficienteReparto")
      val cau = fromField[Option[String]](value, "cau")

      ContractV2(cups, distributor, marketer, tension, accessFare, accessFareCode, timeDiscrimination, province, provinceCode, municipality,
        postalCode, contractPower, modePowerControl, modePowerControlName, startDate, endDate, errorCode, url,
        installedCapacityKW, selfConsumptionTypeCode, selfConsumptionTypeDesc, section, subsection,
        partitionCoefficient, cau)
    }
  }

  implicit val contractV2ResponseFormat: RootJsonFormat[ContractV2Response] = jsonFormat(ContractV2Response, "response")

  implicit val consumptionV2Format: RootJsonFormat[ConsumptionV2Hourly] = jsonFormat(ConsumptionV2Hourly, "cups", "date", "hour", "measureMagnitudeActive",
    "obtainingMethod", "error", "metodoObtencion", "energyPoured", "period")
  implicit val consumptionV2HourlyResponseFormat: RootJsonFormat[ConsumptionV2HourlyResponse] = jsonFormat3(ConsumptionV2HourlyResponse)
  implicit val consumptionV2HourlyResponseWrapperFormat: RootJsonFormat[ConsumptionV2HourlyResponseWrapper] = jsonFormat1(ConsumptionV2HourlyResponseWrapper)

  implicit val consumptionV2WeeklyFormat: RootJsonFormat[ConsumptionV2Weekly] = jsonFormat(ConsumptionV2Weekly, "cups", "semanaDia", "consumo",
    "autoConsumo", "metodoObtencion", "totalValle", "totalLlano", "totalPunta", "totalP1", "totalP2", "totalP3", "totalP4", "totalP5",
    "totalP6", "totalAnual")
  implicit val consumptionV2WeeklyResponseFormat: RootJsonFormat[ConsumptionV2WeeklyResponse] = jsonFormat(ConsumptionV2WeeklyResponse, "semanaDia",
    "mediumCurveList", "mediumCurveAutoList", "totalValle", "totalLlano", "totalPunta", "totalP1", "totalP2", "totalP3", "totalP4", "totalP5", "totalP6",
    "totalAnual", "fareGroup", "total", "totalAutoConsumo", "error")
  implicit val consumptionV2WeeklyResponseWrapperFormat: RootJsonFormat[ConsumptionV2WeeklyResponseWrapper] = jsonFormat1(ConsumptionV2WeeklyResponseWrapper)

  implicit val consumptionV2MonthlyFormat: RootJsonFormat[ConsumptionV2Monthly] = jsonFormat(ConsumptionV2Monthly, "cups", "anoMes", "consumo",
    "autoConsumo", "metodoObtencion", "totalValle", "totalLlano", "totalPunta", "totalP1", "totalP2", "totalP3", "totalP4", "totalP5",
    "totalP6", "totalAnual")
  implicit val consumptionV2MonthlyResponseFormat: RootJsonFormat[ConsumptionV2MonthlyResponse] = jsonFormat(ConsumptionV2MonthlyResponse, "anoMes",
    "mediumCurveList", "mediumCurveAutoList", "totalValle", "totalLlano", "totalPunta", "totalP1", "totalP2", "totalP3", "totalP4", "totalP5", "totalP6",
    "totalAnual", "fareGroup", "total", "totalAutoConsumo", "error")
  implicit val consumptionV2MonthlyResponseWrapperFormat: RootJsonFormat[ConsumptionV2MonthlyResponseWrapper] = jsonFormat1(ConsumptionV2MonthlyResponseWrapper)

  implicit val maxPowerV2Format: RootJsonFormat[MaxPowerV2] = jsonFormat(MaxPowerV2, "cups", "fechaMaximo", "hora", "maximoPotenciaDemandada", "periodo",
    "error")
  implicit val maxPowerV2ResponseFormat: RootJsonFormat[MaxPowerV2Response] = jsonFormat1(MaxPowerV2Response)
}
