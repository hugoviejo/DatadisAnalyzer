package org.ordus.datadisanalizer.domain

import com.influxdb.annotations.{Column, Measurement}

import java.time.Instant

trait ExecutionMeasure

@Measurement(name = "TotalTimeExecutionMeasure")
class TotalTimeExecutionMeasure(@Column(tag = true) size: String,
                                @Column(tag = true) typeJob: String,
                                @Column(tag = true) namespace: String,
                                @Column(tag = true) jobId: String,
                                @Column(tag = true) finalStatus: String,
                                @Column(tag = true) state: String,
                                @Column timeTakeMilliseconds: Long,
                                @Column(timestamp = true) createdAt: Instant) extends ExecutionMeasure {

  def getSize: String = size

  def getTypeJob: String = typeJob

  def getNamespace: String = namespace

  def getJobId: String = jobId

  def getFinalStatus: String = finalStatus

  def getState: String = state

  def getTimeTakeMilliseconds: Long = timeTakeMilliseconds

  def getCreatedAt: Instant = createdAt

  override def toString: String = s"TotalTimeExecutionMeasure($size, $typeJob, $namespace, $jobId, $finalStatus, $state, $timeTakeMilliseconds," +
    s" $createdAt)"

  def canEqual(other: Any): Boolean = other.isInstanceOf[TotalTimeExecutionMeasure]

  override def equals(other: Any): Boolean = other match {
    case that: TotalTimeExecutionMeasure =>
      (that canEqual this) &&
        size == that.getSize &&
        typeJob == that.getTypeJob &&
        namespace == that.getNamespace &&
        jobId == that.getJobId &&
        finalStatus == that.getFinalStatus &&
        state == that.getState &&
        timeTakeMilliseconds == that.getTimeTakeMilliseconds &&
        createdAt == that.getCreatedAt
    case _ => false
  }

  override def hashCode(): Int = {
    val currentState = Seq(size, typeJob, namespace, jobId, finalStatus, state, timeTakeMilliseconds, createdAt)
    currentState.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object TotalTimeExecutionMeasure {
  def apply(size: String, typeJob: String, namespace: String, jobId: String, finalStatus: String, state: String,
            timeTake: Long, createdAt: Instant): TotalTimeExecutionMeasure =
    new TotalTimeExecutionMeasure(size, typeJob, namespace, jobId, finalStatus, state, timeTake, createdAt)

  /*def fromExecutionInfo(executionInfo: ExecutionInfo, state: String, timeTake: Long, createdAt: Instant):
  TotalTimeExecutionMeasure = {
    TotalTimeExecutionMeasure(executionInfo.size, executionInfo.typeJob, executionInfo.namespace, executionInfo.jobId, executionInfo.finalStatus, state,
      timeTake, createdAt)
  }*/
}

@Measurement(name = "StateExecutionMeasure")
class StateExecutionMeasure(@Column(tag = true) size: String,
                            @Column(tag = true) typeJob: String,
                            @Column(tag = true) namespace: String,
                            @Column(tag = true) jobId: String,
                            @Column(tag = true) finalStatus: String,
                            @Column(tag = true) state: String,
                            @Column timeTakeMilliseconds: Long,
                            @Column(timestamp = true) createdAt: Instant) extends ExecutionMeasure {

  def getSize: String = size

  def getTypeJob: String = typeJob

  def getNamespace: String = namespace

  def getJobId: String = jobId

  def getFinalStatus: String = finalStatus

  def getState: String = state

  def getTimeTakeMilliseconds: Long = timeTakeMilliseconds

  def getCreatedAt: Instant = createdAt

  override def toString: String = s"StateExecutionMeasure($size, $typeJob, $namespace, $jobId, $finalStatus, $state, $timeTakeMilliseconds, $createdAt)"

  def canEqual(other: Any): Boolean = other.isInstanceOf[StateExecutionMeasure]

  override def equals(other: Any): Boolean = other match {
    case that: StateExecutionMeasure =>
      (that canEqual this) &&
        size == that.getSize &&
        typeJob == that.getTypeJob &&
        namespace == that.getNamespace &&
        jobId == that.getJobId &&
        finalStatus == that.getFinalStatus &&
        state == that.getState &&
        timeTakeMilliseconds == that.getTimeTakeMilliseconds &&
        createdAt == that.getCreatedAt
    case _ => false
  }

  override def hashCode(): Int = {
    val currentState = Seq(size, typeJob, namespace, jobId, finalStatus, state, timeTakeMilliseconds, createdAt)
    currentState.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object StateExecutionMeasure {
  def apply(size: String, typeJob: String, namespace: String, jobId: String, finalStatus: String, state: String,
            timeTakeMilliseconds: Long, createdAt: Instant): StateExecutionMeasure =
    new StateExecutionMeasure(size, typeJob, namespace, jobId, finalStatus, state, timeTakeMilliseconds, createdAt)

  def fromTotalTimeExecutionMeasure(executionMeasure: TotalTimeExecutionMeasure, value: Long, createdAt: Instant): StateExecutionMeasure = {
    StateExecutionMeasure(executionMeasure.getSize, executionMeasure.getTypeJob, executionMeasure.getNamespace, executionMeasure.getJobId,
      executionMeasure.getFinalStatus, executionMeasure.getState, value, createdAt)
  }
}


