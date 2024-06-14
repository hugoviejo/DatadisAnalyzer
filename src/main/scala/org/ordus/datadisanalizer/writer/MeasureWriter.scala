package org.ordus.datadisanalizer.writer

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.WriteParameters
import com.influxdb.client.{InfluxDBClientFactory, InfluxDBClientOptions, WriteApiBlocking}
import org.ordus.datadisanalizer.constants.{DatadisAnalyzerConstants, ErrorCodeConstants}
import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, ExecutionMeasure, MeasureWriterParams}
import org.ordus.datadisanalizer.error.{DatadisAnalyzerError, MeasureWriterError}
import org.slf4j.{Logger, LoggerFactory}

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

trait MeasureWriter {
  private val logger: Logger = LoggerFactory.getLogger(classOf[MeasureWriter])

  /**
   * Write a lis of ExecutionMeasure into influxdb
   *
   * @param environmentStudioParams Configuration parameters
   * @param measures                The list of measures to write
   * @return the number of measures written
   */
  def writeMeasures(environmentStudioParams: DatadisAnalyzerParams, measures: Seq[ExecutionMeasure]): Either[DatadisAnalyzerError, Int] = {
    logger.info(s"Writing ${measures.size} measures")
    //logger.info(s"Writing ${measures.size} measures: $measures")
    getInfluxApi(environmentStudioParams.measureWriterParams) match {
      case Right(writerApi) =>
        Try {
          writerApi.writeMeasurements(measures.toList.asJava,
            new WriteParameters(environmentStudioParams.measureWriterParams.influxdbBucket,
              environmentStudioParams.measureWriterParams.influxdbOrg, WritePrecision.MS))
        } match {
          case Success(_) => Right(measures.size)
          case Failure(exception) => Left(MeasureWriterError(s"Error writing measures: $measures - Error: ${exception.getMessage}",
            ErrorCodeConstants.ErrorWritingMeasures, Some(exception)))
        }
      case Left(error) => Left(error)
    }

  }

  /**
   * get the Influxdb client to operate
   *
   * @param measureWriterParams Configuration
   * @return the client
   */
  private[writer] def getInfluxApi(measureWriterParams: MeasureWriterParams): Either[DatadisAnalyzerError, WriteApiBlocking] = {
    val connectionString = s"http://${measureWriterParams.influxdbHost}:${measureWriterParams.influxdbPort}"
    logger.info(s"InfluxDB connection string:||$connectionString")
    val dbOptions: InfluxDBClientOptions = InfluxDBClientOptions.builder().connectionString(connectionString).org(measureWriterParams.influxdbOrg)
      .bucket(measureWriterParams.influxdbBucket).authenticateToken(measureWriterParams.influxdbToken.toCharArray).build
    val influxDBClient = InfluxDBClientFactory.create(dbOptions)

    Option(influxDBClient.ready()) match {
      case Some(_) => Right(influxDBClient.getWriteApiBlocking)
      case None => Left(MeasureWriterError("Error connecting to influxdb", ErrorCodeConstants.ErrorConnectingDB, None))
    }
  }
}
