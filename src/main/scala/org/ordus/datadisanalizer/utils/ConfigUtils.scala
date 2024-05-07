package org.ordus.datadisanalizer.utils

import com.typesafe.config.Config
import org.ordus.datadisanalizer.constants.ErrorCodeConstants
import org.ordus.datadisanalizer.error.{ConfigReadError, DatadisAnalyzerError}

import scala.util.{Failure, Success, Try}

trait ConfigUtils {

  /**
   * read a configuration key that contains a typesafe.Config.
   *
   * @param config  configuration
   * @param confKey configuration key
   * @return the value or an error
   */
  def readConfig(config: Config, confKey: String): Either[ConfigReadError, Config] = {
    Try {
      config.getConfig(confKey)
    } match {
      case Success(value) => Right(value)
      case Failure(ex) =>
        Left(ConfigReadError(s"Fail when reading $confKey configuration key. Message => ${ex.getMessage}",
          ErrorCodeConstants.ConfigReadError, Some(ex)))
    }
  }

  /**
   * read a configuration key that contains a String.
   *
   * @param config  configuration
   * @param confKey configuration key
   * @return the value or an error
   */
  def readString(config: Config, confKey: String): Either[DatadisAnalyzerError, String] = {
    Try {
      config.getString(confKey)
    } match {
      case Success(value) => Right(value)
      case Failure(ex) =>
        Left(ConfigReadError(s"Fail when reading $confKey configuration key. Message => ${ex.getMessage}",
          ErrorCodeConstants.ConfigReadError, Some(ex)))
    }
  }

  /**
   * read a configuration key that contains a String as option if set or none.
   *
   * @param config  configuration
   * @param confKey configuration key
   * @return the Some(value) or None
   */
  def readOptionString(config: Config, confKey: String): Either[DatadisAnalyzerError, Option[String]] = {
    Try {
      config.getString(confKey)
    } match {
      case Success(value) => Right(Some(value))
      case Failure(_) => Right(None)
    }
  }

  /**
   * read a configuration key that contains a Integer.
   *
   * @param config  configuration
   * @param confKey configuration key
   * @return the value or an error
   */
  def readInteger(config: Config, confKey: String): Either[ConfigReadError, Int] = {
    Try {
      config.getInt(confKey)
    } match {
      case Success(value) => Right(value)
      case Failure(ex) =>
        Left(ConfigReadError(s"Fail when reading $confKey configuration key. Message => ${ex.getMessage}",
          ErrorCodeConstants.ConfigReadError, Some(ex)))
    }
  }

  /**
   * read a configuration key that contains a Integer as option if set or none.
   *
   * @param config  configuration
   * @param confKey configuration key
   * @return the Some(value) or None
   */
  def readOptionInteger(config: Config, confKey: String): Either[ConfigReadError, Option[Int]] = {
    Try {
      config.getInt(confKey)
    } match {
      case Success(value) => Right(Some(value))
      case Failure(_) => Right(None)
    }
  }
}
