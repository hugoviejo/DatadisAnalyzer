package org.ordus.datadisanalizer.domain.datadis

/**
 *
 * @param address Address of the supply
 * @param cups CUPS
 * @param postalCode Postal code
 * @param province Province
 * @param municipality Municipality
 * @param distributor Name of the distributor company
 * @param validDateFrom Date from de beginning of the supply contract (yyyy/MM/dd)
 * @param validDateTo Date to the end of the supply contract (yyyy/MM/dd)
 * @param error Error message
 * @param pointType Type of the point of measure (1, 2, 3, 4 or 5)
 * @param distributorCode Code of the distributor company (1: Viesgo, 2: E-distribución, 3: E-redes, 4: ASEME, 5: UFD, 6: EOSA, 7:CIDE, 8: IDE)
 * @param startDate
 * @param endDate
 * @param url URL to the distributor
 * @param hasSelfConsumption True if the supply has self consumption
 */
case class SupplyV2(address: String, cups: String, postalCode: String, province: String, municipality: String, distributor: String, validDateFrom: String,
                    validDateTo: Option[String], error: Option[String], pointType: Int, distributorCode: String, startDate: Option[String],
                    endDate: Option[String], url: Option[String], hasSelfConsumption: Boolean)

/**
 *
 * @param response List of supplies
 * @param codError Error code
 * @param distributorsError Error message
 * @param distributors List of distributors
 */
case class SupplyV2Response(response: List[SupplyV2], codError: String, distributorsError: String, distributors: String)
