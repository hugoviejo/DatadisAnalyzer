package org.ordus.datadisanalizer.error

import org.ordus.datadisanalizer.constants.DatadisAnalyzerConstants
import org.apache.commons.lang3.exception.ExceptionUtils

sealed trait DatadisAnalyzerError {
  val message: String
  val code: Int
  val cause: Option[Throwable]

  override def toString: String = s"${DatadisAnalyzerConstants.LogPrefix}Error with code: $code. Error Message: $message." +
    s" Cause: ${cause.map(ExceptionUtils.getStackTrace).getOrElse("None")}"

  def toException: DatadisAnalyzerException = DatadisAnalyzerException(message, this, cause)
}

case class DatadisAnalyzerProcessorError(message: String, code: Int, cause: Option[Throwable]) extends DatadisAnalyzerError

case class ConfigReadError(message: String, code: Int, cause: Option[Throwable]) extends DatadisAnalyzerError

case class HttpError(message: String, code: Int, cause: Option[Throwable]) extends DatadisAnalyzerError

case class MeasureWriterError(message: String, code: Int, cause: Option[Throwable]) extends DatadisAnalyzerError
