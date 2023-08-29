package com.salessparrow.api.unit.repositories;

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
import com.salessparrow.api.config.DynamoDBConfiguration;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.repositories.SalesforceUserRepository;

@SpringBootTest
@Import({ Setup.class, Cleanup.class, Common.class, LoadFixture.class })
public class SalesforceUserRepositoryTest {

	@Autowired
	private Setup setup;

	@Autowired
	private Cleanup cleanup;

	@Autowired
	private Common common;

	@Autowired
	private LoadFixture loadFixture;

	private DynamoDBMapper mockDynamoDBMapper;

	private SalesforceUserRepository realSalesforceUserRepository;

	private SalesforceUserRepository mockSalesforceUserRepository;

	@BeforeEach
	public void setUp() throws DynamobeeException, IOException {
		setup.perform();
		// Use your Spring bean here
		DynamoDBMapper dynamoDBMapper = new DynamoDBConfiguration().dynamoDBMapper();
		this.realSalesforceUserRepository = new SalesforceUserRepository(dynamoDBMapper);

		this.mockDynamoDBMapper = mock(DynamoDBMapper.class); // Mocked instance
		this.mockSalesforceUserRepository = new SalesforceUserRepository(mockDynamoDBMapper);
	}

	@AfterEach
	public void tearDown() {
		cleanup.perform();
	}

	/**
	 * Test Valid Save Db Query
	 *
	 */
	@Test
	public void testValidSaveSalesforceUser() {
		// Valid Save Db Query
		SalesforceUser salesforceUserValid = new SalesforceUser();
		salesforceUserValid.setExternalUserId("externalUserId-test1");
		SalesforceUser salesforceUserResp = this.realSalesforceUserRepository.saveSalesforceUser(salesforceUserValid);
		assertEquals(salesforceUserValid.getExternalUserId(), salesforceUserResp.getExternalUserId());
	}

	/**
	 * Test Invalid Save Db Query
	 *
	 */
	@Test
	public void testInvalidSaveSalesforceUser() {
		// Invalid Save Db Query without partition key
		SalesforceUser salesforceUserInvalid = new SalesforceUser();
		salesforceUserInvalid.setExternalUserId("externalUserId-test2");

		// Mock the behavior to throw an exception when save is called
		doThrow(new AmazonDynamoDBException("mock db save error")).when(mockDynamoDBMapper).save(salesforceUserInvalid);

		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceUserRepository.saveSalesforceUser(salesforceUserInvalid));
		// Validate the error identifier to be a 500 error
		assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
	}

	/**
	 * Test Valid Get Db Query
	 * @throws Exception
	 */
	@Test
	public void testValidGetSalesforceUserByExternalUserId() throws Exception {
		String currentFunctionName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		FixtureData fixtureData = common
			.loadFixture("classpath:fixtures/unit/repositories/salesforceUserRepository.json", currentFunctionName);
		loadFixture.perform(fixtureData);

		// Valid Get Db Query
		SalesforceUser salesforceUserResp = this.realSalesforceUserRepository
			.getSalesforceUserByExternalUserId("0055i00000AUxQHAA1");
		assertEquals("0055i00000AUxQHAA1", salesforceUserResp.getExternalUserId());
	}

	/**
	 * Test Invalid Get Db Query
	 * @throws Exception
	 */
	@Test
	public void testInvalidGetSalesforceUserByExternalUserId() throws Exception {
		String testExternalUserId = "externalUserId-test3";

		// Mock the behavior to throw an exception when load is called
		when(mockDynamoDBMapper.load(SalesforceUser.class, testExternalUserId))
			.thenThrow(new AmazonDynamoDBException("mock db get error"));

		// Mock Invalid Get Db Query
		// Test if CustomException is thrown with the expected error code
		CustomException thrownException = assertThrows(CustomException.class,
				() -> mockSalesforceUserRepository.getSalesforceUserByExternalUserId(testExternalUserId));
		// Validate the error identifier to be a 500 error
		assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
	}

}
