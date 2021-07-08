package kitchenpos.application;

import kitchenpos.menu.domain.*;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    MenuGroup savedMenuGroup;

    Menu savedMenu;

    OrderTable savedOrderTable;

    OrderTable savedEmptyOrderTable;

    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("패스트푸드");
        savedMenuGroup = menuGroupRepository.save(menuGroup);

        Menu menu = new Menu("맥도날드햄버거", BigDecimal.valueOf(10000), savedMenuGroup);

        savedMenu = menuRepository.save(menu);

        OrderTable orderTable = new OrderTable(2, false);

        savedOrderTable = orderTableRepository.save(orderTable);

        OrderTable emptyOrderTable = new OrderTable(0, true);

        savedEmptyOrderTable = orderTableRepository.save(emptyOrderTable);

        orderLineItem = new OrderLineItem(savedMenu.getId(), new Quantity(1));
    }

    @DisplayName("주문을 생성하자")
    @Test
    public void createOrder() throws Exception {
        //given
        OrderStatus orderStatus = OrderStatus.COOKING;

        OrderRequest order = new OrderRequest(savedOrderTable.getId(), orderStatus, Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), null, orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        OrderResponse savedOrder = orderService.create(order);

        //then
        assertNotNull(savedOrder.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 목록을 출력해보자자")
    @Test
    public void listMenuGroup() throws Exception {
        //given
        LocalDateTime orderedTime = LocalDateTime.of(2021, 7, 1, 01, 10, 00);

        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, orderedTime, Arrays.asList(orderLineItem));
        order.reception();
        Order savedOrder = orderRepository.save(order);

        //when
        List<OrderResponse> orders = orderService.list();
        List<Long> findOrderIds = orders.stream()
                .map(findOrder -> findOrder.getId())
                .collect(Collectors.toList());

        //then
        assertNotNull(orders);
        assertTrue(findOrderIds.contains(savedOrder.getId()));
    }

    @DisplayName("메뉴가 없을때는 주문생성에 실패한다.")
    @Test
    public void failCreateOrderEmptyOrderLineItems() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 번호가 존재하지 않을경우 주문생성에 실패한다.")
    @Test
    public void failCreateOrderInvalidOrderTableId() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest(0L, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), null, orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 비어있는 경우에 주문생성에 실패한다.")
    @Test
    public void failCreateOrderEmptyOrderTableId() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest(savedEmptyOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), null, orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경해보자")
    @Test
    public void changeOrderStatus() throws Exception {
        //given
        LocalDateTime orderedTime = LocalDateTime.of(2021, 7, 1, 01, 10, 00);

        OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), new Quantity(1));
        Order order = new Order(savedOrderTable.getId(), OrderStatus.COOKING, orderedTime, Arrays.asList(orderLineItem));
        order.reception();

        Order savedOrder = orderRepository.save(order);


        OrderStatus orderStatusMeal = OrderStatus.MEAL;

        OrderRequest changeOrder = new OrderRequest(savedOrderTable.getId(), orderStatusMeal, orderedTime);

        //when
        OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrder);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatusMeal);
        assertThat(changedOrder.getOrderedTime()).isEqualTo(orderedTime);
    }

    @DisplayName("존재하지 않는 주문을 변경할수는 없다.")
    @Test
    public void failChangeOrderStatusNotExistOrderId() throws Exception {
        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(0L, new OrderRequest())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 상태를 변경할수는 없다.")
    @Test
    public void couldNotChangeOrderStatus() throws Exception {
        // given
        Order order = new Order(savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList(orderLineItem));
        order.reception();
        Order savedOrder = orderRepository.save(order);

        OrderRequest changeOrder = new OrderRequest(savedOrderTable.getId(), OrderStatus.MEAL);

        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(savedOrder.getId(), changeOrder)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}