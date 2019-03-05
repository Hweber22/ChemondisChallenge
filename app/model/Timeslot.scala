package model

import play.api.libs.json.{JsValue, Json}

case class Timeslot(weekday: String, startingTime: Int)


object TimeslotFormat {

  implicit val timeslotFormat = Json.format[Timeslot]

  implicit class TimeslotOpsTo(timeslot: Timeslot) {
    def toJson = Json.toJson(timeslot)
  }

  implicit class TimeslotOpsFrom(timeslot: Timeslot.type) {
    def fromJson(json: JsValue) = Json.fromJson[Timeslot](json)
  }

}

case class Timespan(weekday: String, startingTime: Int, endTime: Int)

object TimespanFormat {

  implicit val timespanFormat = Json.format[Timespan]

  implicit class TimespanOpsTo(timespan: Timespan) {
    def toJson = Json.toJson(timespan)
  }

  implicit class TimespanOpsFrom(timeslot: Timespan.type) {
    def fromJson(json: JsValue) = Json.fromJson[Timespan](json)
  }
}

object Timespan {

  def spanToSlots(span: Timespan): List[Timeslot] =
    (span.startingTime until span.endTime).map(hour => Timeslot(span.weekday, hour)).toList

  def spansToSlots(spans: List[Timespan]): List[Timeslot] =
    spans.flatMap(spanToSlots)
}