package org.ordus.datadisanalizer.rest

object DatadisServerResponses {
  val Token: String = "thisIsAToken"

  val SupplyR: String =
    """
      |  [
      |    {
      |      "address":"this street",
      |      "cups":"648376872634W",
      |      "postalCode":"28001",
      |      "province":"MADRID",
      |      "municipality":"MADRID-MADRID",
      |      "distributor":"UFD",
      |      "validDateFrom":"2024/01/01",
      |      "validDateTo":"",
      |      "pointType":4,
      |      "distributorCode":"5"
      |    }
      |  ]
      |""".stripMargin

  val ContractR: String =
    """
      | [
      |    {
      |      "cups":"648376872634W",
      |      "distributor":"UFD",
      |      "marketer":"My marketer",
      |      "tension":"Baja 230",
      |      "accesFare":"2.0TD PEAJE ATR",
      |      "province":"MADRID",
      |      "municipality":"MADRID-MADRID",
      |      "postalCode":"28001",
      |      "contractedPowerkW":[ 3.4, 3.4 ],
      |      "timeDiscrimination":"",
      |      "modePowerControl":"",
      |      "startDate":"2024/01/01",
      |      "endDate":"",
      |      "codeFare":"2T",
      |      "selfConsumptionTypeCode":null,
      |      "selfConsumptionTypeDesc":null,
      |      "section":null,
      |      "subsection":null,
      |      "partitionCoefficient":null,
      |      "cau":null,
      |      "installedCapacityKW":null
      |    }
      |  ]
      |""".stripMargin

  val ConsumptionR: String =
    """
      |[
      |    {
      |      "cups":"648376872634W",
      |      "date":"2024/01/01",
      |      "time":"24:00",
      |      "consumptionKWh":0.021,
      |      "obtainMethod":"Real",
      |      "surplusEnergyKWh":0.0
      |    },
      |    {
      |      "cups":"648376872634W",
      |      "date":"2024/01/02",
      |      "time":"01:00",
      |      "consumptionKWh":0.025,
      |      "obtainMethod":"Real",
      |      "surplusEnergyKWh":0.0
      |    }
      |  ]
      |""".stripMargin

  val MaxPowerR: String =
    """
      |  [
      |    {
      |      "cups":"648376872634W",
      |      "date":"2023/12/07",
      |      "time":"12:45",
      |      "maxPower":2.62,
      |      "period":"1"
      |    },
      |    {
      |      "cups":"648376872634W",
      |      "date":"2023/12/13",
      |      "time":"08:45",
      |      "maxPower":2.646,
      |      "period":"2"
      |    },
      |    {
      |      "cups":"648376872634W",
      |      "date":"2023/12/09",
      |      "time":"12:15",
      |      "maxPower":3.823,
      |      "period":"3"
      |    }
      |  ]
      |""".stripMargin

  val SupplyV2ResponseR: String =
    """{
      |    "response": [
      |        {
      |            "direccion": "this street",
      |            "cups": "648376872634W",
      |            "codigoPostal": "28001",
      |            "provincia": "MADRID",
      |            "municipio": "MADRID-MADRID",
      |            "distribuidora": "UFD",
      |            "fechaVigenciaDesde": "2024/01/01",
      |            "fechaVigenciaHasta": null,
      |            "error": null,
      |            "tipoPunto": 4,
      |            "cod_disitribuidora": "5",
      |            "startDate": null,
      |            "endDate": null,
      |            "url": "https://www.ufd.es/",
      |            "hasAutoconsumo": false
      |        }
      |    ],
      |    "CodError": "902",
      |    "DistributorsError": "",
      |    "Distributors": "UFD"
      |}""".stripMargin

  val ContractV2ResponseR: String =
    """{
      |    "response": [
      |        {
      |            "cups": "648376872634W",
      |            "distribuidor": "UFD",
      |            "comercializador": "My marketer",
      |            "tension": "Baja 230",
      |            "tarifaAcceso": "2.0TD PEAJE ATR",
      |            "tarifaAccesoCode": "2T",
      |            "discriminacionHoraria": "",
      |            "provincia": "MADRID",
      |            "provinciaCode": "28",
      |            "municipio": "MADRID-MADRID",
      |            "codigoPostal": "28001",
      |            "potenciaContratada": [
      |                3.4,
      |                3.4
      |            ],
      |            "modoControlPotencia": 0,
      |            "modoControlPotenciaName": "",
      |            "fechaInicio": "2024/01/01",
      |            "fechaFin": "",
      |            "error": 0,
      |            "url": "https://www.ufd.es/",
      |            "potenciaTotalGenerable": null,
      |            "tipoAutoConsumo": null,
      |            "seccion": null,
      |            "subseccion": null,
      |            "coeficienteReparto": null,
      |            "cau": null,
      |            "descTipoAutoConsumo": null
      |        }
      |    ]
      |}""".stripMargin

