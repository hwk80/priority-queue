package com.somecompany.priorityqueue.service;

import java.util.List;
import com.somecompany.priorityqueue.domain.Order;
import com.somecompany.priorityqueue.domain.OrderRepository;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
    "orderqueue.max_delivery_size=25","orderqueue.max_priority_id_cust=999"
})
public class OrderServiceImplTest {

    private static final int TEST_ID = 12345;
    
    @TestConfiguration
    static class OrderServiceImplTestContextConfiguration {

        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl();
        }
    }

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private QueuePositionCalculator queuePositionCalculator;

    @Before
    public void setUp() {
        Order testOrder = new Order();
        testOrder.setIdCust(TEST_ID);
        testOrder.setPriority(1);
        testOrder.setDatetime(1000000);
        testOrder.setQuantity(10);
        
        Mockito.when(orderRepository.findById(TEST_ID))
                .thenReturn(Optional.of(testOrder));

        Mockito.when(orderRepository
                .findAllByOrderByPriorityDescDatetimeAscIdCust(Mockito.any()))
                .thenReturn(Collections.singletonList(testOrder));

        Mockito.when(queuePositionCalculator.process(Mockito.any(List.class)))
                .thenAnswer(//return same object
                        (InvocationOnMock mock) -> mock.getArguments()[0]
                );
    }
    
    @Test
    public void testSave() {
        orderService.save(new OrderRequestDTO(TEST_ID, 10));
        
        verify(orderRepository, times(1))
                .save(Mockito.any());
    }
    
    @Test
    public void testDeleteById() {
        orderService.deleteById(TEST_ID);
        
        verify(orderRepository, times(1))
                .findById(Mockito.any());
        verify(orderRepository, times(1))
                .delete(Mockito.any(Order.class));
    }
    
    @Test
    public void testDeleteByIdFail() {
        orderService.deleteById(54321);
        
        verify(orderRepository, times(1))
                .findById(Mockito.any());
        verify(orderRepository, never())
                .delete(Mockito.any(Order.class));
    }
    
    @Test
    public void testGetPageableOrdersSorted() {
        List<OrderResponseDTO> result = orderService.getPageableOrdersSorted(0, 25);
        
        verify(orderRepository, times(1))
                .findAllByOrderByPriorityDescDatetimeAscIdCust(Mockito.any());
        verify(queuePositionCalculator, times(1))
                .process(Mockito.any(List.class));
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(TEST_ID, result.get(0).getIdCust());
    }
    
    @Test
    public void testGetAllOrdersSorted() {
        List<OrderResponseDTO> result = orderService.getAllOrdersSorted();
        
        verify(orderRepository, times(1))
                .findAllByOrderByPriorityDescDatetimeAscIdCust(Mockito.any());
        verify(queuePositionCalculator, times(1))
                .process(Mockito.any(List.class));
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(TEST_ID, result.get(0).getIdCust());
    }

    @Test
    public void testFindById() {
        OrderResponseDTO found = orderService.findById(TEST_ID);

        Assert.assertEquals(TEST_ID, found.getIdCust());
        verify(orderRepository, times(1))
                .findAllByOrderByPriorityDescDatetimeAscIdCust(Mockito.any());
        verify(queuePositionCalculator, times(1))
                .process(Mockito.any(List.class));
    }
    
    @Test(expected = NoSuchElementException.class) 
    public void testFindByIdFail() {
        // search for unavailable id
        orderService.findById(54321);
    }
    
    @Test
    public void testGetNextDelivery() {
        List<OrderResponseDTO> result = orderService.getNextDelivery();
        
        verify(orderRepository, times(1))
                .findAllByOrderByPriorityDescDatetimeAscIdCust(Mockito.any());
        verify(queuePositionCalculator, times(1))
                .process(Mockito.any(List.class));
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(TEST_ID, result.get(0).getIdCust());
    }

}
