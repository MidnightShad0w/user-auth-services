The educational project includes three services where the services communicate using http

Testing:

1)clone repository

2)use docker-compose up -d from parent(user-auth-services) directory

For testing application use Postman or Insomnia.

Test Requests:

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

After posting any of this requests check logs in docker 