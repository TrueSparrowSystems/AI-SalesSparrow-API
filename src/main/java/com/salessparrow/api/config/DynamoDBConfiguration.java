package com.salessparrow.api.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfiguration {

    Logger logger = LoggerFactory.getLogger(DynamoDBConfiguration.class);

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        DynamoDBMapper defaultMapper = new DynamoDBMapper(buildAmazonDynamoDB(), dynamoDBMapperConfig());
        
        //Override DynamoDb operations to add logging.
        return new DynamoDBMapper(buildAmazonDynamoDB(), dynamoDBMapperConfig()) {

            @Override
            public <T> T load(Class<T> clazz, Object hashKey) {
                logger.debug("DBQuery:Load: table-{} hashKey-{}", clazz.getSimpleName(), hashKey);
                return defaultMapper.load(clazz, hashKey);
            }

            @Override
            public <T> void save(T object) {
                logger.debug("DBQuery:Save: table-{}", object.getClass().getSimpleName());
                defaultMapper.save(object);
            }
            // Similarly, you can override other used methods like delete, batchSave, etc. similarly
        };
    }

    @Bean
    public AmazonDynamoDB buildAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                   new AwsClientBuilder.EndpointConfiguration(
                    CoreConstants.dynamoDbUrl(),
                    CoreConstants.awsRegion()))
                .build();
    }

    @Bean
    DynamoDBMapperConfig dynamoDBMapperConfig() {
        String prefix = CoreConstants.environment() + "_";
        return new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(prefix))
                .build();
    }
}

