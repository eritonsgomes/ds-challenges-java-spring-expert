package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper extends BeanMapper<OrderItem, OrderItemDTO> {

    @Override
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.imgUrl", target = "imgUrl")
    OrderItemDTO toDTO(OrderItem item);

    @Override
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "name", target = "product.name")
    @Mapping(source = "price", target = "product.price")
    @Mapping(source = "imgUrl", target = "product.imgUrl")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Override
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.imgUrl", target = "imgUrl")
    List<OrderItemDTO> toDTO(List<OrderItem> orderItems);

    @Override
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "name", target = "product.name")
    @Mapping(source = "price", target = "product.price")
    @Mapping(source = "imgUrl", target = "product.imgUrl")
    List<OrderItem> toEntity(List<OrderItemDTO> orderItemDTOS);

}
