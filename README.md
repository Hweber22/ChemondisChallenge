# ChemondisChallenge

The API as been documented according to the Openapi 3.0.0 format (formerly Swagger) and can be found in `app/documentation/openapi.json`. 

With sbt (The Scala Build Tool) running the 2 API routes can be called for example like below. However, I'm currently working on getting rid of the need to use sbt by using a docker container.

## PUT your name, role and free Timeslots to the repo:

   `curl -H 'Content-Type: application/json' -XPUT http://localhost:9000/freeTimeslots/Interviewer/philipp --data '[{"weekday":"Mon", "startingTime":11}]'`

## GET possible appointments for a list of persons:

   `curl -XGET http://localhost:9000/appointments?persons=philipp,carl`
