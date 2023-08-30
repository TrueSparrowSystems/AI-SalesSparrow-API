package com.salessparrow.api.lib.salesforce.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SalesforceContactWrapperDto {
  private List<SalesforceContactDto> records = new ArrayList<>();
}