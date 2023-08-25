package com.salessparrow.api.dto.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * TaskEntity is a DTO class for the Task List.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskEntity {
  private String id;
  private String creatorName;
  private String description;
  private String dueDate;
  private String crmOrganizationUserName;
  private Date lastModifiedTime;
}
