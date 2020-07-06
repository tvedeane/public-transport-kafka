package delays

import java.time.LocalTime

import scala.collection.concurrent.TrieMap

class InMemoryDelayManager extends DelayManager {

  private val delays = TrieMap[String, (Int, LocalTime)]()

  def isDelayed(lineName: String): Boolean =
    delays.get(lineName).exists(_._1 > 0)

  def getDelay(lineName: String): Int =
    delays.get(lineName).map(_._1).getOrElse(0)

  def updateDelay(lineName: String, delay: Int, reportedAt: LocalTime): Unit =
    delays.put(lineName, (delay, reportedAt))
}

trait DelayManager {
  def isDelayed(lineName: String): Boolean

  def getDelay(lineName: String): Int

  def updateDelay(lineName: String, delay: Int, reportedAt: LocalTime = LocalTime.now()): Unit
}
