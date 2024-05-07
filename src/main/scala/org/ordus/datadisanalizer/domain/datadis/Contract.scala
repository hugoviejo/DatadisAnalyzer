package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param cups                    CUPS
 * @param distributor             distributor company
 * @param marketer                marketer company
 * @param tension                 tension
 * @param accesFare               access fare description
 * @param province                province
 * @param municipality            municipality
 * @param postalCode              postal code
 * @param contractedPowerkW       contracted power in kW
 * @param timeDiscrimination      time discrimination
 * @param modePowerControl        mode power control (ICP/Maxímetro)
 * @param startDate               start date of the contract
 * @param endDate                 end date of the contract
 * @param codeFare                code of the fare (Codes from CNMC)
 * @param selfConsumptionTypeCode self consumption type code (Codes from CNMC). Only when self consumption.
 * @param selfConsumptionTypeDesc self consumption type description. Only when self consumption.
 * @param section                 section. Only when self consumption.
 * @param subsection              subsection. Only when self consumption.
 * @param partitionCoefficient    partition coefficient. Only when self consumption.
 * @param cau                     CAU. Only when self consumption.
 * @param installedCapacityKW     installed capacity in kW. Only when self consumption.
 */
case class Contract(cups: String, distributor: String, marketer: String, tension: String, accesFare: String, province: String, municipality: String,
                    postalCode: String, contractedPowerkW: List[Double], timeDiscrimination: String, modePowerControl: String, startDate: String,
                    endDate: String, codeFare: String, selfConsumptionTypeCode: Option[String], selfConsumptionTypeDesc: Option[String],
                    section: Option[String], subsection: Option[String], partitionCoefficient: Option[Double], cau: Option[String],
                    installedCapacityKW: Option[Double])
