package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderService orderService;
    private final TableGroupService tableGroupService;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderService orderService,
        final TableGroupService tableGroupService,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderService = orderService;
        this.tableGroupService = tableGroupService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.save(orderTableRequest.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = orderService.findOrderTableById(orderTableId);
        savedOrderTable.updateEmpty(orderTableRequest.getEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        orderTableRequest.validateNumberOfGuests();
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderService.findOrderTableById(orderTableId);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }

    public OrderTables findAllByIdIn(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        return new OrderTables(orderTables);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}