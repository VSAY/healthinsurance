package com.vsay.microservice.provider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProviderApp.class)
@IntegrationTest
@WebAppConfiguration
@ActiveProfiles("test")
public class ProviderWebIntegrationTest {

	@Autowired
	private ProviderRepository ProviderRepository;

	@Value("${server.port:8080}")
	private int serverPort;

	private RestTemplate restTemplate;

	private <T> T getForMediaType(Class<T> value, MediaType mediaType,
			String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(mediaType));

		HttpEntity<String> entity = new HttpEntity<String>("parameters",
				headers);

		ResponseEntity<T> resultEntity = restTemplate.exchange(url,
				HttpMethod.GET, entity, value);

		return resultEntity.getBody();
	}

	@Test
	public void IsProviderReturnedAsHTML() {

		Provider ProviderWolff = ProviderRepository.findByName("Smith").get(0);

		String body = getForMediaType(String.class, MediaType.TEXT_HTML,
				ProviderURL() + ProviderWolff.getId() + ".html");

		assertThat(body, containsString("Smith"));
		assertThat(body, containsString("<div"));
	}

	@Before
	public void setUp() {
		restTemplate = new RestTemplate();
	}

	@Test
	public void IsProviderReturnedAsJSON() {

		Provider ProviderWolff = ProviderRepository.findByName("Smith").get(0);

		String url = ProviderURL() + "Provider/" + ProviderWolff.getId();
		Provider body = getForMediaType(Provider.class,
				MediaType.APPLICATION_JSON, url);

		assertThat(body, equalTo(ProviderWolff));
	}

	@Test
	public void IsProviderListReturned() {

		Iterable<Provider> Providers = ProviderRepository.findAll();
		assertTrue(StreamSupport.stream(Providers.spliterator(), false)
				.noneMatch(c -> (c.getName().equals("Hoeller1"))));
		ResponseEntity<String> resultEntity = restTemplate.getForEntity(
				ProviderURL() + "/list.html", String.class);
		assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
		String ProviderList = resultEntity.getBody();
		assertFalse(ProviderList.contains("Hoeller1"));
		ProviderRepository.save(new Provider("Juergen", "Hoeller1",
				"springjuergen@twitter.com", "Schlossallee", "Linz"));

		ProviderList = restTemplate.getForObject(ProviderURL() + "/list.html",
				String.class);
		assertTrue(ProviderList.contains("Hoeller1"));

	}

	private String ProviderURL() {
		return "http://localhost:" + serverPort + "/";
	}

	@Test
	public void IsProviderFormDisplayed() {
		ResponseEntity<String> resultEntity = restTemplate.getForEntity(
				ProviderURL() + "/form.html", String.class);
		assertTrue(resultEntity.getStatusCode().is2xxSuccessful());
		assertTrue(resultEntity.getBody().contains("<form"));
	}

	@Test
	@Transactional
	public void IsSubmittedProviderSaved() {
		assertEquals(0, ProviderRepository.findByName("Hoeller").size());
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("firstname", "Juergen");
		map.add("name", "Hoeller");
		map.add("street", "Schlossallee");
		map.add("city", "Linz");
		map.add("email", "springjuergen@twitter.com");

		restTemplate.postForObject(ProviderURL() + "form.html", map,
				String.class);
		assertEquals(1, ProviderRepository.findByName("Hoeller").size());
	}

}
