package com.salessparrow.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import jakarta.annotation.PostConstruct;

@TestConfiguration
@TestPropertySource(properties = {
    "aws.aws.accesskey=test1",
    "aws.aws.secretkey=test231",
})
public class LocalStackConfiguration {

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.0.4.1.nodejs18"))
                    .withServices(Service.DYNAMODB)
                    .withNetworkAliases("localstack")
                    .withNetwork(Network.builder().createNetworkCmdModifier(cmd -> cmd.withName("test-net")).build());

    static {
        localStack.start();
        System.setProperty("aws.dynamodb.endpoint", localStack.getEndpointOverride(Service.DYNAMODB).toString());
    }

    @PostConstruct
    public void init() {
        // DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        // CreateTableRequest tableUserRequest = dynamoDBMapper
        //         .generateCreateTableRequest(User.class);
        // tableUserRequest.setProvisionedThroughput(
        //         new ProvisionedThroughput(1L, 1L));
        // amazonDynamoDB.createTable(tableUserRequest);
    }
}
