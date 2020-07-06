package timetable

import java.time.LocalTime

import delays.DelayManager
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._

class TimetableManagerTest extends AnyFunSuite {

  test("finds next line at stop") {
    val delayManagerMock = mock(classOf[DelayManager])
    val timetableManager = new InMemoryTimetableManager(
      delayManagerMock,
      Seq(Line(1, "A1")), Seq(Stop(1, 2, 2)), Seq(Time(1, 1, LocalTime.of(8, 10))))

    assert(timetableManager.getNextLine(1, LocalTime.of(8, 9)).contains("A1"))
    assert(timetableManager.getNextLine(1, LocalTime.of(8, 10)).isEmpty)
  }

  test("finds line at location") {
    val delayManagerMock = mock(classOf[DelayManager])
    val timetableManager = new InMemoryTimetableManager(
      delayManagerMock,
      Seq(Line(1, "A1")), Seq(Stop(1, 2, 2)), Seq(Time(1, 1, LocalTime.of(8, 10))))

    assert(timetableManager.getLineAt(2, 2, LocalTime.of(8, 10)).contains("A1"))
    assert(timetableManager.getLineAt(1, 2, LocalTime.of(8, 10)).isEmpty)
  }
}
