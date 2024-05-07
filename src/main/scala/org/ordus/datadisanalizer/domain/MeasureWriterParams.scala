package org.ordus.datadisanalizer.domain

import com.typesafe.config.Config
import org.ordus.datadisanalizer.constants.ConfigConstants
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.utils.ConfigUtils

case class MeasureWriterParams(influxdbHost: String, influxdbPort: Int, influxdbToken: String,
                               influxdbOrg: String, influxdbBucket: String)

object MeasureWriterParams extends ConfigUtils {
  def apply(measureWriterConfig: Config): Either[DatadisAnalyzerError, MeasureWriterParams] = {
    for {
      influxdbHost <- readString(measureWriterConfig, ConfigConstants.MeasureWriterInfluxDBHostKey)
      influxdbPort <- readInteger(measureWriterConfig, ConfigConstants.MeasureWriterInfluxDBPortKey)
      influxdbToken <- readString(measureWriterConfig, ConfigConstants.MeasureWriterInfluxDBTokenKey)
      influxdbOrg <- readString(measureWriterConfig, ConfigConstants.MeasureWriterInfluxDBOrgKey)
      influxdbBucket <- readString(measureWriterConfig, ConfigConstants.MeasureWriterInfluxDBBucketKey)
    } yield MeasureWriterParams(influxdbHost, influxdbPort, influxdbToken, influxdbOrg, influxdbBucket)
  }
}