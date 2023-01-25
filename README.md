# Optimistic Locking And Pessimistic Locking example with Spring Boot

## Introduction

Have concurrency control is one of the most common problems of the development.
Sometimes, depends on the framework or software we can use some tools to avoid these 
issues.

In this Spring Boot project we are going to deal with Optimistic and Pessimistic Locking
as two different approaches  to mitigate the concurrency problem. 

## Optimistic Locking

This is our first approach, this solution is not going to lock rows. Optimistic assumes
that a row would not be changed by another transaction, but if we have this case 
then we are going to hava a field with a version of the commit. So if you are going to 
update data then you have to have the correct version of the field version. 


If you want more information you can take a look in this article : 
https://refactorizando.com/optimistic-locking-jpa-spring-data/

## Pesimistic Locking

The idea behind pessimistic approach is prevents simultaneous modification of a record. 
To reach this goal we are going to lock the record a time.

If you want more information you can take a look in this article :
https://refactorizando.com/pessimistic-locking-jpa-spring-boot

## How does it work?

To verify and check how optimistic locking works you can run the folder test.

    ./mvnw clean verify


