package org.ordus.datadisanalizer.domain

import com.typesafe.config.Config
import org.ordus.datadisanalizer.constants.ConfigConstants
import org.ordus.datadisanalizer.error.DatadisAnalyzerError
import org.ordus.datadisanalizer.utils.ConfigUtils

case class ProxyParams(host: String, port: Int)

object ProxyParams extends ConfigUtils {
  def apply(proxyConf: Config): Either[DatadisAnalyzerError, Option[ProxyParams]] = {
    for {
      url <- readOptionString(proxyConf, ConfigConstants.ProxyHostKey)
      port <- readOptionInteger(proxyConf, ConfigConstants.ProxyPortKey)
    } yield if(url.isDefined && port.isDefined) Some(ProxyParams(url.get, port.get)) else None
  }
}
