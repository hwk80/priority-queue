package com.somecompany.priorityqueue.service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service interface providing access to the domain layer..
 *
 * @author hweitekamp
 */
public interface OrderService {

    /**
     * Place a single Order in the priority queue.
     *
     * @param order
     * @return the saved order
     */
    public OrderDTO save(OrderDTO order);

    /**
     * Delete a single Order from the queue.
     *
     * @param idCust
     */
    public void deleteById(int idCust);

    /**
     * Get a sublist of all orders in the queue with pagination.
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<OrderDTO> getPageableOrdersSorted(int pageNumber, int pageSize);

    /**
     * Get a list of all orders in the queue.
     *
     * @return
     */
    public List<OrderDTO> getAllOrdersSorted();

    /**
     * Get a single order from the queue for a specified customer.
     *
     * @param idCust
     * @throws NoSuchElementException if there is no Order for this customer in
     * the queue.
     * @return The found order with queue position and wait time.
     */
    public OrderDTO findById(int idCust);

    /**
     * Get the orders for the next delivery.
     *
     * @return The list of orders for the next delivery.
     */
    public List<OrderDTO> getNextDelivery();

}
