package model

import org.scalatest.{FlatSpec, Matchers}

class TimeslotTest extends FlatSpec with Matchers {

  "Timeslot" should "serialize to Json and the Json should deserialize to Timeslot" in {
    import model.TimeslotFormat._

    val timeslot = Timeslot(
      weekday = "Wednesday",
      startingTime = 8
    )
    Timeslot.fromJson(timeslot.toJson).get should equal(timeslot)
  }

  "Timespan" should "serialize to Json and the Json should deserialize to Timespan" in {
    import model.TimespanFormat._

    val timespan = Timespan(
      weekday = "Wednesday",
      startingTime = 8,
      endTime = 11
    )
    Timespan.fromJson(timespan.toJson).get should equal(timespan)
  }

  it should "split correctly into Timeslots" in {
   val timespans = List(
     Timespan( weekday = "Wednesday", startingTime = 8, endTime = 11),
     Timespan( weekday = "Thursday", startingTime = 11, endTime = 12)
   )
   val timeslots =  List(
     Timeslot( weekday = "Wednesday", startingTime = 8),
     Timeslot( weekday = "Wednesday", startingTime = 9),
     Timeslot( weekday = "Wednesday", startingTime = 10),
     Timeslot( weekday = "Thursday", startingTime = 11)
   )
    Timespan.spansToSlots(timespans) should equal(timeslots)
  }
}