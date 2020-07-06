import delays.{DelayObserver, InMemoryDelayManager}
import javax.servlet.ServletContext
import org.scalatra.LifeCycle
import timetable.InMemoryTimetableManager

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit = {
    val delayStorage = new InMemoryDelayManager
    DelayObserver.startObserving(delayStorage)
    context.mount(new Servlets(delayStorage, InMemoryTimetableManager.getStaticCsvTimetableManager), "/*")
  }
}
