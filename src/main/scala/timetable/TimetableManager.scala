package timetable

import java.time.LocalTime

import scala.io.Source

class InMemoryTimetableManager(lines: Seq[Line], stops: Seq[Stop], times: Seq[Time]) extends TimetableManager {
  def getNextLine(stopId: Int, time: LocalTime): Option[String] = {
    times
      .find(t => t.stopId == stopId && t.time.isAfter(time))
      .map(_.lineId)
      .flatMap(getLineName)
  }

  def getLineAt(x: Int, y: Int, time: LocalTime): Option[String] = {
    getStopId(x, y)
      .flatMap(stopId => getLineId(time, stopId))
      .flatMap(getLineName)
  }

  private def getLineId(time: LocalTime, stopId: Int): Option[Int] = {
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

  def getStaticCsvTimetableManager: InMemoryTimetableManager = {
    new InMemoryTimetableManager(readLines(), readStops(), readTimes())
  }
}

case class Line(lineId: Int, lineName: String)

case class Stop(stopId: Int, x: Int, y: Int)

case class Time(lineId: Int, stopId: Int, time: LocalTime)

trait TimetableManager {
  def getNextLine(stopId: Int, time: LocalTime = LocalTime.now()): Option[String]

  def getLineAt(x: Int, y: Int, time: LocalTime): Option[String]
}
