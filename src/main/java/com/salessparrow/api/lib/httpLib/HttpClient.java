package com.salessparrow.api.lib.httpLib;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {

	public static class HttpResponse {

		private int statusCode;

		private String responseBody;

		private Map<String, List<String>> headers;

		private String contentType;

		public HttpResponse(int statusCode, String responseBody, Map<String, List<String>> headers,
				String contentType) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
			this.headers = headers;
			this.contentType = contentType;
		}

		public HttpResponse() {
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public Map<String, List<String>> getHeaders() {
			return headers;
		}

		public String getContentType() {
			return contentType;
		}

		public void setResponseBody(String responseBody) {
			this.responseBody = responseBody;
		}

	}

	public static HttpResponse makeGetRequest(String url, Map<String, String> headers, int timeoutMillis) {

		WebClient webClient = WebClient.builder().build();

		WebClient.RequestHeadersSpec<?> request = webClient.get().uri(url);

		if (headers != null) {
			request.headers(httpHeaders -> {
				headers.forEach(httpHeaders::set);
			});
		}

		Mono<ResponseEntity<String>> responseMono = request.retrieve().toEntity(String.class);

		ResponseEntity<String> responseEntity = responseMono.block(Duration.ofMillis(timeoutMillis));

		int statusCode = responseEntity.getStatusCode().value();
		String responseBody = responseEntity.getBody();
		Map<String, List<String>> responseHeaders = new HashMap<>(responseEntity.getHeaders());
		String contentType = "";
		if (responseEntity.getHeaders().getContentType() != null) {
			contentType = responseEntity.getHeaders().getContentType().toString();
		}
		return new HttpResponse(statusCode, responseBody, responseHeaders, contentType);
	}

	public static HttpResponse makePostRequest(String url, Map<String, String> headers, Object requestBody,
			int timeoutMillis) {
		WebClient webClient = WebClient.builder().build();

		WebClient.RequestHeadersSpec<?> request = webClient.post()
			.uri(url)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(requestBody);

		if (headers != null) {
			request.headers(httpHeaders -> {
				headers.forEach(httpHeaders::set);
			});
		}

		Mono<ResponseEntity<String>> responseMono = request.retrieve().toEntity(String.class);

		ResponseEntity<String> responseEntity = responseMono.block(Duration.ofMillis(timeoutMillis));

		int statusCode = responseEntity.getStatusCode().value();
		String responseBody = responseEntity.getBody();
		Map<String, List<String>> responseHeaders = new HashMap<>(responseEntity.getHeaders());
		String contentType = "";
		if (responseEntity.getHeaders().getContentType() != null) {
			contentType = responseEntity.getHeaders().getContentType().toString();
		}
		return new HttpResponse(statusCode, responseBody, responseHeaders, contentType);
	}

}