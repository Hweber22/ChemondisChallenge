# Documentation

The API as been documented according to the Openapi 3.0.0 format (formerly Swagger) and can be found in `app/documentation/openapi.json`. 

# Starting the Program

The program is written in Scala. To try the program locally , build a docker image from the file by using `docker build --tag meinprojekt .` Then create a container from that with `docker run --rm -ti -p 9000:9000 --name meinprojekt meinprojekt`. When Docker runs use `sbt` to start sbt, the Scala Build Tool. When sbt has started, use `run` to start the App.

# API Calls

## PUT your name and role to the repo:

   `curl -H 'Content-Type: application/json' -XPUT http://localhost:9000/candidate/Carl`
   
   for candidates or 
   
   `curl -H 'Content-Type: application/json' -XPUT http://localhost:9000/interviewer/Philipp`
   
   for interviewers.
   
## PUT your free timeslots: 

   `curl -H 'Content-Type: application/json' -XPUT http://localhost:9000/freeTimeslots/429cc359-afa2-4096-83a4-fa9377205a89 --data '[{"weekday":"Mon", "startingTime":12}]'`   
  
## GET a list of all candidates / of all interviewers

   `curl -XGET http://localhost:9000/allCandidates` or `curl -XGET http://localhost:9000/allInterviewers`

## GET possible appointments for a list of person IDs:

   `curl -XGET http://localhost:9000/appointments?id=bcf741c7-1d4f-45ba-ad74-c36964424f3f&id=99fa0f3f-b197-47cf-a1eb-963f8b34cedf&id=bcf741c7-1d4f-45ba-ad74-c36964424f3f`
     
