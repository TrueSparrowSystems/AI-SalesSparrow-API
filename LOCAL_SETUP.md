### Prerequisites and System Requirements

Before using the *Sales Sparrow* apis, make sure your development environment meets the following prerequisites and system requirements:
* For Docker **(Recommended)**
    - Docker version 4.19.0 or newer
* Without Docker
    - Java 17 
        - You can download it from [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
    - Dynamo DB Local Setup
        - Download the dynamodb local jar(noSQL Workbench) from [here](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.settingup.html)
        - Install and run the noSQL Workbench on your local machine
        - Toggle *DDB local* option from bottom of the left sidemenu to turn it on.
        - Add a  local connection from the operation builder tab

## Getting Started
To clone the project and install dependencies, follow these steps:

### Clone the project

```
$ git clone git@github.com:TrueSparrowSystems/AI-SalesSparrow-API.git
$ cd AI-SalesSparrow-API
```

### Clone the AI-SalesSparrow-Docs submodule ###
```
$ git submodule update --init
```

### Set Environment Variables
1. Copy the contents of set_env_vars-sample.sh to set_env_vars.sh
2. Update the values of the environment variables in set_env_vars.sh
     - Salesforce credentials needs to be copied from the salesforce connected app
     - Aws credentials needs to be copied from the aws account

### Start servers with docker
```
$ docker-compose up
```

### Start servers without docker   
```
$ source set_env_vars.sh 
$ ./mvnw spring-boot:run    
 ```
 
 **To install new dependencies**
 ```
 $ ./mvnw clean install -Dmaven.test.skip
 ```

 **To run test cases**
 ```
 $ ./mvnw clean test
 ```

 **To run test cases and generate coverage report**
 ```
 $ ./mvnw clean test jacoco:report
 ```