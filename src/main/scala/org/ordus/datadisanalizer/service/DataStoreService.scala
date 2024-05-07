package org.ordus.datadisanalizer.service

import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, DatadisAnalyzerResult, DatadisResults}
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.slf4j.{Logger, LoggerFactory}

trait DataStoreService {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def storeData(datadisAnalyzerParams: DatadisAnalyzerParams, datadisResults: DatadisResults): Either[DatadisAnalyzerError, DatadisAnalyzerResult] = {
    logger.info(s"Results: $datadisResults")

    Right(DatadisAnalyzerResult(0, None))
  }
}
