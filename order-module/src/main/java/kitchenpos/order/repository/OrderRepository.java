package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTable(Long orderTableId);
}
