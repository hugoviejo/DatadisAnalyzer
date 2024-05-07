package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups CUPS
 * @param date Date (yyyy/MM/dd)
 * @param hour Hour (HH:mm)
 * @param consumptionKWh Measure magnitude active (kWh)
 * @param obtainingMethodCode Obtaining method code (Real/Estimada)
 * @param error Error message
 * @param obtainingMethodDescription Obtaining method description (Real/Estimada)
 * @param energyPoured Energy poured (kWh)
 * @param period Period (Punta/Llano/Valle)
 */
case class ConsumptionV2Hourly(cups: String, date: String, hour: String, consumptionKWh: Double, obtainingMethodCode: Int, error: Int,
                               obtainingMethodDescription: String, energyPoured: Double, period: String)

/**
 *
 * @param timeCurveList Consumption list
 * @param mediumCurveList Medium curve list
 * @param mediumCurveAutoList Medium curve auto list
 */
case class ConsumptionV2HourlyResponse(timeCurveList: List[ConsumptionV2Hourly], mediumCurveList: Option[String], mediumCurveAutoList: Option[String])

case class ConsumptionV2HourlyResponseWrapper(response: ConsumptionV2HourlyResponse)