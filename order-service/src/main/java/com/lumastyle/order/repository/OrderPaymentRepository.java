package com.lumastyle.order.repository;

import com.lumastyle.order.entity.Order;
import com.lumastyle.order.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {
    OrderPayment findByOrder(Order order);
}
