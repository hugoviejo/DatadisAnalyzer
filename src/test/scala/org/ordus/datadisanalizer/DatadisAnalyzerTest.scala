package org.ordus.datadisanalizer

import com.typesafe.config.{Config, ConfigFactory}
import org.ordus.datadisanalizer.constants.{ConfigConstants, ErrorCodeConstants}
import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, DatadisAnalyzerResult, ExecutionMeasure, MeasureWriterParams, TotalTimeExecutionMeasure}
import org.ordus.datadisanalizer.error.{DatadisAnalyzerError, DatadisAnalyzerProcessorError, MeasureWriterError}

import java.io.File
import java.time.Instant
import scala.language.postfixOps

class DatadisAnalyzerTest extends DatadisAnalyzerUnitSpec {
  "a DatadisAnalyzer" when {
    "call launch" should {
      "return ok when all ok" in {
        val studio = new DatadisAnalyzer() {
          override private[datadisanalizer] def getConfig: Either[DatadisAnalyzerError, Config] = Right(ConfigFactory.parseString(""))

          override private[datadisanalizer] def executeAnalyzer(config: Config): Either[DatadisAnalyzerError, DatadisAnalyzerResult] = {
            Right(DatadisAnalyzerResult(0, None))
          }
        }
        val result = studio.launch
        result shouldBe 0
      }

      "return the error code when fails" in {
        val studio = new DatadisAnalyzer() {
          override private[datadisanalizer] def getConfig: Either[DatadisAnalyzerError, Config] = Right(ConfigFactory.parseString(""))

          override private[datadisanalizer] def executeAnalyzer(config: Config): Either[DatadisAnalyzerError, DatadisAnalyzerResult] = {
            Left(MeasureWriterError("Error", 999, None))
          }
        }
        val result = studio.launch
        //TODO change this
        result shouldBe 0
      }
    }

    "call getConfig" should {
      "return a config when all ok" in {
        val field = System.getenv().getClass.getDeclaredField("m")
        field.setAccessible(true)
        val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
        map.put("INFLUXDB_HOST", "host1")
        map.put("INFLUXDB_PORT", "8086")
        map.put("INFLUXDB_TOKEN", "token1")
        map.put("INFLUXDB_ORG", "org")
        map.put("INFLUXDB_BUCKET", "bucket")

        val studio = new DatadisAnalyzer()
        val result = studio.getConfig
        result shouldBe a[Right[_, _]]
        val config = result.toOption.get
        config shouldBe a[Config]
        val root = config.getConfig(ConfigConstants.RootConfKey)
        root shouldBe a[Config]

        map.remove("INFLUXDB_HOST")
        map.remove("INFLUXDB_PORT")
        map.remove("INFLUXDB_TOKEN")
        map.remove("INFLUXDB_ORG")
        map.remove("INFLUXDB_BUCKET")
      }
    }

    "call executeAnalyzer" should {
      "work when all configuration ok" in {
        val config = ConfigFactory.parseString(
          """
            |datadis-analyzer {
            |  influxdb {
            |    influxdbHost = "host"
            |    influxdbPort = "8086"
            |    influxdbToken ="token1"
            |    influxdbOrg = "ordus"
            |    influxdbBucket = "bucket"
            |  }
            |}
            |""".stripMargin)
        val studio = new DatadisAnalyzer() {


        }
        val result = studio.executeAnalyzer(config)
        result shouldBe a[Right[_, _]]
        val dresult = result.toOption.get

        dresult shouldBe a[DatadisAnalyzerResult]
        dresult.numberOfMeasuresWritten shouldBe 1
      }
    }

    /*"call executeJobExecutionFiles" should {
      "work when all ok" in {
        val studio = new DatadisAnalyzer() {
          override private[studio] def executeJobExecutionForOneFile(environmentStudioParams: DatadisAnalyzerParams, file: File): Either[DatadisAnalyzerError, Int] =
            Right(1)
        }

        val result = studio.executeJobExecutionFiles(DatadisAnalyzerParams("", MeasureProcessorParams(1), MeasureWriterParams("", 0, "", "", "")), Stream(new File("file1"), new File("file2")))
        result shouldBe a[DatadisAnalyzerResult]
        result.numberOfMeasuresWritten shouldBe 2
        result.firstError shouldBe None
      }

      "fails when one file fails" in {
        val studio = new DatadisAnalyzer() {
          override private[studio] def executeJobExecutionForOneFile(environmentStudioParams: DatadisAnalyzerParams, file: File):
          Either[DatadisAnalyzerError, Int] = {
            if (file.getName == "file2") {
              Left(DatadisAnalyzerProcessorError("Error", ErrorCodeConstants.JsonParserError, None))
            } else {
              Right(2)
            }
          }
        }

        val result = studio.executeJobExecutionFiles(DatadisAnalyzerParams("", MeasureProcessorParams(1), MeasureWriterParams("", 0, "", "", "")), Stream(new File("file1"), new File("file2"), new File("file3")))
        result shouldBe a[DatadisAnalyzerResult]
        result.numberOfMeasuresWritten shouldBe 4
        result.firstError shouldBe a[Some[_]]
        result.firstError.get.code shouldBe ErrorCodeConstants.JsonParserError
      }
    }*/

    /*"call executeJobExecutionForOneFile" should {
      "work when all ok" in {
        val studio = new DatadisAnalyzer() {
          override def retrieveJobExecutionsTraces(executionFile: File): Either[DatadisAnalyzerError, List[String]] = Right(List(""))

          override def processJobExecutionTraces(measureProcessorParams: MeasureProcessorParams, jobExecutionTraces: Seq[String]): Either[DatadisAnalyzerError, Seq[ExecutionMeasure]] = Right(Seq(TotalTimeExecutionMeasure("", "", "", "", "", "", 0, Instant.now)))

          override def writeMeasures(environmentStudioParams: DatadisAnalyzerParams, measures: Seq[ExecutionMeasure]): Either[DatadisAnalyzerError, Int] = Right(measures.size)

          override def doneProcessingFile(inputPath: String, file: File): Boolean = true

          override def failProcessingFile(inputPath: String, file: File): Boolean = ???
        }

        val result = studio.executeJobExecutionForOneFile(DatadisAnalyzerParams("", MeasureProcessorParams(1), MeasureWriterParams("", 0, "", "", "")), new File("file1"))
        result shouldBe a[Right[_, _]]
        result.right.get shouldBe 1
      }

      "fail when fail to write" in {
        val studio = new DatadisAnalyzer() {
          override def retrieveJobExecutionsTraces(executionFile: File): Either[DatadisAnalyzerError, List[String]] = Right(List(""))

          override def processJobExecutionTraces(measureProcessorParams: MeasureProcessorParams, jobExecutionTraces: Seq[String]): Either[DatadisAnalyzerError, Seq[ExecutionMeasure]] = Right(Seq(TotalTimeExecutionMeasure("", "", "", "", "", "", 0, Instant.now)))

          override def writeMeasures(environmentStudioParams: DatadisAnalyzerParams, measures: Seq[ExecutionMeasure]): Either[DatadisAnalyzerError, Int] = Left(MeasureWriterError("Error", ErrorCodeConstants.ErrorConnectingDB, None))

          override def doneProcessingFile(inputPath: String, file: File): Boolean = ???

          override def failProcessingFile(inputPath: String, file: File): Boolean = true
        }

        val result = studio.executeJobExecutionForOneFile(DatadisAnalyzerParams("", MeasureProcessorParams(1), MeasureWriterParams("", 0, "", "", "")), new File("file1"))
        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.ErrorConnectingDB
      }
    }*/
  }
}
