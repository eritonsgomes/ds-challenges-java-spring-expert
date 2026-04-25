package com.devsuperior.dscommerce.repositories;


import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.projections.OrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = """
            SELECT o.id AS "ORDER_ID", o.moment AS "ORDER_MOMENT", o.status AS "ORDER_STATUS",
                oi.product_id, p.img_url AS "PRODUCT_IMG_URL", p.name AS "PRODUCT_NAME", oi.price AS "PRODUCT_PRICE",
                oi.quantity, pay.order_id AS "PAYMENT_ID", pay.moment AS "PAYMENT_MOMENT",
                c.id AS "CLIENT_ID", c.name AS "CLIENT_NAME"
            FROM tb_order o
            INNER JOIN tb_user c ON o.client_id = c.id
            INNER JOIN tb_order_item oi ON o.id = oi.order_id
            INNER JOIN tb_product p ON p.id = oi.product_id
            LEFT JOIN tb_payment pay ON o.id = pay.order_id
            WHERE o.id = :id
            ORDER BY c.name
            """)
    List<OrderProjection> searchById(Long id);

}
