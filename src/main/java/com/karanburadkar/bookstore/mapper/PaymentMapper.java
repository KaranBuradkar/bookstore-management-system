package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.PaymentDto;
import com.karanburadkar.bookstore.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toEntity(PaymentDto paymentDto);
    PaymentDto toDto(Payment payment);
}
