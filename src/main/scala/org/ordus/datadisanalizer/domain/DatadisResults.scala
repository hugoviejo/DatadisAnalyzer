package org.ordus.datadisanalizer.domain

import org.ordus.datadisanalizer.domain.datadis.{ConsumptionV2HourlyResponse, ContractV2Response, MaxPowerV2Response, SupplyV2Response}

case class DatadisResults(supplies: Option[SupplyV2Response], contracts: Option[ContractV2Response], consumptionDataHours: Option[ConsumptionV2HourlyResponse],
                          maxPower: Option[MaxPowerV2Response])
