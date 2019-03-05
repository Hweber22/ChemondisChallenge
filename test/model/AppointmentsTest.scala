package model

import org.scalatest.{FlatSpec, Matchers}

class AppointmentsTest extends FlatSpec with Matchers {

  "Appointments" should "serialize to Json and the Json should deserialize to Appointments" in {
    import model.AppointmentsFormat._
    import model.PersonFormat._
    import model.TimeslotFormat._

    val appointments = Appointments(
      persons = List(
        Person("Henning", "Candidate", List(Timeslot("Monday", 10)))
      ),
      possibleDates = List(
        Timeslot("Monday", 10)
      )
    )
    Appointments.fromJson(appointments.toJson).get should equal(appointments)
  }

  it should "find common slots" in {

    val entireWeek = Timespan.spansToSlots(List(Timespan("Monday", 0, 24), Timespan("Tuesday", 0, 24), Timespan("Wednesday", 0, 24), Timespan("Thursday", 0, 24), Timespan("Friday", 0, 24), Timespan("Saturday", 0, 24), Timespan("Sunday", 0, 24)))

    val slotlists = List(
      List(Timeslot("Monday", 10),
           Timeslot("Monday", 13),
           Timeslot("Thursday", 14),
           Timeslot("Friday", 9)
      ),
      List(Timeslot("Monday", 10),
           Timeslot("Monday", 11),
           Timeslot("Monday", 12),
           Timeslot("Thursday", 17),
           Timeslot("Friday", 9)
      )
    )

    Appointments.commonSlots(slotlists, entireWeek) should equal(List(Timeslot("Monday", 10), Timeslot("Friday", 9)))
  }

  it should "not work when no interviewer is present" in {

    val persons = List(
      Person("Henning", "Candidate", List(Timeslot("Monday", 10))),
      Person("Erika", "Candidate", List(Timeslot("Monday", 10)))
    )

    Appointments.possibleAppointments(persons) should equal(Left("must be one candidate and one or more interviewers"))
  }

  it should "find appointments for a candidate and multiple interviewers" in {

    val carl = Person("Carl", "Candidate",
      Timespan.spansToSlots(
        List(Timespan("Monday", 9, 10),
          Timespan("Tuesday", 9, 10),
          Timespan("Wednesday", 9, 12),
          Timespan("Thursday", 9, 10),
          Timespan("Friday", 9, 10)
        ))
    )
    val philipp = Person("Philipp", "Interviewer",
      Timespan.spansToSlots(
        List(Timespan("Monday", 9, 16),
          Timespan("Tuesday", 9, 16),
          Timespan("Wednesday", 9, 16),
          Timespan("Thursday", 9, 16),
          Timespan("Friday", 9, 16)
        ))
    )
    val sarah = Person("Sarah", "Interviewer",
      Timespan.spansToSlots(
        List(Timespan("Monday", 12, 18),
          Timespan("Tuesday", 9, 12),
          Timespan("Wednesday", 12, 18),
          Timespan("Thursday", 9, 12)
        ))
    )

    val persons = List(carl, philipp, sarah)

    Appointments.possibleAppointments(persons) should equal(Right(Appointments(persons, List(Timeslot("Tuesday", 9), Timeslot("Thursday", 9)))))

  }
}