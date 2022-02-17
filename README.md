[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ToxiquOi_P4_ParkingSystem&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ToxiquOi_P4_ParkingSystem)

# Parking System

A command line app for managing the parking system. This app uses Java to run and stores the data in Mysql DB.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 1.8
- Maven 3.6.2
- Docker/docker-compose

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3'.Install Docker:

https://docs.docker.com/desktop/windows/install/


### Running App
Post installation of Docker, Java and Maven, you will have to set up the tables and data in the database.

All utils script are stored under the `docker` folder.

* `start_service.bat`: Launch mysql and adminer (localhost:8080) containers
* `install_database.bat`: To create the database.

Finally, you will be ready to import the code into an IDE of your choice and run the App.java to launch the application.

### Testing

The app has unit tests and integration tests written.
All test are executed after each commit with `github Actions` feature.
We can see all results in the `Actions` page on github.

Sonarcloud is used for create `Quality statistics` and archive them.
all statistics are viewable on sonarcloud if you clic on the badge at start of this .md file

Of course you can always use the standard maven command to start all tests.

`mvn test` or `mvn verify`
