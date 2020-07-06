package delays

import java.time.LocalTime
import java.time.temporal.ChronoUnit

import scala.collection.concurrent.TrieMap

class InMemoryDelayManager extends DelayManager {

  private val delays = TrieMap[String, (Int, LocalTime)]()

  def isDelayed(lineName: String): Boolean =
    delays.get(lineName).exists(_._1 > 0)

  def getDelay(lineName: String): Int = {
    val now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
    val delayOpt = delays.get(lineName)
    delayOpt match {
      case Some(delay) =>
        val expectedArrival = delay._2.plus(delay._1, ChronoUnit.MINUTES)
        val delayValue = ChronoUnit.MINUTES.between(now, expectedArrival)
        Math.max(0, delayValue).toInt
      case None => 0
    }
  }

  def updateDelay(lineName: String, delay: Int, reportedAt: LocalTime): Unit =
    delays.put(lineName, (delay, reportedAt.truncatedTo(ChronoUnit.MINUTES)))
}

trait DelayManager {
  def isDelayed(lineName: String): Boolean

  def getDelay(lineName: String): Int

  def updateDelay(lineName: String, delay: Int, reportedAt: LocalTime = LocalTime.now()): Unit
}
