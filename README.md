The educational project includes three services where the services communicate using http

***

**Testing:**

1)clone repository

2)use docker-compose up -d from parent(user-auth-services) directory

For testing application use Postman or Insomnia.

***

**Test Requests:**

All test request can be used for data: from user1 to user4 and from password1 to password4:

user1, user2 - low score;

user3 - good score;

user4 - no score, but signed up;

***

Composition service - localhost:8082/composition/authorize 
with body - 
{
"login": "user2",
"password": "password2"
}

Score service - localhost:8081/score?login=user1
with no body

Auth service - localhost:8080/auth/login
with body - 
{
"login": "user1",
"password": "password1"
}

gRPC tests - localhost:8082/composition/authorize-grpc 
with body -
{
"login": "user4",
"password": "password4"
}

***

**Testing Traefik:**

To start multiple instances run command:
>docker-compose up -d --scale composition-service=3


address - http://localhost/composition/authorize-grpc
with body -
{
"login": "user4",
"password": "password4"
}

After posting any of this requests check logs in docker

***

**If you want to rebuild project use these commands:**

>docker-compose down --volumes

>docker-compose up --build -d

***

Swagger available on http://localhost/composition/swagger-ui/index.html