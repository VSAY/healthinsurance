package com.vsay.microservice.provider;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "provider", path = "provider")
public interface ProviderRepository extends
		PagingAndSortingRepository<Provider, Long> {

	List<Provider> findByName(@Param("name") String name);

}
