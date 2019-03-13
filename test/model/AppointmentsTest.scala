package model

import java.util.UUID

import org.scalatest.{FlatSpec, Matchers}

class AppointmentsTest extends FlatSpec with Matchers {

  "Appointments" should "serialize to Json and the Json should deserialize to Appointments" in {
    import model.AppointmentsFormat._
    import model.InterviewerFormat._
    import model.CandidateFormat._
    import model.TimeslotFormat._

    val appointments = Appointments(
      candidate = Candidate(UUID.randomUUID(), "Henning"),
      interviewers = List.empty,
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

    val henning = Candidate(UUID.randomUUID(), "Henning")
    val erika = Candidate(UUID.randomUUID(), "Erika")
    val henningsSlots = List(Timeslot("Monday", 10))
    val erikasSlots = List(Timeslot("Monday", 10))

    val personMap: Map[Person, List[Timeslot]] = Map(henning -> henningsSlots, erika -> erikasSlots)
    Appointments.possibleAppointments(personMap) should equal(Left("must be one candidate and one or more interviewers"))
  }

  it should "find appointments for a candidate and multiple interviewers" in {

    val carl = Candidate(UUID.randomUUID(), "Carl")

    val carlsSlots = Timespan.spansToSlots(
        List(Timespan("Monday", 9, 10),
          Timespan("Tuesday", 9, 10),
          Timespan("Wednesday", 9, 12),
          Timespan("Thursday", 9, 10),
          Timespan("Friday", 9, 10)
        ))

    val philipp = Interviewer(UUID.randomUUID(), "Philipp")

    val philippsSlots = Timespan.spansToSlots(
        List(Timespan("Monday", 9, 16),
          Timespan("Tuesday", 9, 16),
          Timespan("Wednesday", 9, 16),
          Timespan("Thursday", 9, 16),
          Timespan("Friday", 9, 16)
        ))

    val sarah = Interviewer(UUID.randomUUID(), "Sarah")

    val sarahsSlots = Timespan.spansToSlots(
        List(Timespan("Monday", 12, 18),
          Timespan("Tuesday", 9, 12),
          Timespan("Wednesday", 12, 18),
          Timespan("Thursday", 9, 12)
        ))

    val personMap: Map[Person, List[Timeslot]] = Map(carl -> carlsSlots, philipp -> philippsSlots, sarah -> sarahsSlots)

    Appointments.possibleAppointments(personMap) should equal(Right(Appointments(carl, List(philipp, sarah) , List(Timeslot("Tuesday", 9), Timeslot("Thursday", 9)))))

  }
}