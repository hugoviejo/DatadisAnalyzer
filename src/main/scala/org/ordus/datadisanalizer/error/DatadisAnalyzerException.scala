package org.ordus.datadisanalizer.error

case class DatadisAnalyzerException(msg: String, error: DatadisAnalyzerError, cause: Option[Throwable]) extends Exception(msg: String, cause.orNull)