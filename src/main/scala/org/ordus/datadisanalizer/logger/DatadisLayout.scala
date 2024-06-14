package org.ordus.datadisanalizer.logger

import org.apache.log4j.PatternLayout
import org.apache.log4j.spi.LoggingEvent
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.config.Node
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.ordus.datadisanalizer.constants.DatadisAnalyzerConstants

@Plugin(name = "DatadisLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE)
class DatadisLayout extends PatternLayout {

  override def format(event: LoggingEvent): String = {

    val message: String = s"${event.getMessage}"
    val strBld: StringBuilder = new StringBuilder

    strBld.append(DatadisAnalyzerConstants.LogPrefix)
    strBld.append("\n")

    message.split("\\|\\|").foreach(line => {
      strBld.append("     ┌───────────────────────────────────────" +
        "─────────────────────────────\n")
      line.split("\\n").foreach(line => strBld.append(s"     │ $line \n"))
      strBld.append("     └───────────────────────────────────────" +
        "─────────────────────────────\n")
    })
    val finalEvent = new LoggingEvent(event.fqnOfCategoryClass, event.getLogger, event.timeStamp, event.getLevel, strBld.mkString, event.getThreadName,
      event.getThrowableInformation, event.getNDC, event.getLocationInformation, event.getProperties)
    super.format(finalEvent)
  }
}
