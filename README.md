# priority-queue

A RESTful service exposing the priority queue. Based on Java, Spring-Boot and an in-memory H2-Database.

## Prerequisites
* Java JDK 1.8+

## Getting started
* clone or download this project from github
* in the project folder, run `mvnw clean install` on linux or `mvnw.cmd clean install` on windows
* then, run `mvnw spring-boot:run` to start the application

## Using the API
* open <http://localhost:8080/swagger-ui.html> in your browser to view the API documentation. 
The UI also provides functionality to call the API directly.
* in the project folder is a file called **example-script** with calls to the API, using `curl` and `jq`.

## More
* open <http://localhost:8080/h2-console/> in your browser to access the H2 database tables that implement the queue.
Use the default values with the JDBC-URL `jdbc:h2:mem:testdb`.
