package delays

import java.time.LocalTime
import java.time.temporal.ChronoUnit

import org.scalatest.funsuite.AnyFunSuite

class DelayManagerTest extends AnyFunSuite {
  private val delayManager = new InMemoryDelayManager

  test("no delay for non-existing line") {
    assert(!delayManager.isDelayed("a"))
    assert(delayManager.getDelay("a") == 0)
  }

  test("handles delay 0 correctly") {
    delayManager.updateDelay("A3", 0)

    assert(!delayManager.isDelayed("A3"))
  }

  test("delay drops with time") {
    delayManager.updateDelay("A3", 10, LocalTime.now().minus(5, ChronoUnit.MINUTES))
    assert(delayManager.getDelay("A3") == 5)
  }
}
