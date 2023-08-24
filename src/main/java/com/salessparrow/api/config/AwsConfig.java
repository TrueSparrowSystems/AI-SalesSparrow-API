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
   * Returns an instance of the AWS Key Management Service (KMS)
   * client based on the environment.
   * 
   * @implNote - Client is configured to use the local KMS endpoint for following environments
   *    - development
   *    - test
   *    - local-test 
   *    For test and production environments, the client is configured to use the AWS KMS endpoint
   * 
   * @return An instance of the AWSKMS client configured 
   * for the appropriate environment.
   * 
   */
  @Bean
  public AWSKMS kmsClient() {
    if (
      CoreConstants.isDevEnvironment() || 
      CoreConstants.isTestEnvironment() || 
      CoreConstants.isLocalTestEnvironment()
    ) {
      
      AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            CoreConstants.localKmsEndpoint(),
            CoreConstants.awsRegion()
        );

      AWSKMS kms = AWSKMSClientBuilder.standard()
          .withEndpointConfiguration(endpointConfiguration)
          .build();

      System.out.println("kms.listKeys(): ======" + kms.listKeys());
      return kms;
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