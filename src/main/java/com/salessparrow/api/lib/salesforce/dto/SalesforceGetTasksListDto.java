package com.salessparrow.api.lib.salesforce.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.salessparrow.api.dto.entities.TaskEntity;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SalesforceGetTasksListDto {
  private String id;
  private String description;
  private Date activityDate;
  private CreatedBy createdBy;
  private Owner owner;
  private Date lastModifiedDate;

  @Data
  @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
  private class CreatedBy{
    private String name;
  }

  @Data
  @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
  private class Owner{
    private String name;
  }

  public TaskEntity taskEntity(){
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setId(this.id);
    taskEntity.setCreatorName(this.createdBy.name);
    taskEntity.setDescription(this.description);
    taskEntity.setDueDate(this.activityDate);
    taskEntity.setCrmOrganizationUserName(this.owner.name);
    taskEntity.setLastModifiedTime(this.lastModifiedDate);
    return taskEntity;
  }
}
