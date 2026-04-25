package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.qualifiers.OrderItemDtosToOrderItemsQualifier;
import com.devsuperior.dscommerce.qualifiers.OrderItemMapperQualifier;
import com.devsuperior.dscommerce.qualifiers.OrderItemToOrderItemDtosQualifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@OrderItemMapperQualifier
public class OrderItemCollectionMapper {

    @OrderItemDtosToOrderItemsQualifier
    static List<OrderItemDTO> mapOrderItemsToOrderItemDtos(Set<OrderItem> entities) {
        List<OrderItemDTO> dtos = new ArrayList<>();

        for (OrderItem item: entities) {
            dtos.add(new OrderItemDTO(item));
        }

        return dtos;
    }

    @OrderItemToOrderItemDtosQualifier
    static Set<OrderItem> mapOrderItemDtosToOrderItems(List<OrderItemDTO> dtos) {
        Set<OrderItem> entities = new HashSet<>();

        for (OrderItemDTO dto: dtos) {
            entities.add(new OrderItem(dto));
        }

        return entities;
    }

}
