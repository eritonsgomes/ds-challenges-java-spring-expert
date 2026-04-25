package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.projections.OrderProjection;
import com.devsuperior.dscommerce.projections.OrderProjectionImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderFactory {

	public static Order createOrder(User client) {
		
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		return order;

	}

	public static List<OrderProjection> createOrderProjection(User user) {

		List<OrderProjection> list = new ArrayList<>();

		OrderProjectionImpl order = new OrderProjectionImpl(
			1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user.getId(), user.getUsername(),
			1L, "Console PlayStation 5", 3999.0,
			"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg",
			1, 1L, Instant.now()
		);

		list.add(order);

		return list;

	}

}
