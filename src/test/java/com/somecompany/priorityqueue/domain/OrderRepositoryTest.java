package com.somecompany.priorityqueue.domain;

import javax.persistence.PersistenceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository repository;

    @Test
    public void whenFindById_thenReturnOrder() {
        // given a new order
        Order testOrder = new Order();
        testOrder.setIdCust(12345);
        testOrder.setPriority(1);
        testOrder.setDatetime(1000000);
        testOrder.setQuantity(10);
        
        entityManager.persist(testOrder);
        entityManager.flush();

        // when
        Order found = repository.findById(testOrder.getIdCust()).get();

        // then
        Assert.assertEquals(found.getIdCust(), testOrder.getIdCust());
        Assert.assertEquals(found, testOrder);
    }
    
    @Test(expected= PersistenceException.class)
    public void testDuplicateInsertFail() {
        // given an order with an already existent idCust
        Order testOrder = new Order();
        testOrder.setIdCust(1);
        testOrder.setPriority(1);
        testOrder.setDatetime(1000000);
        testOrder.setQuantity(10);
        
        entityManager.persist(testOrder);
        entityManager.flush();
    }
}
