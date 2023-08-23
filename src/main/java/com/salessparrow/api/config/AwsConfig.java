package com.salessparrow.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

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
    if (CoreConstants.isTestEnvironment() || CoreConstants.isLocalTestEnvironment()) {
      AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            CoreConstants.localKmsEndpoint(),
            CoreConstants.awsRegion()
        );

      return AWSKMSClientBuilder.standard()
          .withEndpointConfiguration(endpointConfiguration)
          .build();
    }
    
    return AWSKMSClientBuilder.standard()
        .withRegion(CoreConstants.awsRegion())
        .build();
  }

  /**
   * Creates and configures an AWS SES (Simple Email Service) client.
   *
   * @return An instance of AmazonSimpleEmailService that allows access to AWS SES operations.
   */
  @Bean
  public AmazonSimpleEmailService sesClient() {
      return AmazonSimpleEmailServiceClientBuilder.standard()
          .withRegion(CoreConstants.awsRegion())
          .build();
  }

}