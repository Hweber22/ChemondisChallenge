package controllers

import javax.inject.Inject
import model.AppointmentsFormat._
import model.TimeslotFormat._
import model._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.InjectedController
import repo.PersonsRepository

import scala.concurrent.{ExecutionContext, Future}

class AppointmentController @Inject()(personsRepo: PersonsRepository)(implicit ec: ExecutionContext) extends InjectedController {

  def setFreeTimeslots(name: String, typ: String) = Action(parse.json).async { implicit request =>
    Json.fromJson[List[Timeslot]](request.body) match {
      case JsSuccess(slots, _) =>

        personsRepo.setFreeTimeslots(name, typ, slots).map { _ =>
          Ok(Json.toJson(s"Timeslots saved for '$name' [$typ]"))
        }

      case JsError(errors) =>
        Future.successful(BadRequest(Json.toJson(errors.mkString(","))))
    }
  }

  def appointments(persons: List[String]) = Action.async { implicit request =>
    personsRepo.addedPersons.map { ps =>
      ps.filter(p => persons.contains(p.name))
    }.map { ps =>
      Appointments.possibleAppointments(ps).fold(
        error => BadRequest(error),
        appointments => Ok(Json.toJson(appointments))
      )
    }
  }
}
