package com.devsuperior.dscommerce.qualifiers;

import org.mapstruct.Qualifier;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Import(value = { OrderItemDtosToOrderItemsQualifier.class, OrderItemToOrderItemDtosQualifier.class})
public @interface OrderItemMapperQualifier {

}
