package org.ordus.datadisanalizer.domain

import com.typesafe.config.Config
import org.ordus.datadisanalizer.constants.ConfigConstants
import org.ordus.datadisanalizer.domain.DatadisAnalyzerParams.readConfig
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.utils.ConfigUtils

case class DatadisParams(url: String, user: String, password: String, proxyParams: Option[ProxyParams] = None)

object DatadisParams extends ConfigUtils {
  def apply(datadisConfig: Config): Either[DatadisAnalyzerError, DatadisParams] = {
    for {
      url <- readString(datadisConfig, ConfigConstants.DatadisUrlKey)
      user <- readString(datadisConfig, ConfigConstants.DatadisUserKey)
      password <- readString(datadisConfig, ConfigConstants.DatadisPasswordKey)
      proxyConfig <- readConfig(datadisConfig, ConfigConstants.ProxyConfKey)
      proxyParams <- ProxyParams(proxyConfig)
    } yield DatadisParams(url, user, password, proxyParams)
  }
}
