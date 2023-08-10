# Sales Sparrow API

This repository contains salessparrow apis.

### Prerequisites and System Requirements

Before using the *Sales Sparrow* apis, make sure your development environment meets the following prerequisites and system requirements:

1. Docker: The setup requires Docker version 4.19.0 or newer to build and run.

## Getting Started

To clone the project and install dependencies, follow these steps:

### Clone the project

```
$ git clone git@github.com:TrueSparrowSystems/AI-SalesSparrow-API.git
$ cd AI-SalesSparrow-API
```
### Set Environment Variables
1. Copy the contents of set_env_vars-sample.sh to set_env_vars.sh
2. Update the values of the environment variables in set_env_vars.sh
     - Salesforce credentials needs to be copied from the salesforce connected app
     - Aws credentials needs to be copied from the aws account

### Start servers
```
$ docker-compose up
```
## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contributing
Contributions to the project are welcome! Please see our guidelines on how to [CONTRIBUTE](CONTRIBUTING.md).


## Code of Conduct
This project has a code of conduct that outlines expected behavior for contributors and users. Please read the [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md) file before getting involved.