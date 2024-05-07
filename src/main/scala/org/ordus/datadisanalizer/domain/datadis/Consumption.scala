package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups CUPS
 * @param date Date (yyyy/MM/dd)
 * @param time Time (HH:mm)
 * @param consumptionKWh Consumption (kWh)
 * @param obtainMethod Obtain method (Real/Estimada)
 * @param surplusEnergyKWh Surplus energy (kWh)
 */
case class Consumption(cups: String, date: String, time: String, consumptionKWh: Double, obtainMethod: String, surplusEnergyKWh: Double)
