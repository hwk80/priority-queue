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

## Example workflow
1. GET /orders to view all orders (initially empty)
2. POST /orders with *idCust* and *quantity* parameters (repeat as needed)
3. GET /orders to view all orders with their position in the queue and approximate wait time
4. GET /orders/{idCust} to view a specific order with its position in the queue and approximate wait time
5. GET /orders/next-delivery to view the list of orders for the next delivery
6. DELETE /orders/{idCusts} with the IDs of the orders retrieved from the previous call to accept the delivery and remove it from the queue

## More
* open <http://localhost:8080/h2-console/> in your browser to access the H2 database tables that implement the queue.
Use the default values with the JDBC-URL `jdbc:h2:mem:testdb`.
