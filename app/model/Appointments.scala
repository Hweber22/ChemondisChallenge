package model

import play.api.libs.json.{JsValue, Json}

case class Appointments(persons: List[Person], possibleDates: List[Timeslot])

object Appointments {

  val entireWeek = Timespan.spansToSlots(List(Timespan("Mo", 0, 24), Timespan("Tu", 0, 24), Timespan("We", 0, 24), Timespan("Th", 0, 24), Timespan("Fr", 0, 24), Timespan("Sa", 0, 24), Timespan("Su", 0, 24)))
  def commonSlots(slotlists: List[List[Timeslot]], acc: List[Timeslot]): List[Timeslot] = slotlists match {
    case Nil => Nil
    case List(slots) => slots.filter(slot => acc.contains(slot))
    case x :: xs => commonSlots(xs, x.filter(slot => acc.contains(slot)))
  }

  def possibleAppointments(persons: List[Person]): Either[String, Appointments] = {
    val candidates = persons.filter(p => p.typ == "Candidate")
    val interviewers = persons.filter(p => p.typ == "Interviewer")
    if(candidates.length == 1 && interviewers.nonEmpty) {
      val slots = commonSlots(persons.map { person => person.availableSlots }, entireWeek)
      Right(Appointments(persons, slots))
    }
    else Left("must be one candidate and one or more interviewers")
  }
}

object AppointmentsFormat {
  import TimeslotFormat._
  import PersonFormat._

  implicit val appointmentsFormat = Json.format[Appointments]

  implicit class AppointmentsOpsTo(appointments: Appointments) {
    def toJson = Json.toJson(appointments)
  }

  implicit class AppointmentsOpsFrom(appointments: Appointments.type) {
    def fromJson(json: JsValue) = Json.fromJson[Appointments](json)
  }

}