  val ConsumptionV2HourlyResponseR: String =
    """
      |{
      |    "response": {
      |        "timeCurveList": [
      |            {
      |                "cups": "648376872634W",
      |                "date": "2024/01/01",
      |                "hour": "24:00",
      |                "measureMagnitudeActive": 0.021,
      |                "obtainingMethod": 1,
      |                "error": 0,
      |                "metodoObtencion": "Real",
      |                "energyPoured": 0.0,
      |                "period": "LLANO"
      |            },
      |            {
      |                "cups": "648376872634W",
      |                "date": "2024/01/02",
      |                "hour": "01:00",
      |                "measureMagnitudeActive": 0.025,
      |                "obtainingMethod": 1,
      |                "error": 0,
      |                "metodoObtencion": "Real",
      |                "energyPoured": 0.0,
      |                "period": "VALLE"
      |            }
      |        ],
      |        "mediumCurveList": null,
      |        "mediumCurveAutoList": null
      |    }
      |}
      |""".stripMargin

  val ConsumptionV2WeeklyResponseR: String =
    """
      |{
      |    "response": {
      |        "semanaDia": [
      |            {
      |                "cups": "648376872634W",
      |                "semanaDia": "2024/01/01",
      |                "consumo": 7.6850000000000005,
      |                "autoConsumo": 0.0,
      |                "metodoObtencion": "Estimada",
      |                "totalValle": 0.547,
      |                "totalLlano": 2.581,
      |                "totalPunta": 4.557,
      |                "totalP1": null,
      |                "totalP2": null,
      |                "totalP3": null,
      |                "totalP4": null,
      |                "totalP5": null,
      |                "totalP6": null,
      |                "totalAnual": null
      |            },
      |            {
      |                "cups": "648376872634W",
      |                "semanaDia": "2024/01/02",
      |                "consumo": 2.337,
      |                "autoConsumo": 0.0,
      |                "metodoObtencion": "Estimada",
      |                "totalValle": 2.337,
      |                "totalLlano": null,
      |                "totalPunta": null,
      |                "totalP1": null,
      |                "totalP2": null,
      |                "totalP3": null,
      |                "totalP4": null,
      |                "totalP5": null,
      |                "totalP6": null,
      |                "totalAnual": null
      |            }
      |        ],
      |        "mediumCurveList": null,
      |        "mediumCurveAutoList": null,
      |        "totalValle": null,
      |        "totalLlano": null,
      |        "totalPunta": null,
      |        "totalP1": null,
      |        "totalP2": null,
      |        "totalP3": null,
      |        "totalP4": null,
      |        "totalP5": null,
      |        "totalP6": null,
      |        "totalAnual": null,
      |        "fareGroup": 1,
      |        "total": null,
      |        "totalAutoConsumo": null,
      |        "error": 0
      |    }
      |}
      |""".stripMargin

  val ConsumptionV2MonthlyResponseR: String =
    """
      |{
      |    "response": {
      |        "anoMes": [
      |            {
      |                "anoMes": "2023/12",
      |                "consumo": 0.105,
      |                "autoConsumo": 0.0,
      |                "totalValle": 0.105,
      |                "totalLlano": null,
      |                "totalPunta": null,
      |                "totalP1": null,
      |                "totalP2": null,
      |                "totalP3": null,
      |                "totalP4": null,
      |                "totalP5": null,
      |                "totalP6": null,
      |                "totalAnual": null,
      |                "metodoObtencion": "Estimada",
      |                "cups": "648376872634W"
      |            },
      |            {
      |                "anoMes": "2024/01",
      |                "consumo": 58.479,
      |                "autoConsumo": 0.0,
      |                "totalValle": 12.815,
      |                "totalLlano": 17.018,
      |                "totalPunta": 28.646,
      |                "totalP1": null,
      |                "totalP2": null,
      |                "totalP3": null,
      |                "totalP4": null,
      |                "totalP5": null,
      |                "totalP6": null,
      |                "totalAnual": null,
      |                "metodoObtencion": "Estimada",
      |                "cups": "648376872634W"
      |            }
      |        ],
      |        "totalValle": null,
      |        "totalLlano": null,
      |        "totalPunta": null,
      |        "totalP1": null,
      |        "totalP2": null,
      |        "totalP3": null,
      |        "totalP4": null,
      |        "totalP5": null,
      |        "totalP6": null,
      |        "totalAnual": null,
      |        "total": null,
      |        "totalAutoConsumo": null,
      |        "fareGroup": 1,
      |        "mediumCurveList": null,
      |        "mediumCurveAutoList": null,
      |        "error": 0
      |    }
      |}
      |""".stripMargin

  val MaxPowerV2ResponseR: String =
    """
      |{
      |    "response": [
      |        {
      |            "cups": "648376872634W",
      |            "fechaMaximo": "2023/12/07",
      |            "hora": "12:45",
      |            "error": 0,
      |            "maximoPotenciaDemandada": 2.62,
      |            "periodo": "1"
      |        },
      |        {
      |            "cups": "648376872634W",
      |            "fechaMaximo": "2023/12/13",
      |            "hora": "08:45",
      |            "error": 0,
      |            "maximoPotenciaDemandada": 2.646,
      |            "periodo": "2"
      |        },
      |        {
      |            "cups": "648376872634W",
      |            "fechaMaximo": "2023/12/09",
      |            "hora": "12:15",
      |            "error": 0,
      |            "maximoPotenciaDemandada": 3.823,
      |            "periodo": "3"
      |        }
      |    ]
      |}
      |""".stripMargin
}
