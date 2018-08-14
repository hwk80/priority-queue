package com.somecompany.priorityqueue.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.priorityqueue.service.OrderRequestDTO;
import java.util.List;
import com.somecompany.priorityqueue.service.OrderResponseDTO;
import com.somecompany.priorityqueue.service.OrderService;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private static final int TEST_ID = 12345;  
    private final ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    @Test
    public void testGetOrders() throws Exception {
        OrderResponseDTO testOrder = new OrderResponseDTO(TEST_ID, 10);
        OrderResponseDTO testOrder2 = new OrderResponseDTO(TEST_ID + 1, 25);

        List<OrderResponseDTO> allOrders = Arrays.asList(testOrder, testOrder2);

        given(service.getAllOrdersSorted()).willReturn(allOrders);

        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idCust", is(TEST_ID)));
    }

    @Test
    public void testGetSpecificOrder() throws Exception {
        OrderResponseDTO testOrder = new OrderResponseDTO(TEST_ID, 10);

        given(service.findById(TEST_ID)).willReturn(testOrder);

        mvc.perform(get("/orders/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idCust", is(TEST_ID)))
                .andExpect(jsonPath("quantity", is(10)))
                .andExpect(jsonPath("positionInQueue", is(0)))
                .andExpect(jsonPath("approximateWaitTimeMinutes", is(0)));
    }

    @Test
    public void testGetSpecificOrderFail() throws Exception {
        given(service.findById(TEST_ID)).willThrow(NoSuchElementException.class);

        mvc.perform(get("/orders/" + TEST_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        mvc.perform(delete("/orders/" + TEST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateOrder() throws Exception {
        OrderRequestDTO testOrder = new OrderRequestDTO(TEST_ID, 10);

        mvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testOrder))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", 
                        is("http://localhost/orders/" + TEST_ID)))
                ;
    }
}
