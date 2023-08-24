package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/crm-organization-users")
public class CrmOrganizationUserController {
    Logger logger = LoggerFactory.getLogger(CrmOrganizationUserController.class);
    
    @PostMapping("")
    public void createCrmOrganizationUser(
        @Positive @RequestBody String crmOrganizationUserId
    ){
        logger.info("Create crm organization user request received");
    }
    
}
