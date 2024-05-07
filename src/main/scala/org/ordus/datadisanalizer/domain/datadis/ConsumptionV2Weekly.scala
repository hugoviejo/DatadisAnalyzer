package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups CUPS
 * @param date Date (yyyy/MM/dd)
 * @param consumptionKWh Consumption (kWh)
 * @param energyPoured Energy poured (kWh)
 * @param obtainingMethodDescription Obtaining method description (Real/Estimada)
 * @param totalValle Total Valle period (kWh)
 * @param totalLlano Total Llano period (kWh)
 * @param totalPunta Total Punta period (kWh)
 * @param totalP1 Total P1 period (kWh)
 * @param totalP2 Total P2 period (kWh)
 * @param totalP3 Total P3 period (kWh)
 * @param totalP4 Total P4 period (kWh)
 * @param totalP5 Total P5 period (kWh)
 * @param totalP6 Total P6 period (kWh)
 * @param totalYearly Total yearly period (kWh)
 */
case class ConsumptionV2Weekly(cups: String, date: String, consumptionKWh: Double, energyPoured: Double,
                               obtainingMethodDescription: String, totalValle: Option[Double], totalLlano: Option[Double], totalPunta: Option[Double],
                               totalP1: Option[Double], totalP2: Option[Double], totalP3: Option[Double], totalP4: Option[Double], totalP5: Option[Double],
                               totalP6: Option[Double], totalYearly: Option[Double])

/**
 *
 * @param timeCurveList Consumption list
 * @param mediumCurveList Medium curve list
 * @param mediumCurveAutoList Medium curve auto list
 * @param totalValle Total Valle period (kWh)
 * @param totalLlano Total Llano period (kWh)
 * @param totalPunta Total Punta period (kWh)
 * @param totalP1 Total P1 period (kWh)
 * @param totalP2 Total P2 period (kWh)
 * @param totalP3 Total P3 period (kWh)
 * @param totalP4 Total P4 period (kWh)
 * @param totalP5 Total P5 period (kWh)
 * @param totalP6 Total P6 period (kWh)
 * @param totalYearly Total yearly period (kWh)
 * @param fareGroup Fare group
 * @param total Total consumption (kWh)
 * @param totalSelfConsumption Total self consumption (kWh)
 * @param error Error code
 */
case class ConsumptionV2WeeklyResponse(timeCurveList: List[ConsumptionV2Weekly], mediumCurveList: Option[String], mediumCurveAutoList: Option[String],
                                       totalValle: Option[Double], totalLlano: Option[Double], totalPunta: Option[Double], totalP1: Option[Double],
                                       totalP2: Option[Double], totalP3: Option[Double], totalP4: Option[Double], totalP5: Option[Double],
                                       totalP6: Option[Double], totalYearly: Option[Double], fareGroup: Int, total: Option[Double],
                                       totalSelfConsumption: Option[Double], error: Int)

case class ConsumptionV2WeeklyResponseWrapper(response: ConsumptionV2WeeklyResponse)