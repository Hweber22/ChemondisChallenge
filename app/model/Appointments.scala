package model

import play.api.libs.json.{JsValue, Json}

case class Appointments(persons: List[Person], possibleDates: List[Timeslot])

object Appointments {

  val entireWeek = Timespan.spansToSlots(List(Timespan("Mo", 7, 19), Timespan("Tu", 7, 19), Timespan("We", 7, 19), Timespan("Th", 7, 19), Timespan("Fr", 7, 19)))
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
    else Left("Cannot make an appointment due to wrong number of Candidates or Interviewers")
  }
}

object AppointmentsFormat {

  implicit val appointmentsFormat = Json.format[Appointments]

  implicit class AppointmentsOpsTo(appointments: Appointments) {
    def toJson = Json.toJson(appointments)
  }

  implicit class AppointmentsOpsFrom(appointments: Appointments.type) {
    def fromJson(json: JsValue) = Json.fromJson[Appointments](json)
  }

}