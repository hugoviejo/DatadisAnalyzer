package org.ordus.datadisanalizer.service

import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, DatadisResults}
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.rest.DatadisClient

trait DataRetrieverService extends DatadisClient {
  def retrieveData(datadisAnalyzerParams: DatadisAnalyzerParams, startDate: String, endDate: String): Either[DatadisAnalyzerError, DatadisResults] = {
    for {
      token <- login(datadisAnalyzerParams.datadisParams)
      supplies <- getSuppliesV2(datadisAnalyzerParams.datadisParams, token.get)
      contracts <- getContractDetailV2(datadisAnalyzerParams.datadisParams, token.get, supplies.get.response.head.cups,
        supplies.get.response.head.distributorCode)
      consumptionDataHours <- getTimeCurveDataHours(datadisAnalyzerParams.datadisParams, token.get, supplies.get.response.head.cups,
        supplies.get.response.head.distributorCode, startDate, endDate, "0", supplies.get.response.head.pointType.toString, supplies.get.response.head.hasSelfConsumption,
        contracts.get.response.head.provinceCode, contracts.get.response.head.accessFareCode, None)
      maxPower <- getMaxPowerV2(datadisAnalyzerParams.datadisParams, token.get, supplies.get.response.head.cups,
        supplies.get.response.head.distributorCode, startDate, endDate)
    } yield DatadisResults(supplies, contracts, consumptionDataHours, maxPower)
  }
}
