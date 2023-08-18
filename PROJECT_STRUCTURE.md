# Project Directory Structure - Sales Sparrow API

This file provides an overview of the directory structure of the project. It explains the purpose of key folders and files and provides a general guideline for organizing your project files.

## Project Root Directory:

```
/ai-salessparrow-api/
|-- src/
|-- pom.xml
|-- README.md
|-- docs/
```

### src/

This directory is where you will typically store your source code files. It contains Java classes, packages, and other resources that make up your application. This is where you'll implement your business logic, controllers, services, data access code, and more.

### pom.xml

It contains configuration and dependency information for your project. You define project details, dependencies, build settings, and other configurations here. Maven uses this file to manage your project's build, dependencies, and other aspects.

### README.md

This is a Markdown file that usually contains important information about your project. It gives an overview of your project, installation instructions, usage guidelines, and any other relevant details.


### docs/

This directory serves as a submodule linked to [ai-salesparrow-docs](https://github.com/TrueSparrowSystems/AI-SalesSparrow-docs) repository that holds comprehensive documentation for the project. Instead of duplicating documentation within the main repository, this submodule approach allows you to maintain documentation separately, ensuring consistency and easier management.

## src/ Directory:

```
/ai-salessparrow-api/
|-- src/
    |-- main/
    |   |-- java/
    |   |   |-- com/
    |   |   |   |-- salessparrow/
    |   |   |   |   |-- api/
    |   |   |   |   |   |-- changelogs/
    |   |   |   |   |   |-- config/
    |   |   |   |   |   |-- controllers/
    |   |   |   |   |   |-- domain/
    |   |   |   |   |   |-- dto/
    |   |   |   |   |   |-- exception/
    |   |   |   |   |   |-- interceptors/
    |   |   |   |   |   |-- lib/
    |   |   |   |   |   |-- repositories/
    |   |   |   |   |   |-- services/
    |   |   |   |   |   |-- SalesSparrowApi.java
    |   |-- resources/
    |   |   |-- config/ 
    |   |   |-- application.properties
    |-- test/
    |   |-- java/
    |   |   |-- com/
    |   |   |   |-- salessparrow/
    |   |   |   |   |-- api/
    |   |   |   |   |   |-- functional/
    |   |   |   |   |   |-- unit/
    |   |   |   |   |   |-- helper/
    |   |-- resources/
    |   |   |-- data/
    |   |   |-- fixtures/
```

### main/

This directory contains the main source code for your application. 

#### java/

This directory contains the Java source code for your application.

##### com/salessparrow/api/

This directory contains the specific source code for your application. It contains the below directories and files.

###### changelogs/

This directory contains the Dynamobee changelog files for your application which are used to manage database changes.

###### config/

This directory contains the different configuration classes for your application like aws, dynamobee, cors, etc. 

###### controllers/

This directory contains the controller classes for your application which are used to handle the incoming requests and send the response back to the client.

###### domain/

This directory contains the domain classes for your application which are used to map the database tables.

###### dto/

This directory contains the DTO classes for your application which are used to map the request and response objects.

###### exception/

This directory contains the exception classes for your application which are used to handle different exceptions.

###### interceptors/

This directory contains the interceptor classes for your application which are used to intercept the incoming requests and outgoing responses. It contains interceptors for logging, authentication, etc.

###### lib/

This directory contains the library classes for your application which are used to handle the common functionalities like http client, utility functions, etc.

###### repositories/

This directory contains the repository classes for your application which are used to handle the database operations.

###### services/

This directory contains the service classes for your application which are used to handle the business logic.

###### SalesSparrowApi.java

This is the main class for your application which is  used to start the application.

#### resources/

This directory contains the resources for your application.

##### config/

This directory contains the configuration files for your application like apiErrorConfig, paramErrorConfig, etc.

##### application.properties

This file contains the application properties for your application like server port, database connection details, etc.

### test/

This directory contains the test source code for your application.

#### java/

This directory contains the Java test source code for your application.

##### com/salessparrow/api/

This directory contains the specific test source code for your application. It contains the below directories and files.


###### functional/

This directory contains the functional test classes for your application which are used to test the application end to end.

###### unit/

This directory contains the unit test classes for your application which are used to test the application units.

###### helper/

This directory contains the helper classes for running the tests.

#### resources/

This directory contains the test resources for your application. It contains the below directories and files.

##### data/

This directory contains the data files for your application which contains the test data.

##### fixtures/

This directory contains the fixtures files for your application which are used to load the data into the database.

