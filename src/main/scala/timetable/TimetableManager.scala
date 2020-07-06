package timetable

import java.time.LocalTime
import java.time.temporal.ChronoUnit

import delays.DelayManager

import scala.io.Source

class InMemoryTimetableManager(delayManager: DelayManager, lines: Seq[Line], stops: Seq[Stop], times: Seq[Time])
  extends TimetableManager {

  def getNextLine(stopId: Int, time: LocalTime): Option[String] = {
    times
      .filter(t => t.stopId == stopId)
      .map(t => {
        val lineName = getLineName(t.lineId).get
        val currentDelay = delayManager.getDelay(lineName)
        // TODO handle delay making the vehicle coming next day
        (lineName, t.time.plus(currentDelay, ChronoUnit.MINUTES))
      })
      .minByOption(t => t._2)
      .map(t => t._1)
  }

  def getLineAt(x: Int, y: Int, time: LocalTime): Option[String] = {
    getStopId(x, y)
      .flatMap(stopId => getLineIdAtStop(time, stopId))
      .flatMap(getLineName)
  }

  private def getLineIdAtStop(time: LocalTime, stopId: Int): Option[Int] = {
    times.find(t => t.stopId == stopId && t.time == time).map(_.lineId)
  }

  private def getStopId(x: Int, y: Int) = {
    stops.find(s => s.x == x && s.y == y).map(_.stopId)
  }

  private def getLineName(lineId: Int): Option[String] = {
    lines.find(l => l.lineId == lineId).map(_.lineName)
  }
}

object InMemoryTimetableManager {

  private def readLines(): Seq[Line] = {
    readCsv("src/main/resources/lines.csv", cols => {
      Line(cols(0).toInt, cols(1))
    })
  }

  private def readStops(): Seq[Stop] = {
    readCsv("src/main/resources/stops.csv", cols => {
      Stop(cols(0).toInt, cols(1).toInt, cols(2).toInt)
    })
  }

  private def readTimes(): Seq[Time] = {
    readCsv("src/main/resources/times.csv", cols => {
      Time(cols(0).toInt, cols(1).toInt, LocalTime.parse(cols(2)))
    })
  }

  private def readCsv[K](filename: String, mapFn: Array[String] => K) = {
    val bufferedSource = Source.fromFile(filename)
    val stops = Seq().appendedAll(
      bufferedSource.getLines().drop(1).map(l => l.split(",").map(_.trim)).map(mapFn))
    bufferedSource.close
    stops
  }

  def getStaticCsvTimetableManager(delayManager: DelayManager): InMemoryTimetableManager = {
    new InMemoryTimetableManager(delayManager, readLines(), readStops(), readTimes())
  }
}

case class Line(lineId: Int, lineName: String)

case class Stop(stopId: Int, x: Int, y: Int)

case class Time(lineId: Int, stopId: Int, time: LocalTime)

trait TimetableManager {
  def getNextLine(stopId: Int, time: LocalTime = LocalTime.now()): Option[String]

  def getLineAt(x: Int, y: Int, time: LocalTime): Option[String]
}
