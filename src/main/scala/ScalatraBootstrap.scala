import delays.{DelayObserver, InMemoryDelayManager}
import javax.servlet.ServletContext
import org.scalatra.LifeCycle
import timetable.InMemoryTimetableManager

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit = {
    val delayManager = new InMemoryDelayManager
    DelayObserver.startObserving(delayManager)
    context.mount(
      new Servlets(delayManager, InMemoryTimetableManager.getStaticCsvTimetableManager(delayManager)), "/*")
  }
}
