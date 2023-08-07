package com.salessparrow.api.data;

import java.util.Map;

import lombok.Data;

@Data
public class TestDataItem {
  String description;

  Map<String, Object> input;

  Map<String, Object> output;
}
