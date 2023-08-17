package com.salessparrow.api.helper;

import java.util.List;

import lombok.Data;

@Data
public class FixtureData {
  List<FilePathData> salesforce_users;
  List<FilePathData> salesforce_oauth_tokens;
  List<FilePathData> salesforce_organiztions;
}

@Data
class FilePathData {
  String filepath;
}