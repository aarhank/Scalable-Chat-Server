## Scalable Chat Server
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) <img alt="Spring" src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"/> <img alt="Java" src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white"/>

A robust and horizontal scalable real-time chat server using SpringBoot WebFlux and Websocket-Redis(Pub/Sub). 
This demonstrates a real-time chat server in a Microservice Architecture to handle high demands,
while maintaining Sync between multiple instances.

### Horizontal Scaling 

Horizontal scaling refers to adding more machines to your infrastructure to cope with the high demand on the server. 
In our microservice context, scaling horizontally is the same as deploying more instances of the microservice,
A load balancer will then be required to distribute the traffic among the multiple microservice instances

### Issue : Message loss due to the load balancer / No Sync between instances

![problem.png](problem.png)

### Solution : Broadcast messages using Pub/Sub

![solution.png](solution.png)
This solution is inspired by Amr Saleh article - 
[Building Scalable Facebook-like Notification using Server-Sent Events and Redis](https://medium.com/javarevisited/building-scalable-facebook-like-notification-using-server-sent-event-and-redis-9d0944dee618)

### Examples

![example1.png](example1.png)

![example2.png](example2.png)
