package com.somecompany.priorityqueue.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    public List<Order> findAllByOrderByPriorityDescDatetimeAscIdCust();

    public List<Order> findAllByOrderByPriorityDescDatetimeAscIdCust(Pageable pageable);
    
}
