package com.salessparrow.api.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.salessparrow.api.domain.SalesforceOauthToken;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;

/**
 * Repository for SalesforceOauthToken.
 */
@Repository
public class SalesforceOauthTokenRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public SalesforceOauthTokenRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/**
	 * Insert a SalesforceOauthToken to the salesforce_oauth_tokens table.
	 * @param salesforceOauthToken
	 * @return SalesforceOauthToken
	 */
	@CacheEvict(value = CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE,
			key = "#salesforceOauthToken.externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceOauthToken createSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
		// Create a row with status active and created at as current time
		salesforceOauthToken.setStatus(SalesforceOauthToken.Status.ACTIVE);
		salesforceOauthToken.setCreatedAt(Util.getCurrentTimeInDateFormat());

		try {
			dynamoDBMapper.save(salesforceOauthToken);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sotr_csot_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceOauthToken;
	}

	/**
	 * Saves a SalesforceOauthToken to the salesforce_oauth_tokens table.
	 * @param salesforceOauthToken
	 * @return SalesforceOauthToken
	 */
	@CacheEvict(value = CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE,
			key = "#salesforceOauthToken.externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceOauthToken updateSalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
		try {
			dynamoDBMapper.save(salesforceOauthToken);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sotr_ssot_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceOauthToken;
	}

	/**
	 * Retrieves a SalesforceOauthToken from the salesforce_oauth_tokens table based on
	 * the provided externalUserId.
	 * @param externalUserId
	 * @return SalesforceOauthToken
	 */
	@Cacheable(value = CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE,
			key = "#externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceOauthToken getSalesforceOauthTokenByExternalUserId(String externalUserId) {
		try {
			return dynamoDBMapper.load(SalesforceOauthToken.class, externalUserId);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sotr_gsotbeui_1", "something_went_wrong", e.getMessage()));
		}
	}

	/**
	 * Deletes a SalesforceOauthToken from the salesforce_oauth_tokens table based on the
	 * provided SalesforceOauthToken.
	 * @param salesforceOauthToken
	 * @return void
	 */
	@CacheEvict(value = CacheConstants.SALESFORCE_OAUTH_TOKEN_CACHE,
			key = "#salesforceOauthToken.externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public void deleteSalesforceOauthTokenBySalesforceOauthToken(SalesforceOauthToken salesforceOauthToken) {
		try {
			dynamoDBMapper.delete(salesforceOauthToken);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sotr_dsotbeui_1", "something_went_wrong", e.getMessage()));
		}
	}

}