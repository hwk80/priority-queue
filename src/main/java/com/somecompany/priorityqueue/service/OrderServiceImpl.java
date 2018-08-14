package com.somecompany.priorityqueue.service;

import com.somecompany.priorityqueue.domain.Order;
import com.somecompany.priorityqueue.domain.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implementation of the service interface encapsulating the business logic.
 * 
 * @author hweitekamp
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderRepository repository;
    @Autowired
    private QueuePositionCalculator queuePositionCalculator;

    @Value("${orderqueue.max_delivery_size}")
    private int maxDeliverySize;
    @Value("${orderqueue.max_priority_id_cust}")
    private int maxPrioritizedIdCust;

    @Override
    public void save(OrderRequestDTO orderDto) {
        repository.save(convertToOrder(orderDto));
    }

    @Override
    public void deleteById(int idCust) {
        try {
            Order entity = repository.findById(idCust).get();
            repository.delete(entity);
        } catch (NoSuchElementException | EmptyResultDataAccessException e) {
            LOG.warn("Could not delete. Order not found for customer " 
                    + idCust);
        }
    }

    @Override
    public List<OrderResponseDTO> getPageableOrdersSorted(int pageNumber, int size) {
        final PageRequest page = PageRequest.of(pageNumber, size);

        List<OrderResponseDTO> orders = convertToDTO(
                repository.findAllByOrderByPriorityDescDatetimeAscIdCust(page)
        );

        return queuePositionCalculator.process(orders);
    }

    @Override
    public List<OrderResponseDTO> getAllOrdersSorted() {
        return getPageableOrdersSorted(0, Integer.MAX_VALUE);
    }

    @Override
    public OrderResponseDTO findById(int idCust) {
        return getAllOrdersSorted().stream()
                .filter(o -> idCust == (o.getIdCust()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }

    @Override
    public List<OrderResponseDTO> getNextDelivery() {
        final List<OrderResponseDTO> orders = new ArrayList<>();
        final AtomicInteger deliverySize = new AtomicInteger();

        getPageableOrdersSorted(0, maxDeliverySize).forEach(o -> {
            if (deliverySize.addAndGet(o.getQuantity()) > maxDeliverySize) {
                return;
            }
            orders.add(o);
        });

        return orders;
    }

    private Order convertToOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setIdCust(dto.getIdCust());
        order.setQuantity(dto.getQuantity());
        order.setDatetime(System.currentTimeMillis());
        order.setPriority((dto.getIdCust() <= maxPrioritizedIdCust) ? 1 : 0);

        return order;
    }

    private List<OrderResponseDTO> convertToDTO(List<Order> orders) {
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO convertToDTO(Order order) {
        return new OrderResponseDTO(order.getIdCust(), order.getQuantity());
    }

}
