package timetable

import java.time.LocalTime

import org.scalatest.funsuite.AnyFunSuite

class TimetableManagerTest extends AnyFunSuite {

  test("finds next line at stop") {
    val timetableManager = new InMemoryTimetableManager(
      Seq(Line(1, "A1")), Seq(Stop(1, 2, 2)), Seq(Time(1, 1, LocalTime.of(8, 10))))

    assert(timetableManager.getNextLine(1, LocalTime.of(8, 9)).contains("A1"))
    assert(timetableManager.getNextLine(1, LocalTime.of(8, 10)).isEmpty)
  }

  test("finds line at location") {
    val timetableManager = new InMemoryTimetableManager(
      Seq(Line(1, "A1")), Seq(Stop(1, 2, 2)), Seq(Time(1, 1, LocalTime.of(8, 10))))

    assert(timetableManager.getLineAt(2, 2, LocalTime.of(8, 10)).contains("A1"))
    assert(timetableManager.getLineAt(1, 2, LocalTime.of(8, 10)).isEmpty)
  }
}