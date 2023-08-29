package com.salessparrow.api.unit.repositories;

import com.salessparrow.api.config.DynamoDBConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.repositories.SalesforceOauthTokenRepository;

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
	 * Test valid case for saveSalesforceOauthToken method
	 */
	@Test
	public void testValidSaveSalesforceOauthToken() {
		// Valid Save Db Query
		SalesforceOauthToken salesforceOauthTokenValid = new SalesforceOauthToken();
		salesforceOauthTokenValid.setExternalUserId("externalUserId-1");

		SalesforceOauthToken salesforceOauthTokenResp = this.realSalesforceOauthTokenRepository
			.saveSalesforceOauthToken(salesforceOauthTokenValid);
		assertEquals(salesforceOauthTokenValid.getExternalUserId(), salesforceOauthTokenResp.getExternalUserId());
	}

	/**
	 * Test invlaid case for saveSalesforceOauthToken method
	 */
	@Test
	public void testInvalidSaveSalesforceOauthToken() {
		// Invalid Save Db Query without partition key
		SalesforceOauthToken salesforceOauthTokenInvalid = new SalesforceOauthToken();
		salesforceOauthTokenInvalid.setExternalUserId("externalUserId-2");

		// Mock the behavior to throw an exception when save is called
		doThrow(new AmazonDynamoDBException("mock db save error")).when(mockDynamoDBMapper)
			.save(salesforceOauthTokenInvalid);

		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceOauthTokenRepository.saveSalesforceOauthToken(salesforceOauthTokenInvalid));
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
				"classpath:fixtures/unit/repositories/salesforceOauthTokenRepository.json", currentFunctionName);
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

}
