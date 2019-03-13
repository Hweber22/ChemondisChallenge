# Documentation

The API as been documented according to the Openapi 3.0.0 format (formerly Swagger) and can be found in `app/documentation/openapi.json`. 

# Starting the Program

The program is written in Scala. To try the program locally , build a docker image from the file by using `docker build --tag meinprojekt .` Then create a container from that with `docker run --rm -ti -p 9000:9000 --name meinprojekt meinprojekt`. When Docker runs use `sbt` to start sbt, the Scala Build Tool. When sbt has started, use `run` to start the App.

# API Calls

## PUT your name, role and free Timeslots to the repo:

   `curl -H 'Content-Type: application/json' -XPUT http://localhost:9000/freeTimeslots/Interviewer/philipp --data '[{"weekday":"Mon", "startingTime":11}]'`

## GET possible appointments for a list of persons:

   `curl -XGET http://localhost:9000/appointments?persons=philipp&persons=carl&persons=sarah`
     
