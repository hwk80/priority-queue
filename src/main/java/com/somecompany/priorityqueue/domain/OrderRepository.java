package com.somecompany.priorityqueue.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository providing access to the priority queue implementation.
 * 
 * @author hweitekamp
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    public List<Order> findAllByOrderByPriorityDescDatetimeAscIdCust();

    public List<Order> findAllByOrderByPriorityDescDatetimeAscIdCust(Pageable pageable);
    
}
