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
import com.github.dynamobee.exception.DynamobeeException;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.errorLib.ErrorObject;
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

    @BeforeEach
    public void setUp() throws DynamobeeException, IOException {
        setup.perform();
    }

    @AfterEach
    public void tearDown() {
        cleanup.perform();
    }

   @Autowired
   private SalesforceUserRepository salesforceUserRepository;

        @Test
        public void testSaveSalesforceUser() {
            //Valid Save Db Query
            SalesforceUser salesforceUserValid = new SalesforceUser();
            salesforceUserValid.setExternalUserId("externalUserId-test1");
            SalesforceUser salesforceUserResp = salesforceUserRepository.saveSalesforceUser(salesforceUserValid);
            assertEquals(salesforceUserValid.getExternalUserId(), salesforceUserResp.getExternalUserId());

            // Invalid Save Db Query without partition key
            SalesforceUser salesforceUserInvalid = new SalesforceUser();
            salesforceUserInvalid.setExternalUserId("externalUserId-test2");
            
            // mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceUserRepository salesforceUserRepository = new SalesforceUserRepository(dynamoDBMapper);

            // Mock the behavior to throw an exception when save is called
            doThrow(new CustomException(new ErrorObject("test:r_sotr_tssfu_1", "something_went_wrong", "mock db save error")))
            .when(dynamoDBMapper)
            .save(salesforceUserInvalid);

            // Test if CustomException is thrown with the expected error code
            CustomException thrownException = assertThrows(
                CustomException.class, 
                () -> salesforceUserRepository.saveSalesforceUser(salesforceUserInvalid)
            );
            // Validate the error identifier to be a 500 error
            assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
        }

        @Test
        public void testGetSalesforceUserByExternalUserId() throws Exception{{

            String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
            FixtureData fixtureData = common.loadFixture("classpath:fixtures/unit/repositories/salesforceUserRepository.json", currentFunctionName);
            loadFixture.perform(fixtureData);
            
            //Valid Get Db Query
            SalesforceUser salesforceUserResp = salesforceUserRepository.getSalesforceUserByExternalUserId("0055i00000AUxQHAA1");
            assertEquals("0055i00000AUxQHAA1", salesforceUserResp.getExternalUserId());

            String testExternalUserId = "externalUserId-test3";

            // mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceUserRepository salesforceUserRepository = new SalesforceUserRepository(dynamoDBMapper);


            // Mock the behavior to throw an exception when load is called
            when(dynamoDBMapper.load(SalesforceUser.class, testExternalUserId)).thenThrow(
                new CustomException(new ErrorObject("test:r_sur_gsotbsfui_1",
              "something_went_wrong","mock db query error")));

            // Mock Invalid Get Db Query
            // Test if CustomException is thrown with the expected error code
            CustomException thrownException = assertThrows(
                CustomException.class, 
                () -> salesforceUserRepository.getSalesforceUserByExternalUserId(testExternalUserId)
            );
            // Validate the error identifier to be a 500 error
            assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
        }
    }
}
