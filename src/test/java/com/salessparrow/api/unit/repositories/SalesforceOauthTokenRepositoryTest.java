package com.salessparrow.api.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.salessparrow.api.config.DynamoDBConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Scenario;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

/**
 * Unit test for SalesforceOauthTokenRepository.
 *
 */
@SpringBootTest
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class SalesforceOauthTokenRepositoryTest {

	@Autowired
	private Setup setup;

	@Autowired
	private Cleanup cleanup;

	@Autowired
	private Common common;

	@Autowired
	private LoadFixture loadFixture;

	@Autowired
	private ResourceLoader resourceLoader;

	private DynamoDBMapper mockDynamoDBMapper;

	private SalesforceOauthTokenRepository realSalesforceOauthTokenRepository;

	private SalesforceOauthTokenRepository mockSalesforceOauthTokenRepository;

	@BeforeEach
	public void setUp() throws DynamobeeException, IOException {
		setup.perform();
		// Use your Spring bean here
		DynamoDBMapper dynamoDBMapper = new DynamoDBConfiguration().dynamoDBMapper();
		this.realSalesforceOauthTokenRepository = new SalesforceOauthTokenRepository(dynamoDBMapper);

		this.mockDynamoDBMapper = mock(DynamoDBMapper.class); // Mocked instance
		this.mockSalesforceOauthTokenRepository = new SalesforceOauthTokenRepository(mockDynamoDBMapper);
	}

	@AfterEach
	public void tearDown() {
		cleanup.perform();
	}

	/**
	 * Test valid case for createSalesforceOauthToken method
	 */
	@Test
	public void testValidCreateSalesforceOauthToken() {
		// Valid Create Db Query
		SalesforceOauthToken salesforceOauthTokenValid = new SalesforceOauthToken();
		salesforceOauthTokenValid.setExternalUserId("externalUserId-1");

		SalesforceOauthToken salesforceOauthTokenResp = this.realSalesforceOauthTokenRepository
			.createSalesforceOauthToken(salesforceOauthTokenValid);
		assertEquals(salesforceOauthTokenValid.getExternalUserId(), salesforceOauthTokenResp.getExternalUserId());
	}

	/**
	 * Test invalid case for createSalesforceOauthToken method
	 */
	@Test
	public void testInvalidCreateSalesforceOauthToken() {
		// Invalid Create Db Query without partition key
		SalesforceOauthToken salesforceOauthTokenInvalid = new SalesforceOauthToken();
		salesforceOauthTokenInvalid.setExternalUserId("externalUserId-2");

		// Mock the behavior to throw an exception when save is called
		doThrow(new AmazonDynamoDBException("mock db save error")).when(mockDynamoDBMapper)
			.save(salesforceOauthTokenInvalid);

		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceOauthTokenRepository.createSalesforceOauthToken(salesforceOauthTokenInvalid));
		// Validate the error identifier to be a 500 error
		assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
	}

	/**
	 * Test valid case for updateSalesforceOauthToken method
	 */
	@Test
	public void testValidUpdateSalesforceOauthToken() {
		// Valid Update Db Query
		SalesforceOauthToken salesforceOauthTokenValid = new SalesforceOauthToken();
		salesforceOauthTokenValid.setExternalUserId("externalUserId-1");

		SalesforceOauthToken salesforceOauthTokenResp = this.realSalesforceOauthTokenRepository
			.updateSalesforceOauthToken(salesforceOauthTokenValid);
		assertEquals(salesforceOauthTokenValid.getExternalUserId(), salesforceOauthTokenResp.getExternalUserId());
	}

	/**
	 * Test invalid case for updateSalesforceOauthToken method
	 */
	@Test
	public void testInvalidUpdateSalesforceOauthToken() {
		// Invalid Update Db Query without partition key
		SalesforceOauthToken salesforceOauthTokenInvalid = new SalesforceOauthToken();
		salesforceOauthTokenInvalid.setExternalUserId("externalUserId-2");

		// Mock the behavior to throw an exception when save is called
		doThrow(new AmazonDynamoDBException("mock db save error")).when(mockDynamoDBMapper)
			.save(salesforceOauthTokenInvalid);

		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceOauthTokenRepository.updateSalesforceOauthToken(salesforceOauthTokenInvalid));
		// Validate the error identifier to be a 500 error
		assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
	}

	/**
	 * Test valid case for getSalesforceOauthTokenByExternalUserId method
	 */
	@Test
	public void testValidGetSalesforceOauthTokenByExternalUserId() throws Exception {

		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/unit/repositories/salesforceOauthTokenRepository.fixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		// Valid Get Db Query
		SalesforceOauthToken salesforceOauthTokenResp = this.realSalesforceOauthTokenRepository
			.getSalesforceOauthTokenByExternalUserId("0055i00000AUxQHAA1");
		assertEquals("0055i00000AUxQHAA1", salesforceOauthTokenResp.getExternalUserId());
	}

	/**
	 * Test invalid case for getSalesforceOauthTokenByExternalUserId method
	 */
	@Test
	public void testInvalidGetSalesforceOauthTokenByExternalUserId() throws Exception {

		String testExternalUserId = "externalUserId-3";

		// Mock the behavior to throw an exception when load is called
		when(mockDynamoDBMapper.load(SalesforceOauthToken.class, testExternalUserId))
			.thenThrow(new AmazonDynamoDBException("mock db get error"));

		// Mock Invalid Get Db Query
		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceOauthTokenRepository.getSalesforceOauthTokenByExternalUserId(testExternalUserId));
		// Validate the error identifier to be a 500 error
		assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
	}

	/**
	 * Test valid case for insert method and verify the inserted data
	 * @throws Exception
	 */
	@Test
	public void testInsert() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		List<Scenario> testDataItems = loadTestData(currentFunctionName);

		for (Scenario testDataItem : testDataItems) {

			ObjectMapper objectMapper = new ObjectMapper();
			SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
					objectMapper.writeValueAsString(testDataItem.getInput()),
					new TypeReference<SalesforceOauthToken>() {
					});
			salesforceOauthToken.setCreatedAt(Util.getCurrentTimeInDateFormat());

			SalesforceOauthToken insertedSalesforceOauthToken = this.realSalesforceOauthTokenRepository
				.updateSalesforceOauthToken(salesforceOauthToken);

			assertNotNull(insertedSalesforceOauthToken);
			assertNotNull(insertedSalesforceOauthToken.getExternalUserId());
			assertNotNull(insertedSalesforceOauthToken.getCreatedAt());
			assertNotNull(insertedSalesforceOauthToken.getUpdatedAt());
		}
	}

	/**
	 * Test valid case for update method and verify the updated data
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/unit/repositories/salesforceOauthTokenRepository.fixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = loadTestData(currentFunctionName);

		for (Scenario testDataItem : testDataItems) {

			ObjectMapper objectMapper = new ObjectMapper();
			SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
					objectMapper.writeValueAsString(testDataItem.getInput()),
					new TypeReference<SalesforceOauthToken>() {
					});

			SalesforceOauthToken existingSalesforceOauthToken = this.realSalesforceOauthTokenRepository
				.getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

			this.realSalesforceOauthTokenRepository.updateSalesforceOauthToken(salesforceOauthToken);

			SalesforceOauthToken updatedSalesforceOauthToken = this.realSalesforceOauthTokenRepository
				.getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

			assertNotNull(updatedSalesforceOauthToken);
			assertNotNull(updatedSalesforceOauthToken.getUpdatedAt());
			assertEquals(updatedSalesforceOauthToken.getCreatedAt(), existingSalesforceOauthToken.getCreatedAt());
			assertEquals(updatedSalesforceOauthToken.getInstanceUrl(), existingSalesforceOauthToken.getInstanceUrl());
			assertEquals(updatedSalesforceOauthToken.getAccessToken(), salesforceOauthToken.getAccessToken());
			assertEquals(updatedSalesforceOauthToken.getRefreshToken(), salesforceOauthToken.getRefreshToken());
		}
	}

	/**
	 * Test valid case for update with null attributes
	 * @throws Exception
	 */
	@Test
	public void testUpdateWithNullAttributes() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		FixtureData fixtureData = common.loadFixture(
				"classpath:fixtures/unit/repositories/salesforceOauthTokenRepository.fixture.json",
				currentFunctionName);
		loadFixture.perform(fixtureData);

		List<Scenario> testDataItems = loadTestData(currentFunctionName);
		for (Scenario testDataItem : testDataItems) {

			ObjectMapper objectMapper = new ObjectMapper();
			SalesforceOauthToken salesforceOauthToken = objectMapper.readValue(
					objectMapper.writeValueAsString(testDataItem.getInput()),
					new TypeReference<SalesforceOauthToken>() {
					});

			SalesforceOauthToken existingSalesforceOauthToken = this.realSalesforceOauthTokenRepository
				.getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

			this.realSalesforceOauthTokenRepository.updateSalesforceOauthToken(salesforceOauthToken);

			SalesforceOauthToken updatedSalesforceOauthToken = this.realSalesforceOauthTokenRepository
				.getSalesforceOauthTokenByExternalUserId(salesforceOauthToken.getExternalUserId());

			assertEquals(updatedSalesforceOauthToken.getAccessToken(), salesforceOauthToken.getAccessToken());
			assertEquals(updatedSalesforceOauthToken.getIdToken(), existingSalesforceOauthToken.getIdToken());
		}
	}

	/**
	 * Load test data scenarios from json file
	 * @throws Exception
	 */
	public List<Scenario> loadTestData(String key) throws IOException {
		String scenariosPath = "classpath:data/unit/repositories/salesforceOauthTokenRepository.scenarios.json";
		Resource resource = resourceLoader.getResource(scenariosPath);
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, List<Scenario>> scenariosMap = new HashMap<>();
		scenariosMap = objectMapper.readValue(resource.getInputStream(),
				new TypeReference<HashMap<String, List<Scenario>>>() {
				});
		return scenariosMap.get(key);
	}

}
