package org.ordus.datadisanalizer.domain

import com.typesafe.config.Config
import org.ordus.datadisanalizer.constants.ConfigConstants
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.utils.ConfigUtils

case class DatadisAnalyzerParams(datadisParams: DatadisParams, measureWriterParams: MeasureWriterParams)


object DatadisAnalyzerParams extends ConfigUtils {
  def apply(environmentStudioConfig: Config): Either[DatadisAnalyzerError, DatadisAnalyzerParams] = {
    for {
      rootConfig <- readConfig(environmentStudioConfig, ConfigConstants.RootConfKey)
      datadisConfig <- readConfig(rootConfig, ConfigConstants.DatadisConfKey)
      datadisParams <- DatadisParams(datadisConfig)
      measureWriterConfig <- readConfig(rootConfig, ConfigConstants.MeasureWriterConfKey)
      measureWriterParams <- MeasureWriterParams(measureWriterConfig)
    } yield DatadisAnalyzerParams(datadisParams, measureWriterParams)
  }
}