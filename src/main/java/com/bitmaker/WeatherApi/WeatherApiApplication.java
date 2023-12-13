package com.bitmaker.WeatherApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class WeatherApiApplication {

	private static final Logger LOG = LoggerFactory.getLogger(WeatherApiApplication.class);

	private static final String WEATHER_API_URL = "http://api.weatherstack.com/current?query=%s&access_key=%s";

	private static final String API_KEY = "ddf4b41f0b0a0458af9ec4a0ff1079b4";

	public static void main(String[] args) throws JsonProcessingException {
		String city = "How is the day today?";
		ResponseEntity<String> response = callChatGPTApi(city);

		String body = response.getBody();
		String temperature = getTemperature(body);
		System.out.println("The weather in " + city + " is " + temperature);
	}

	public static ResponseEntity<String> callWeatherApi(String city) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format(WEATHER_API_URL, city, API_KEY);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return response;

	}


	public static String getTemperature(String responseBody) throws JsonProcessingException {
		String temperature = "";

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(responseBody);

		temperature = jsonNode.get("current").get("temperature").asText() + "°C";
		return temperature;
	}
	public static ResponseEntity<String> callChatGPTApi(String inputText) {

		final String uri = "https://api.openai.com/v1/completions";
		final String apiKey = "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";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(apiKey);

		String requestBody = "{\"prompt\": \"" + inputText + "\", \"model\":\"text-davinci-003\", \"max_tokens\": 150, \"temperature\": 0.7}";
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		return response;
	}
	public static ChatGPTResponse callChatGPTApiHold(String inputText) {

		final String uri = "https://api.openai.com/v1/engines/davinci-codex/completions";
		final String apiKey = "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";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(apiKey);

		String requestBody = "{\"prompt\": \"" + inputText + "\", \"max_tokens\": 150, \"temperature\": 0.7}";
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChatGPTResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, ChatGPTResponse.class);
		return response.getBody();
	}
}

