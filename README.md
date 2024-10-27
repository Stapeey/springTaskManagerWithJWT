to run have java17 or later installed and maven installed

TO COPY

open up a terminal and enter the following:
  git clone https://github.com/Stapeey/springTaskManagerWithJWT.git
  cd springTaskManagerWithJWT/task
  mvn clean install

TO CONFIGURE
navigate to: task/src/main/resources/application.properties
run your database server and pass its parameters into the config file:
  spring.datasource.url=YOUR_DB_URL (for mysql: jdbc:mysql://localhost:3306/task)
  spring.datasource.username=YOURUSERNAME
  spring.datasource.password=YOURPASSWORD
  spring.datasource.driver-class-name=YOUR_DB_DRIVER (for mysql: com.mysql.cj.jdbc.Driver)
save

TO RUN
navigate to /task open a terminal and enter: mvn spring-boot:run

TO USE
Send http requests to your host ip with port 8080 (i used postman for this)
ill demonstrate through examples:
first register
POST localhost:8080/auth/register -> {
    "username":"Swag",
    "password":"yolo"
}
second login:
POST localhost:8080/auth/login -> {
    "username":"Swag",
    "password":"yolo"
}
if no errors arise you will receive a JWT token, which you can use for authentication for the next 30 minutes until it expires
note that this is a JWT BEARER token, authorization is stateless, you have to include it in every request

to get your tasks:
GET localhost:8080/listall
to create your task:
POST localhost:8080/create -> {
    "deadline": "2024-09-06",
    "description": "this is another test",
    "status": "pending"
}
to update your task:
PUT localhost:8080/update -> {
    "deadline": "2024-09-05",
    "description": "this is another test",
    "status": "pending"
}
to delete your task:
DELETE localhost:8080/delete -> {
    "id":2
}
