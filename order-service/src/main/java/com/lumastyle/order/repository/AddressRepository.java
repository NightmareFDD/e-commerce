package com.lumastyle.order.repository;

import com.lumastyle.order.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByOrderId(Long orderId);
}
