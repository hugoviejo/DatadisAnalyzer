package org.ordus.datadisanalizer

import org.ordus.datadisanalizer.constants.DatadisAnalyzerConstants
import org.slf4j.{Logger, LoggerFactory}

object Main {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.info(s"${DatadisAnalyzerConstants.LogPrefix}init")
    val analyzer = new DatadisAnalyzer()
    analyzer.launch
    logger.info(s"${DatadisAnalyzerConstants.LogPrefix}end")
    sys.exit(0)
  }
}
