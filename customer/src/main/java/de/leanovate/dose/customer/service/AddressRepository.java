package de.leanovate.dose.customer.service;

import de.leanovate.dose.customer.domain.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "addresses", path = "addresses")
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
}
