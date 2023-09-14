package com.salessparrow.api.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.CacheConstants;

/**
 * Repository for SalesforceUser.
 */
@Repository
public class SalesforceUserRepository {

	private final DynamoDBMapper dynamoDBMapper;

	public SalesforceUserRepository(DynamoDBMapper dynamoDBMapper) {
		this.dynamoDBMapper = dynamoDBMapper;
	}

	/**
	 * Insert a SalesforceUser to the salesforce_users table.
	 * @param salesforceUser
	 * @return SalesforceUser
	 */
	@CacheEvict(value = CacheConstants.SALESFORCE_USER_CACHE,
			key = "#salesforceUser.externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceUser createSalesforceUser(SalesforceUser salesforceUser) {
		// Create a row with status active and created at as current time
		salesforceUser.setStatus(SalesforceUser.Status.ACTIVE);
		salesforceUser.setCreatedAt(Util.getCurrentTimeInDateFormat());

		try {
			dynamoDBMapper.save(salesforceUser);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_csu_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceUser;
	}

	/**
	 * Updates a SalesforceUser to the salesforce_users table.
	 * @param salesforceUser
	 * @return SalesforceUser
	 */
	@CacheEvict(value = CacheConstants.SALESFORCE_USER_CACHE,
			key = "#salesforceUser.externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceUser updateSalesforceUser(SalesforceUser salesforceUser) {
		try {
			dynamoDBMapper.save(salesforceUser);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_usu_1", "something_went_wrong", e.getMessage()));
		}
		return salesforceUser;
	}

	/**
	 * Retrieves a SalesforceUser from the salesforce_users table based on the provided
	 * id.
	 * @param id
	 * @return SalesforceUser
	 */
	@Cacheable(value = CacheConstants.SALESFORCE_USER_CACHE,
			key = "#externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public SalesforceUser getSalesforceUserByExternalUserId(String externalUserId) {
		try {
			return dynamoDBMapper.load(SalesforceUser.class, externalUserId);
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_gsubi_1", "something_went_wrong", e.getMessage()));
		}
	}

	@CacheEvict(value = CacheConstants.SALESFORCE_USER_CACHE,
			key = "#externalUserId + T(com.salessparrow.api.lib.globalConstants.CacheConstants).CACHE_SUFFIX")
	public void removeSalesforceUserData(String externalUserId) {
		SalesforceUser salesforceUser = getSalesforceUserByExternalUserId(externalUserId);
		salesforceUser.setIdentityUrl(null);
		salesforceUser.setExternalOrganizationId(null);
		salesforceUser.setName(null);
		salesforceUser.setEmail(null);
		salesforceUser.setUserKind(null);
		salesforceUser.setCookieToken(null);
		salesforceUser.setEncryptionSalt(null);
		salesforceUser.setStatus(SalesforceUser.Status.DELETED);

		try {
			dynamoDBMapper.save(salesforceUser,
					new DynamoDBMapperConfig.Builder().withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
						.build());
		}
		catch (Exception e) {
			throw new CustomException(new ErrorObject("r_sur_r_1", "something_went_wrong", e.getMessage()));
		}
	}

}