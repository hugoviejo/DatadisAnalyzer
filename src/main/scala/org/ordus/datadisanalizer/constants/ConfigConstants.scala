package org.ordus.datadisanalizer.constants

object ConfigConstants {
  val RootConfKey: String = "datadis-analyzer"
  val DatadisConfKey: String = "datadis"
  val ProxyConfKey: String = "proxy"
  val MeasureWriterConfKey: String = "influxdb"

  val DatadisUrlKey: String = "url"
  val DatadisUserKey: String = "username"
  val DatadisPasswordKey: String = "password"

  val ProxyHostKey: String = "host"
  val ProxyPortKey: String = "port"

  val MeasureWriterInfluxDBHostKey: String = "influxdbHost"
  val MeasureWriterInfluxDBPortKey: String = "influxdbPort"
  val MeasureWriterInfluxDBTokenKey: String = "influxdbToken"
  val MeasureWriterInfluxDBOrgKey: String = "influxdbOrg"
  val MeasureWriterInfluxDBBucketKey: String = "influxdbBucket"
}
