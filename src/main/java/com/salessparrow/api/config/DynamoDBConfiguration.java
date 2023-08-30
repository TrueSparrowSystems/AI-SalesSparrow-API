package com.salessparrow.api.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;

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
		// Override DynamoDb operations to add logging.
		return new DynamoDBMapper(buildAmazonDynamoDB(), dynamoDBMapperConfig()) {
			@Override
			public <T> T load(Class<T> clazz, Object hashKey) {
				T response = null;
				long startTimestamp = System.currentTimeMillis();
				try {
					response = defaultMapper.load(clazz, hashKey);
				} catch (Exception e) {
					logger.debug("DBQuery:Load: table-{} hashKey-{}", clazz.getSimpleName(), hashKey);
					logger.error("DBQuery:Load: exception-{}", e);
					throw new RuntimeException("Error during load database operation", e);
				}

				long duration = System.currentTimeMillis() - startTimestamp;
				logger.debug("({} ms)DBQuery:Load: table-{} hashKey-{}", duration, clazz.getSimpleName(), hashKey);
				return response;
			}

			@Override
			public <T> void save(T object) {
				long startTimestamp = System.currentTimeMillis();
				try {
					defaultMapper.save(object);
				} catch (Exception e) {
					logger.debug("DBQuery:Save: table-{}", object.getClass().getSimpleName());
					logger.error("DBQuery:Save: exception-{}", e);
					throw new RuntimeException("Error during save database operation", e);
				}

				long duration = System.currentTimeMillis() - startTimestamp;
				logger.debug("({} ms)DBQuery:Save: table-{}", duration, object.getClass().getSimpleName());
			}

			@Override
			public <T> void save(T object, DynamoDBMapperConfig config) {
				long startTimestamp = System.currentTimeMillis();
				try {
					defaultMapper.save(object, config);
				} catch (Exception e) {
					logger.debug("DBQuery:Save: table-{}", object.getClass().getSimpleName());
					logger.error("DBQuery:Save: exception-{}", e);
					throw new RuntimeException("Error during save database operation", e);
				}

				long duration = System.currentTimeMillis() - startTimestamp;
				logger.debug("({} ms)DBQuery:Save: table-{}", duration, object.getClass().getSimpleName());
			}

			// Similarly, you can override other used methods like delete, batchSave, etc.
			// similarly
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
                .withSaveBehavior(SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES)
                .build();
    }

}
