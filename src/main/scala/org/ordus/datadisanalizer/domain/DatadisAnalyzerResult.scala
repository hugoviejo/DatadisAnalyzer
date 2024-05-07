package org.ordus.datadisanalizer.domain

import org.ordus.datadisanalizer.error.DatadisAnalyzerError

case class DatadisAnalyzerResult(numberOfMeasuresWritten: Int, firstError: Option[DatadisAnalyzerError])
