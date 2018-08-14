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
     */
    public void save(OrderRequestDTO order);

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
    public List<OrderResponseDTO> getPageableOrdersSorted(int pageNumber, int pageSize);

    /**
     * Get a list of all orders in the queue.
     *
     * @return
     */
    public List<OrderResponseDTO> getAllOrdersSorted();

    /**
     * Get a single order from the queue for a specified customer.
     *
     * @param idCust
     * @throws NoSuchElementException if there is no Order for this customer in
     * the queue.
     * @return The found order with queue position and wait time.
     */
    public OrderResponseDTO findById(int idCust);

    /**
     * Get the orders for the next delivery.
     *
     * @return The list of orders for the next delivery.
     */
    public List<OrderResponseDTO> getNextDelivery();

}
