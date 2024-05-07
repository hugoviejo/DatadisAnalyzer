package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups CUPS
 * @param date Date (yyyy/MM/dd)
 * @param time Time (HH:mm)
 * @param maxPower Max power (kW)
 * @param period Period (1, 2, 3, 4, 5 or 6)
 */
case class MaxPower(cups: String, date: String,	time: String, maxPower: Double, period: String)
