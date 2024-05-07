package org.ordus.datadisanalizer.writer

import com.influxdb.client.WriteApiBlocking
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.WriteParameters
import com.influxdb.exceptions.InfluxException
import org.ordus.datadisanalizer.constants.ErrorCodeConstants
import org.ordus.datadisanalizer.domain.{DatadisAnalyzerParams, DatadisParams, MeasureWriterParams, TotalTimeExecutionMeasure}
import org.ordus.datadisanalizer.error.{DatadisAnalyzerError, MeasureWriterError}
import org.ordus.datadisanalizer.{DatadisAnalyzerUnitSpec, HttpServer}
import org.scalatestplus.easymock.EasyMockSugar

import java.time.Instant
import scala.jdk.CollectionConverters._
import scala.language.postfixOps

class MeasureWriterTest extends DatadisAnalyzerUnitSpec with EasyMockSugar with HttpServer {
  "a MeasureWriter" when {
    "call writeMeasures" should {
      "work when all ok" in {
        val datadisAnalyzerParams = DatadisAnalyzerParams(DatadisParams("", "", ""), MeasureWriterParams("host", 8086, "user", "token1", "bucket"))
        val measures = List(TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "PENDING", 1000L, Instant.parse("2022-04-28T16:44:25.943505Z")),
          TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "RUNNING", 3000L, Instant.parse("2022-04-28T16:44:25.943505Z")))

        val influxApiMock = mock[WriteApiBlocking]
        val writer = new MeasureWriter() {
          override private[writer] def getInfluxApi(measureWriterParams: MeasureWriterParams): Either[DatadisAnalyzerError, WriteApiBlocking] = Right(influxApiMock)
        }
        val writerParameters = new WriteParameters(datadisAnalyzerParams.measureWriterParams.influxdbBucket,
          datadisAnalyzerParams.measureWriterParams.influxdbOrg, WritePrecision.MS)

        expecting {
          influxApiMock.writeMeasurements(measures.asJava, writerParameters)
        }

        whenExecuting(influxApiMock) {
          val result = writer.writeMeasures(datadisAnalyzerParams, measures)
          result shouldBe a[Right[_, _]]
          result.toOption.get shouldBe 2
        }
      }

      "fail when fail to create a client" in {
        val datadisAnalyzerParams = DatadisAnalyzerParams(DatadisParams("", "", ""), MeasureWriterParams("host", 8086, "user", "token1", "bucket"))
        val measures = List(TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "PENDING", 1000L, Instant.parse("2022-04-28T16:44:25.943505Z")),
          TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "RUNNING", 3000L, Instant.parse("2022-04-28T16:44:25.943505Z")))

        val writer = new MeasureWriter() {
          override private[writer] def getInfluxApi(measureWriterParams: MeasureWriterParams): Either[DatadisAnalyzerError, WriteApiBlocking] =
            Left(MeasureWriterError("Error", ErrorCodeConstants.ErrorConnectingDB, None))
        }
        val result = writer.writeMeasures(datadisAnalyzerParams, measures)
        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.ErrorConnectingDB
      }

      "fail when fail to write measures" in {
        val datadisAnalyzerParams = DatadisAnalyzerParams(DatadisParams("", "", ""), MeasureWriterParams("host", 8086, "user", "token1", "bucket"))
        val measures = List(TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "PENDING", 1000L, Instant.parse("2022-04-28T16:44:25.943505Z")),
          TotalTimeExecutionMeasure("S", "type", "ns", "id1", "SUCCESS", "RUNNING", 3000L, Instant.parse("2022-04-28T16:44:25.943505Z")))

        val influxApiMock = mock[WriteApiBlocking]
        val writer = new MeasureWriter() {
          override private[writer] def getInfluxApi(measureWriterParams: MeasureWriterParams): Either[DatadisAnalyzerError, WriteApiBlocking] = Right(influxApiMock)
        }
        val writerParameters = new WriteParameters(datadisAnalyzerParams.measureWriterParams.influxdbBucket,
          datadisAnalyzerParams.measureWriterParams.influxdbOrg, WritePrecision.MS)

        expecting {
          influxApiMock.writeMeasurements(measures.asJava, writerParameters).andThrow(new InfluxException("Error"))
        }

        whenExecuting(influxApiMock) {
          val result = writer.writeMeasures(datadisAnalyzerParams, measures)
          result shouldBe a[Left[_, _]]
          result.left.get.code shouldBe ErrorCodeConstants.ErrorWritingMeasures
        }
      }
    }

    "call getInfluxApi" should {
      "return a client when all ok" in {
        val httpServer = getHttpServer(8087)
        loadTextWireMock(httpServer, "/ready",
          """{
            |    "status": "ready",
            |    "started": "2022-04-28T10:14:59.931793497Z",
            |    "up": "7m30.806638229s"
            |}""".stripMargin)
        val measureWriterParams = MeasureWriterParams("localhost", 8087, "token1", "org", "bucket")
        val writer = new MeasureWriter() {}
        val result = writer.getInfluxApi(measureWriterParams)
        stopHttpServer(httpServer)
        result shouldBe a[Right[_, _]]
        result.toOption.get shouldBe a[WriteApiBlocking]
      }

      "fail when no connection to db" in {
        val measureWriterParams = MeasureWriterParams("host", 8086, "token1", "org", "bucket")
        val writer = new MeasureWriter() {}
        val result = writer.getInfluxApi(measureWriterParams)
        result shouldBe a[Left[_, _]]
        result.left.get.code shouldBe ErrorCodeConstants.ErrorConnectingDB
      }
    }
  }
}
