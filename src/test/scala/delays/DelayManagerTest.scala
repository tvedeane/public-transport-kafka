package delays

import org.scalatest.funsuite.AnyFunSuite

class DelayManagerTest extends AnyFunSuite {
  private val delayManager = new InMemoryDelayManager

  test("no delay for non-existing line") {
    assert(!delayManager.isDelayed("a"))
  }

  test("returns delay") {
    delayManager.updateDelay("A3", 10)

    assert(delayManager.isDelayed("A3"))
    assert(delayManager.getDelay("A3") == 10)
  }

  test("handles delay 0 correctly") {
    delayManager.updateDelay("A3", 0)

    assert(!delayManager.isDelayed("A3"))
  }
}