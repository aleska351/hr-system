package com.codingdrama.hrsystem.repository;

import com.codingdrama.hrsystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
