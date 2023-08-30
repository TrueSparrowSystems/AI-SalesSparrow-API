package com.salessparrow.api.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Common is a helper class for the tests.
 */
@TestConfiguration
public class Common {

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * Load the fixture data from the given location.
	 * @param location
	 * @param key
	 * @return FixtureData
	 * @throws IOException
	 */
	public FixtureData loadFixture(String location, String key) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, FixtureData> fixtureMap = new HashMap<>();
		fixtureMap = objectMapper.readValue(resource.getInputStream(),
				new TypeReference<HashMap<String, FixtureData>>() {
				});
		return fixtureMap.get(key);
	}

	/**
	 * Load the test scenario data from the given location.
	 * @param location
	 * @return List<Scenario>
	 * @throws IOException
	 */
	public List<Scenario> loadScenariosData(String location) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Scenario>>() {
		});
	}

	/**
	 * Load the test scenario data from the given location and key.
	 * @param location - location of the test data
	 * @param key - key of the test data
	 * @return List<Scenario>
	 * @throws IOException
	 */
	public List<Scenario> loadScenariosData(String location, String key) throws IOException {
		Resource resource = resourceLoader.getResource(location);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<Scenario>> scenarioMap = new HashMap<>();
		scenarioMap = objectMapper.readValue(resource.getInputStream(),
				new TypeReference<HashMap<String, List<Scenario>>>() {
				});
		return scenarioMap.get(key);
	}

	/**
	 * Compare the errors from the test data with the actual errors.
	 * @param testDataItem
	 * @param contentAsString
	 */
	public void compareErrors(Scenario testDataItem, String contentAsString) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode resJsonNode = objectMapper.readTree(contentAsString);
		Integer httpCode = resJsonNode.get("http_code").asInt();
		assertEquals(testDataItem.getOutput().get("http_code"), httpCode);

		String code = resJsonNode.get("code").asText();
		assertEquals(testDataItem.getOutput().get("code"), code);

		List<String> paramErrors = (List<String>) testDataItem.getOutput().get("param_errors");
		if (paramErrors != null) {
			for (int i = 0; i < paramErrors.size(); i++) {
				String actualError = resJsonNode.get("param_errors").get(i).get("param_error_identifier").asText();
				assertEquals(paramErrors.contains(actualError), true);
			}
		}
	}

	// Custom assertion method that treats {} and "" as the same
	public void assertCustomEquals(String expected, String actual) {

		if (expected.equals("{}")) {
			expected = "";
		}

		assertEquals(expected, actual);
	}

}