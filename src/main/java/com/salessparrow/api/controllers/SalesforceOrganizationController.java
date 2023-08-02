package com.salessparrow.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.domain.SalesforceOrganization;
import com.salessparrow.api.repositories.SalesforceOrganizationRepository;

@RestController
public class SalesforceOrganizationController {


    @Autowired
    private SalesforceOrganizationRepository sfoRepository;

    @PostMapping("/add/salesforce-organization")
    public SalesforceOrganization saveSalesforceOrganization(@RequestBody SalesforceOrganization sfo) {
        return sfoRepository.saveSalesforceOrganization(sfo);
    }

}
