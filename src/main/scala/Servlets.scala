import java.time.LocalTime

import delays.DelayManager
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{ActionResult, NotFound, Ok, ScalatraServlet}
import timetable.TimetableManager

class Servlets(delayManager: DelayManager, timetableManager: TimetableManager)
  extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }
  get("/line/:line_name/is-delayed") {
    Ok(delayManager.isDelayed(params("line_name")))
  }
  get("/line") {
    handleNotFound(
      timetableManager.getLineAt(
        params("x").toInt,
        params("y").toInt,
        LocalTime.parse(params("time"))))
  }
  get("/stop/:stop_id/next-line") {
    handleNotFound(
      timetableManager.getNextLine(params("stop_id").toInt, LocalTime.now()))
  }

  def handleNotFound(response: Option[Any]): ActionResult = {
    response match {
      case None => NotFound(None)
      case response => Ok(response)
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}
