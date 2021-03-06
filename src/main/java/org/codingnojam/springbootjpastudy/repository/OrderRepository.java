package org.codingnojam.springbootjpastudy.repository;

import lombok.RequiredArgsConstructor;
import org.codingnojam.springbootjpastudy.domain.Order;
import org.codingnojam.springbootjpastudy.repository.orderqeury.OrderQueryDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    // 페이징을 시도하면 메모리에서 페이징을 하므로 페이징 사용 하면 안됨
    public List<Order> findAllWithOrderItem() {
        return em.createQuery("select distinct o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d " +
                "join fetch o.orderItems oi " +
                "join fetch oi.item i", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithOrderItemJoinTest() {
        return em.createQuery("select distinct o from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", Order.class)
                .getResultList();
    }
}
