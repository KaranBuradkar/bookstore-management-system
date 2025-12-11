package com.karanburadkar.bookstore.services.impl;

import com.karanburadkar.bookstore.exception.database.DataIntegrityViolationException;
import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.PaymentMethodDto;
import com.karanburadkar.bookstore.entity.PaymentMethod;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.repository.PaymentMethodRepository;
import com.karanburadkar.bookstore.services.PaymentMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    private PaymentMethod resolvePaymentMethod(Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> {
                    log.error("Payment method not found");
                    return new EntityNotFoundException("Payment method not found");
                });
    }

    @Override
    public List<PaymentMethodDto> getPaymentMethods(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<PaymentMethod> paymentMethodPage = paymentMethodRepository.findAll(PageRequest.of(page, size, dir, sort));

        return paymentMethodPage.stream()
                .map(pm -> new PaymentMethodDto(pm.getId(), pm.getName()))
                .toList();
    }

    @Override
    public PaymentMethodDto getPaymentMethodById(Long paymentMethodId) {
        PaymentMethod paymentMethod = resolvePaymentMethod(paymentMethodId);
        return new PaymentMethodDto(paymentMethod.getId(), paymentMethod.getName());
    }

    @Override
    @Transactional
    public PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(paymentMethodDto.getName())
                .orElse(null);
        if (paymentMethod != null) {
            log.error("Payment method already exist");
            throw new DataIntegrityViolationException(
                    "Payment method already exist",
                    paymentMethod.getName() +": duplicate payment method insertion"
            );
        }
        PaymentMethod saved = paymentMethodRepository.save(new PaymentMethod(paymentMethodDto.getName()));
        return new PaymentMethodDto(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public PaymentMethodDto updatePaymentMethod(PaymentMethodDto paymentMethodDto, Long paymentMethodId) {
        PaymentMethod paymentMethod = resolvePaymentMethod(paymentMethodId);
        paymentMethod.setName(paymentMethod.getName());
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return new PaymentMethodDto(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public void deletePaymentMethodById(Long paymentMethodId) {
        if (paymentMethodRepository.existsById(paymentMethodId)) {
            paymentMethodRepository.deleteById(paymentMethodId);
            log.info("Payment method successfully deleted");
        } else {
            log.error("Payment method not found");
            throw new EntityNotFoundException("Payment method not found with paymentMethodId: "+paymentMethodId);
        }
    }
}
