package com.somecompany.priorityqueue.service;

import java.util.List;

/**
 *
 * @author hweitekamp
 */
public interface OrderService {

    public OrderDTO save(OrderDTO order);

    public void deleteById(int idCust);

    public List<OrderDTO> getPageableOrdersSorted(int page, int size);

    public List<OrderDTO> getAllOrdersSorted();

    public OrderDTO findById(int idCust);

    public List<OrderDTO> getNextDelivery();

}
