package controllers

import model._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import repo.PersonsRepository

import scala.concurrent.{ExecutionContext, Future}

class AppointmentController(controllerComponents: ControllerComponents, personsRepo: PersonsRepository) (implicit ec: ExecutionContext)
  extends AbstractController(controllerComponents) {


  def setFreeTimeslots(name: String, typ: String) = Action(parse.json).async { implicit request =>
    Json.fromJson[List[Timeslot]](request.body) match {
      case JsSuccess(slots, _) => {
        val emptyPerson = Person(name, typ, List.empty)
        personsRepo.setFreeTimeslots(emptyPerson, slots).map(_ => Ok("Timeslot saved"))
        }
      case JsError(errors) => Future.successful(BadRequest(errors.mkString(",")))
    }
  }

  def appointments(persons: List[Person]) = Action.async { implicit request =>
    import model.TimeslotFormat._
    Appointments.possibleAppointments(persons).fold(
      error => Future.successful(BadRequest(error)),
      appointments => Future.successful(Ok(Json.toJson(appointments)))
    )
  }
}
