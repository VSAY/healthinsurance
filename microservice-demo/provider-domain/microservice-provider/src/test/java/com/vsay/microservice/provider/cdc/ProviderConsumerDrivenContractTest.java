package com.vsay.microservice.provider.cdc;

import static org.junit.Assert.*;

import java.util.Collection;

import com.vsay.microservice.provider.ProviderApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProviderApp.class)
@IntegrationTest
@WebAppConfiguration
@ActiveProfiles("test")
public class ProviderConsumerDrivenContractTest {

	@Autowired
	ProviderClient providerClient;

	@Test
	public void testFindAll() {
		Collection<Customer> result = providerClient.findAll();
		assertEquals(
				1,
				result.stream()
						.filter(c -> (
								c.getName().equals("Smith")
										&& c.getFirstname().equals("Jon"))).count());
	}

	@Test
	public void testGetOne() {
		Collection<Customer> allCustomer = providerClient.findAll();
		Long id = allCustomer.iterator().next().getCustomerId();
		Customer result = providerClient.getOne(id);
		assertEquals(id.longValue(), result.getCustomerId());
	}

	@Test
	public void testValidCustomerId() {
		Collection<Customer> allCustomer = providerClient.findAll();
		Long id = allCustomer.iterator().next().getCustomerId();
		assertTrue(providerClient.isValidCustomerId(id));
		assertFalse(providerClient.isValidCustomerId(-1));
	}

}
