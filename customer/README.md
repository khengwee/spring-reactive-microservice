# Customer Service 

This is Customer Service project. This project shows how to build a microservice using Spring Webflux (Reactive) 
with Spring Security Oauth2 between microservices integration. 
    
## Run Microservice
Start both services by running the command below:
    
    $ cd customer
    $ mvn spring-boot:run
    
## Test Integration between Microservices
Use the curl command to trigger the Customer Service

    $ curl -H "Authorization: Basic a2l3aXVzZXI6cGFzc3dvcmQ=" -H "Content-Type: application/json" http://localhost:8091/api/customers/1

You will be able to see the sample response from Customer Resource as shown below

    {
      "id": "1",
      "name": "John Smith",
      "segment": "Priority"
    }

    