package com.somecompany.priorityqueue.api;

import java.util.List;
import com.somecompany.priorityqueue.service.OrderDTO;
import com.somecompany.priorityqueue.service.OrderService;
import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private static final int TEST_ID = 12345;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    @Test
    public void givenOrders_whenGetOrders_thenReturnJsonArray() throws Exception {
        OrderDTO testOrder = new OrderDTO(TEST_ID, 10);

        List<OrderDTO> allOrders = Arrays.asList(testOrder);

        given(service.getAllOrdersSorted()).willReturn(allOrders);

        mvc.perform(get("/orders")
        //                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idCust", is(TEST_ID)));
    }
}
