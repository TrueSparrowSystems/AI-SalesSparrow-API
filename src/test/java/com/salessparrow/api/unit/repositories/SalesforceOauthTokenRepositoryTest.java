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
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.helper.Cleanup;
import com.salessparrow.api.helper.Common;
import com.salessparrow.api.helper.FixtureData;
import com.salessparrow.api.helper.LoadFixture;
import com.salessparrow.api.helper.Setup;
import com.salessparrow.api.lib.errorLib.ErrorObject;
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

    private SalesforceOauthTokenRepository salesforceOauthTokenRepository;

    @BeforeEach
    public void setUp() throws DynamobeeException, IOException {
        setup.perform();
        this.salesforceOauthTokenRepository =  new SalesforceOauthTokenRepository(dynamoDBMapper);

    }

    @AfterEach
    public void tearDown() {
        cleanup.perform();
    }

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

        @Test
        public void testSaveSalesforceOauthToken() {
            //Valid Save Db Query
            SalesforceOauthToken salesforceOauthTokenValid = new SalesforceOauthToken();
            salesforceOauthTokenValid.setExternalUserId("externalUserId-1");

            SalesforceOauthToken salesforceOauthTokenResp = this.salesforceOauthTokenRepository.saveSalesforceOauthToken(salesforceOauthTokenValid);
            assertEquals(salesforceOauthTokenValid.getExternalUserId(), salesforceOauthTokenResp.getExternalUserId());

            // Invalid Save Db Query without partition key
            SalesforceOauthToken salesforceOauthTokenInvalid = new SalesforceOauthToken();
            salesforceOauthTokenInvalid.setExternalUserId("externalUserId-2");

            // mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceOauthTokenRepository mockSalesforceOauthTokenRepository = new SalesforceOauthTokenRepository(dynamoDBMapper);

            // Mock the behavior to throw an exception when save is called
            doThrow(new CustomException(new ErrorObject("test:r_sotr_tssfot_1", "something_went_wrong", "mock db save error")))
            .when(dynamoDBMapper)
            .save(salesforceOauthTokenInvalid);

            // Test if CustomException is thrown with the expected error code
            CustomException thrownException = assertThrows(
                CustomException.class, 
                () -> mockSalesforceOauthTokenRepository.saveSalesforceOauthToken(salesforceOauthTokenInvalid)
            );
            // Validate the error identifier to be a 500 error
            assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
        }

        @Test
        public void testGetSalesforceOauthTokenByExternalUserId() throws Exception{{

            String currentFunctionName = new Object(){}.getClass().getEnclosingMethod().getName();
            FixtureData fixtureData = common.loadFixture("classpath:fixtures/unit/repositories/salesforceOauthTokenRepository.json", currentFunctionName);
            loadFixture.perform(fixtureData);
            
            //Valid Get Db Query
            SalesforceOauthToken salesforceOauthTokenResp = this.salesforceOauthTokenRepository.getSalesforceOauthTokenByExternalUserId("0055i00000AUxQHAA1");
            assertEquals("0055i00000AUxQHAA1", salesforceOauthTokenResp.getExternalUserId());

            String testExternalUserId = "externalUserId-3";

            // mock the DynamoDBMapper
            DynamoDBMapper dynamoDBMapper = mock(DynamoDBMapper.class);
            SalesforceOauthTokenRepository mockSalesforceOauthTokenRepository = new SalesforceOauthTokenRepository(dynamoDBMapper);

            // Mock the behavior to throw an exception when load is called
            when(dynamoDBMapper.load(SalesforceOauthToken.class, testExternalUserId)).thenThrow(
                new CustomException(new ErrorObject("test:r_sotr_gsotbsfui_1",
              "something_went_wrong","mock db query error")));

            // Mock Invalid Get Db Query
            // Test if CustomException is thrown with the expected error code
            CustomException thrownException = assertThrows(
                CustomException.class, 
                () -> mockSalesforceOauthTokenRepository.getSalesforceOauthTokenByExternalUserId(testExternalUserId)
            );
            // Validate the error identifier to be a 500 error
            assertEquals("something_went_wrong", thrownException.getErrorObject().getApiErrorIdentifier());
        }
    }
}
