package com.salessparrow.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AWS-related beans and settings.
 * 
 */
@Configuration
public class AwsConfig {

  /**
   * Creates and configures an AWS KMS (Key Management Service) client.
   *
   * @return An instance of AWSKMS that allows access to AWS KMS operations.
   */
  @Bean
  public AWSKMS kmsClient() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
        CoreConstants.awsAccessKeyId(),
        CoreConstants.awsSecretAccessKey());

    return AWSKMSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(CoreConstants.awsRegion())
        .build();
  }
}