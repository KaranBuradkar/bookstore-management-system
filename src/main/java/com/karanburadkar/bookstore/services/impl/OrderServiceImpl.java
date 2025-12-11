package com.karanburadkar.bookstore.services.impl;

import com.karanburadkar.bookstore.entity.*;
import com.karanburadkar.bookstore.repository.*;
import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.AddressDto;
import com.karanburadkar.bookstore.dto.OrderDto;
import com.karanburadkar.bookstore.dto.OrderItemDto;
import com.karanburadkar.bookstore.entity.type.ShippingStatus;
import com.karanburadkar.bookstore.exception.database.BookNotFoundException;
import com.karanburadkar.bookstore.exception.database.CustomerNotFoundException;
import com.karanburadkar.bookstore.exception.database.OrderNotFoundException;
import com.karanburadkar.bookstore.exception.business.OrderPriceMiscalculationException;
import com.karanburadkar.bookstore.mapper.AddressMapper;
import com.karanburadkar.bookstore.mapper.CustomerMapper;
import com.karanburadkar.bookstore.mapper.OrderItemMapper;
import com.karanburadkar.bookstore.mapper.OrderMapper;
import com.karanburadkar.bookstore.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            AddressRepository addressRepository,
                            CustomerRepository customerRepository,
                            BookRepository bookRepository, OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper, CustomerMapper customerMapper,
                            AddressMapper addressMapper) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public List<OrderDto> getOrders(Integer page, Integer size, String sort, String direction) {

        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 1. GET all orders
        Page<Order> orderPage = orderRepository.findOrdersWithAddressAndCustomerDetails(
                PageRequest.of(page, size, dir, sort));

        // 2. return dto in response orderDto
        return orderPage.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = resolveOrder(orderId);
        return orderMapper.toDto(order);
    }

    private Order resolveOrder(Long orderId) {
        return orderRepository.findOrderWithAddressAndCustomerDetailsById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found.");
                    return new OrderNotFoundException("Order not found with Id: " + orderId);
                });
    }

    @Override
    public List<OrderDto> getOrderByCustomerId(Long customerId, Integer page, Integer size, String sort, String direction) {

        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<Order> orders = orderRepository.findByCustomerId(customerId, PageRequest.of(page, size, dir, sort));

        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    private Customer resolveCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer does not exit.");
                    return new CustomerNotFoundException("Customer not found with customerId: " + customerId);
                });
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        // 1. NULL Id verification
        validateOrderDto(orderDto);

        // 2. Validate total and subtotal price
        validateTotalAndSubtotal(orderDto.getOrderItems(), orderDto.getTotalAmount());

        // 2. Access Customer entity
        Customer customer = resolveCustomer(orderDto.getCustomerId());

        // 3. Check Address exist
        Address address = resolveOrCreateAddress(orderDto.getAddress());

        // 5. map dto -> order entity
        Order order = orderMapper.toEntity(orderDto);
        order.setAddress(address);
        order.setCustomer(customer);

        // 4. Map dto -> orderItems entity
        Set<OrderItem> orderItems = resolveOrderItems(orderDto.getOrderItems(), order);

        order.setOrderItems(orderItems);

        // 6. Order saved in db
        Order savedOrder = orderRepository.save(order);

        // 8. orderItems saved in db
        Set<OrderItem> savedOrderItems = orderItems.stream()
                .map(oi -> {
                    Book book = oi.getBook();
                    book.setStock(book.getStock()-oi.getQuantity()); // Already checked.
                    // Update bookStock
                     bookRepository.save(book);
                     oi.setOrder(savedOrder);

                     return orderItemRepository.save(oi);
                }).collect(Collectors.toSet());

        savedOrder.setOrderItems(savedOrderItems);

        // 10. again map entity -> order dto
        return orderMapper.toDto(savedOrder);
    }

    private Set<OrderItem> resolveOrderItems(Set<OrderItemDto> orderItemDtoSet, Order order) {
        return orderItemDtoSet.stream()
                .map(oi -> {
                    OrderItem entity = orderItemMapper.toEntity(oi);

                    Book book = resolveBook(oi.getBookId());

                    // Filter calculation mistake.
                    validateOrderCalculation(oi, book);

                    entity.setBook(book);
                    entity.setOrder(order);
                    return entity;

                }).collect(Collectors.toSet());
    }

    private void validateTotalAndSubtotal(Set<OrderItemDto> orderItemDtoSet, BigDecimal givenTotalPrice) {

        BigDecimal totalPrice2Decimal = orderItemDtoSet.stream()
                .map(OrderItemDto::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        if (givenTotalPrice.scale() <= 2) {
            givenTotalPrice = givenTotalPrice.setScale(2, RoundingMode.HALF_UP);
        }

        boolean matchedTotalPrice = totalPrice2Decimal.equals(givenTotalPrice);

        if (!matchedTotalPrice) {
            String message = "Order total_price is "+ givenTotalPrice
                    +" and actual total_price is "+totalPrice2Decimal+" calculation mistake.";

            log.error(message);
            throw new OrderPriceMiscalculationException(message);
        }
    }

    private void validateOrderCalculation(OrderItemDto oi, Book book) {

        BigDecimal bookPrice = book.getPrice();
        BigDecimal unitPrice = oi.getUnitPrice().scale() < 2 ?
                oi.getUnitPrice().setScale(2, RoundingMode.HALF_UP) : oi.getUnitPrice();

        if (!Objects.equals(bookPrice, unitPrice)) {
            log.error("Book price: {} and orderItem unit price: {} mismatched for bookId: {}.",
                    bookPrice, oi.getUnitPrice(), book.getId());

            throw new OrderPriceMiscalculationException("Book price '" + bookPrice
                    + "' and unit price '" + oi.getUnitPrice() + "' does not matched.");
        }
        if (book.getStock() < oi.getQuantity()) {
            log.error("Books stock quantity:{} is lesser to customer required:{} for bookId: {}.",
                    book.getStock(), oi.getQuantity(), book.getId());

            throw new OrderPriceMiscalculationException("Books stock quantity is not reach to " +
                    "your requirement for bookId: " + book.getId());
        }

        // Calculate expected subtotal
        BigDecimal expectedSubtotal = bookPrice
                .multiply(BigDecimal.valueOf(oi.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        // Convert provided subtotal to BigDecimal with 2 decimals
        BigDecimal providedSubtotal = oi.getSubTotal();
        if (providedSubtotal.scale() < 2) {
            providedSubtotal = providedSubtotal.setScale(2, RoundingMode.HALF_UP);
        }

        // Compare both value and scale
        boolean matchedSubtotal = expectedSubtotal.equals(providedSubtotal);

        if (!matchedSubtotal) {
            log.error(
                    "Books at price: {} for unit: {} total will be {} but its sub total showed is {} for bookId: {}.",
                    bookPrice,
                    oi.getQuantity(),
                    expectedSubtotal,
                    oi.getSubTotal(),
                    book.getId());

            throw new OrderPriceMiscalculationException("Books at price: " + bookPrice + " for unit: " +
                    oi.getQuantity() + " total will be " + expectedSubtotal +
                    " but its sub total showed is " + providedSubtotal + " for bookId: " + book.getId());
        }
    }

    private Book resolveBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.error("Book not found.");
                    return new BookNotFoundException("Book not found with Id: " + bookId);
                });
    }

    private Address resolveOrCreateAddress(AddressDto a) {
        Address address = addressRepository.findByAddressDetail(
                a.getLandmark(),
                a.getCity(),
                a.getDistrict(),
                a.getState(),
                a.getCountry(),
                a.getPostalCode()
        ).orElse(null);

        // 4. If Address doesn't exist create it
        if (address == null) {
            if (a.getId() != null) a.setId(null);
            log.info("New address entry created.");
            address = addressRepository.save(addressMapper.toEntity(a));
        }

        return address;
    }

    private void validateOrderDto(OrderDto orderDto) {
        if (orderDto.getId() != null) {
            log.error("OrderId provided in order creating.");
            throw new IllegalArgumentException("OrderId does not required for create order.");
        }
        orderDto.getOrderItems().forEach(oi -> {
            if (oi.getId() != null) {
                log.error("OrderItemId provided in order creating.");
                throw new IllegalArgumentException("OrderItemId does not required for create order.");
            }
        });
    }

    @Override
    @Transactional
    public OrderDto updateOrderSpecific(Map<String, Object> orderMap, Long orderId) {
        Order order = resolveOrder(orderId);

        orderMap.forEach((key, value) -> {
            switch (key) {
                case "status" -> {
                    try {
                        ShippingStatus shippingStatus = ShippingStatus.valueOf((String) value);
                        order.setStatus(shippingStatus);
                    } catch (IllegalArgumentException exception) {
                        String message = value + " -> Invalid shipping status provided";
                        log.error(message);
                        throw new IllegalArgumentException(message);
                    }
                }
                case "address" -> order.setAddress(addressMapper.toEntity((AddressDto) value));
                case "date" -> order.setDate((LocalDate) value);
                case "totalAmount" -> order.setTotalAmount((BigDecimal) value);
                default -> {
                    String message = "Invalid key provided to update Order key: "+key;
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }
            }
        });
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(OrderDto orderDto, Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            String message = "Order not found";
            log.error(message);
            throw new OrderNotFoundException(message+" with orderId: " + orderId);
        }

        Order order = orderMapper.toEntity(orderDto);
        order.setId(orderId);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            String message = "Order does not exist";
            log.error(message);
            throw new OrderNotFoundException(message+" with orderId: " + orderId);
        }
        orderRepository.deleteById(orderId);
        log.info("Order Successfully Deleted.");
    }
}
