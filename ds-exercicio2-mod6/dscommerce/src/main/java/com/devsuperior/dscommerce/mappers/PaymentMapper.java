package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.PaymentDTO;
import com.devsuperior.dscommerce.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper extends BeanMapper<Payment, PaymentDTO> {
    
}
