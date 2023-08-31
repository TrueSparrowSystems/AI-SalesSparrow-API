package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.GetCrmOrganizationUsersFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetCrmOrganizationUsersDto;
import com.salessparrow.api.services.crmOrganizationUsers.GetCrmOrganizationUsersList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/crm-organization-users")
public class CrmOrganizationUserController {

	Logger logger = LoggerFactory.getLogger(CrmOrganizationUserController.class);

	@Autowired
	private GetCrmOrganizationUsersList getCrmOrganizationUsersList;

	@GetMapping("")
	public ResponseEntity<GetCrmOrganizationUsersFormatterDto> getCrmOrganizationUsers(HttpServletRequest request,
			@Valid @ModelAttribute GetCrmOrganizationUsersDto CrmOrganizationUsersDto) {
		logger.info("Get list of crm organization users request received");

		GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsersFormatterDto = getCrmOrganizationUsersList
			.getCrmOrganizationUsers(request, CrmOrganizationUsersDto);

		return ResponseEntity.ok().body(getCrmOrganizationUsersFormatterDto);
	}

}
