package org.codingnojam.springbootjpastudy.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.codingnojam.springbootjpastudy.domain.Address;
import org.codingnojam.springbootjpastudy.domain.Order;
import org.codingnojam.springbootjpastudy.domain.OrderItem;
import org.codingnojam.springbootjpastudy.domain.OrderStatus;
import org.codingnojam.springbootjpastudy.repository.OrderRepository;
import org.codingnojam.springbootjpastudy.repository.orderqeury.OrderCollectionQueryDto;
import org.codingnojam.springbootjpastudy.repository.orderqeury.OrderQueryRepository;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderCollectionApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/collection/v1/orders")
    public List<OrderCollectionDto> ordersV1() {
        return orderRepository.findAll().stream()
                .map(o -> new OrderCollectionDto(o))
                .collect(Collectors.toList());
    }


    @GetMapping("/api/collection/v2/orders")
    public List<OrderCollectionDto> ordersV2() {
        List<Order> orders = orderRepository.findAllWithOrderItem();
        return orders.stream().map(o -> new OrderCollectionDto(o)).collect(Collectors.toList());
    }

    @GetMapping("/api/collection/v2/orders/2")
    public List<OrderCollectionDto> ordersV2JoinTest() {
        List<Order> orders = orderRepository.findAllWithOrderItemJoinTest();
        return orders.stream().map(o -> new OrderCollectionDto(o)).collect(Collectors.toList());
    }

    //@BatchSize(size = 100)
    @GetMapping("/api/collection/v3/orders")
    public List<OrderCollectionDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new OrderCollectionDto(o)).collect(Collectors.toList());
    }

    @GetMapping("/api/collection/v4/orders")
    public List<OrderCollectionQueryDto> ordersV4(){
        return orderQueryRepository.findOrderCollections();
    }

    @GetMapping("/api/collection/v5/orders")
    public List<OrderCollectionQueryDto> ordersV5() {
        return orderQueryRepository.findOrderCollectionsOptimize();
    }


    @Data
    static class OrderCollectionDto {
        private Long id;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItemDtos;

        public OrderCollectionDto(Order order) {
            this.id = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItemDtos = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String name;
        private int count;
        private int price;

        public OrderItemDto(OrderItem orderItem){
            this.name = orderItem.getItem().getName();
            this.count = orderItem.getCount();
            this.price = orderItem.getOrderPrice();

        }
    }

}
