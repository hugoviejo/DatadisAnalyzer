package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups                    CUPS
 * @param distributor             Name of the distributor company
 * @param marketer                Name of the marketer company
 * @param tension                 Tension
 * @param accessFare              Access fare
 * @param accessFareCode          Access fare code
 * @param timeDiscrimination      Time discrimination
 * @param province                Province
 * @param provinceCode            Province code
 * @param municipality            Municipality
 * @param postalCode              Postal code
 * @param contractedPowerkW       Contracted power (kW)
 * @param modePowerControl        Mode power control
 * @param modePowerControlName    Mode power control name
 * @param startDate               Start date of contract (yyyy/MM/dd)
 * @param endDate                 End date of contract (yyyy/MM/dd)
 * @param errorCode               Error code
 * @param url                     URL to the distributor
 * @param installedCapacityKW     Installed capacity (kW) of self consumption. Only when self consumption.
 * @param selfConsumptionTypeCode Self consumption type code.  Only when self consumption.
 * @param selfConsumptionTypeDesc Self consumption type description.  Only when self consumption.
 * @param section                 Section. Only when self consumption.
 * @param subsection              Subsection. Only when self consumption.
 * @param partitionCoefficient    Partition coefficient. Only when self consumption.
 * @param cau                     CAU. Only when self consumption.
 */
case class ContractV2(cups: String, distributor: String, marketer: String, tension: String, accessFare: String, accessFareCode: String,
                      timeDiscrimination: String, province: String, provinceCode: String, municipality: String, postalCode: String,
                      contractedPowerkW: List[Double], modePowerControl: Int, modePowerControlName: String, startDate: String, endDate: String, errorCode: Int,
                      url: String, installedCapacityKW: Option[Double], selfConsumptionTypeCode: Option[String], selfConsumptionTypeDesc: Option[String],
                      section: Option[String], subsection: Option[String], partitionCoefficient: Option[Double], cau: Option[String]
                     )

/**
 *
 * @param response List of contracts
 */
case class ContractV2Response(response: List[ContractV2])
