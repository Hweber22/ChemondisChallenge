package controllers

import java.util.UUID

import javax.inject.Inject
import model.AppointmentsFormat._
import model.TimeslotFormat._
import model._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.InjectedController
import repo.PersonsRepository

import scala.concurrent.{ExecutionContext, Future}

class AppointmentController @Inject()(personsRepo: PersonsRepository)(implicit ec: ExecutionContext) extends InjectedController {

  def createCandidate(name: String) = Action(parse.json).async {

    personsRepo.addCandidate(Candidate(UUID.randomUUID(), name)).map { _ =>
      Created(Json.toJson(s"Candidate '$name' saved"))
    }
  }

  def createInterviewer(name: String) = Action(parse.json).async {

    personsRepo.addInterviewer(Interviewer(UUID.randomUUID(), name)).map { _ =>
      Created(Json.toJson(s"Interviewer '$name' saved"))
    }
  }

  def setFreeTimeslots(id: String) = Action(parse.json).async { implicit request =>
    Json.fromJson[List[Timeslot]](request.body) match {
      case JsSuccess(slots, _) =>

        val futurePerson = personsRepo.findPersonByID(UUID.fromString(id))
        futurePerson.map { person =>
          personsRepo.setFreeTimeslots(person, slots)}
          .map { _ =>
          Ok(Json.toJson(s"Timeslots saved"))
        }

      case JsError(errors) =>
        Future.successful(BadRequest(Json.toJson(errors.mkString(","))))
    }
  }

  def appointments(ids: List[String]) = Action.async { implicit request =>
    val convertedIDs = ids.map(id => UUID.fromString(id))
    personsRepo.addedPersons.map { ps =>
      ps.filter(p => convertedIDs.contains(p._1.id))
    }.map { ps =>
      Appointments.possibleAppointments(ps).fold(
        error => BadRequest(Json.toJson(error)),
        appointments => Ok(Json.toJson(appointments))
      )
    }
  }

  def allCandidates = Action.async { implicit request =>
    import model.CandidateFormat._

    personsRepo.addedCandidates
      .map { candidate =>
        Ok(Json.toJson(candidate))
      }
  }

  def allInterviewers = Action.async { implicit request =>
    import model.InterviewerFormat._

    personsRepo.addedInterviewers
      .map { interviewer =>
        Ok(Json.toJson(interviewer))
      }
  }
}
