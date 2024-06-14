package org.ordus.datadisanalizer

import com.typesafe.config.{Config, ConfigFactory}
import org.ordus.datadisanalizer.constants.{DatadisAnalyzerConstants, ErrorCodeConstants}
import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, DatadisAnalyzerResult}
import org.ordus.datadisanalizer.error.{ConfigReadError, DatadisAnalyzerError}
import org.ordus.datadisanalizer.service.{DataRetrieverService, DataStoreService}
import org.slf4j.{Logger, LoggerFactory}

import java.util.Date
import scala.util.{Failure, Success, Try}

class DatadisAnalyzer extends DataRetrieverService with DataStoreService {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private val fullDateFormatter = new java.text.SimpleDateFormat("yyyy/MM/dd")

  /**
   * Launch the studio
   *
   * @return the return code
   */
  def launch: Int = {
    logger.info("Launch Datadis Analyzer")
    val result = getConfig match {
      case Right(config) => executeAnalyzer(config)
      case Left(error) => Left(error)
    }

    logger.info(s"Datadis Analyzer result:||$result")
    //TODO Handler result
    0
  }

  /**
   * get the configuration file
   *
   * @return the configuration
   */
  private[datadisanalizer] def getConfig: Either[DatadisAnalyzerError, Config] = {
    Try {
      ConfigFactory.parseResources(DatadisAnalyzerConstants.ReferenceConfFileName).resolve()
    } match {
      case Success(config) =>
        logger.info(s"config:||$config")
        Right(config)
      case Failure(exception) => Left(ConfigReadError(s"Error reading config file: ${exception.getMessage}", ErrorCodeConstants.ConfigFileError,
        Some(exception)))
    }
  }

  /**
   * Execute the analyzer
   *
   * @param config the configuration
   * @return the result
   */
  private[datadisanalizer] def executeAnalyzer(config: Config): Either[DatadisAnalyzerError, DatadisAnalyzerResult] = {
    for {
      datadisAnalyzertParams <- DatadisAnalyzerParams(config)
      startDate <- getStartDate(datadisAnalyzertParams)
      endDate <- getEndDate(datadisAnalyzertParams)
      datadisData <- retrieveData(datadisAnalyzertParams, startDate, endDate)
      result <- storeData(datadisAnalyzertParams, datadisData)
    } yield result
  }

  private def getEndDate(datadisAnalyzertParams: DatadisAnalyzerParams): Either[DatadisAnalyzerError, String] = {
    Right(fullDateFormatter.format(new Date()))
  }

  private def getStartDate(datadisAnalyzertParams: DatadisAnalyzerParams): Either[DatadisAnalyzerError, String] = {
    Right("2024/01/01")
  }

  /**
   *
   * @param environmentStudioParams params
   * @param file                    The file to `recess`
   * @return number of Measures written
   */
  /*private[studio] def executeJobExecutionForOneFile(environmentStudioParams: DatadisAnalyzerParams, file: File): Either[DatadisAnalyzerError, Int] = {
    logger.info(s"${DatadisAnalyzerConstants.LogPrefix}Executing for file: ${file.getAbsolutePath}")
    val result = for {
      jobExecutionTraces <- retrieveJobExecutionsTraces(file).right
      executionMeasurements <- processJobExecutionTraces(environmentStudioParams.measureProcessorParams, jobExecutionTraces).right
      writeResult <- writeMeasures(environmentStudioParams, executionMeasurements).right
    } yield writeResult

    if (result.isRight) {
      doneProcessingFile(environmentStudioParams.inputPath, file)
    } else {
      failProcessingFile(environmentStudioParams.inputPath, file)
    }
    result
  }*/
}
