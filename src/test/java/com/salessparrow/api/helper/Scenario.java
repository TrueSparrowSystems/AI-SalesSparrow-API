package com.salessparrow.api.helper;

import java.util.Map;

import lombok.Data;

@Data
public class Scenario {
  String description;

  Map<String, Object> input;

  Map<String, Object> mocks;

  Map<String, Object> output;
}
