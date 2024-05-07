package org.ordus.datadisanalizer.utils

import com.typesafe.config.{Config, ConfigFactory}
import org.ordus.datadisanalizer.DatadisAnalyzerUnitSpec
import org.ordus.datadisanalizer.constants.ErrorCodeConstants

class ConfigUtilsTest extends DatadisAnalyzerUnitSpec with ConfigUtils {
  val config: Config = ConfigFactory.parseString(
    """
      |keyString : "value"
      |keyConfig : {
      | things: 2
      |}
      |keyStringList: ["string1","string2", "string3"]
      |keyConfigList : [
      |  {things: 1},
      |  {things: 2}
      |]
      |keyConfigObject : {
      |     element_1: "a"
      |     element_2: "b"
      |  }
      |keyBoolean = true
      |keyInteger = 16
      |""".stripMargin)

  "A ConfigUtils" when {

    "readConfig" should {
      "with exist key return the string value" in {
        val result = readConfig(config, "keyConfig")

        assert(result.isRight && result.right.get.getInt("things") == 2, "The result should be a config with things = 2 inside")
      }

      "with no exist key return an error" in {
        val result = readConfig(config, "non_exist")

        assert(result.isLeft && result.left.get.code == ErrorCodeConstants.ConfigReadError,
          s"The result should be an error with code: ${ErrorCodeConstants.ConfigReadError}")
      }
    }

    "readString" should {
      "with exist key return the string value" in {
        val result = readString(config, "keyString")

        assert(result.isRight && result.right.get == "value", "The result should be 'value'")
      }

      "with no exist key return an error" in {
        val result = readString(config, "non_exist")

        assert(result.isLeft && result.left.get.code == ErrorCodeConstants.ConfigReadError,
          s"The result should be an error with code: ${ErrorCodeConstants.ConfigReadError}")
      }
    }

    "readOptionString" should {
      "return the Some(string) value with exist key" in {
        val result = readOptionString(config, "keyString")

        assert(result.isRight && result.right.get.isDefined && result.right.get.get == "value", "The result should be Some('value')")
      }

      "return a None with no exist key" in {
        val result = readOptionString(config, "non_exist")

        assert(result.isRight && result.right.get.isEmpty, s"The result should be a None")
      }
    }

    "readInteger" should {
      "with exist key return the Integer value" in {
        val result = readInteger(config, "keyInteger")

        assert(result.isRight && result.right.get == 16, "The result should be 'value'")
      }

      "with no exist key return an error" in {
        val result = readInteger(config, "non_exist")

        assert(result.isLeft && result.left.get.code == ErrorCodeConstants.ConfigReadError,
          s"The result should be an error with code: ${ErrorCodeConstants.ConfigReadError}")
      }
    }

    "readOptionInteger" should {
      "with exist key return the Some(Int) value" in {
        val result = readOptionInteger(config, "keyInteger")

        assert(result.isRight && result.right.get.isDefined && result.right.get.get == 16, "The result should be Some('value')")
      }

      "with no exist key return a None" in {
        val result = readOptionInteger(config, "non_exist")

        assert(result.isRight && result.right.get.isEmpty, s"The result should be a None")
      }
    }
  }
}
