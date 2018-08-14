package com.somecompany.priorityqueue.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.priorityqueue.PriorityQueueApplication;
import com.somecompany.priorityqueue.service.OrderDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PriorityQueueApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    private static final int TEST_ID = 12345;
    private final ObjectMapper jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetOrders() throws Exception {
        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].idCust", is(2)));
    }

    @Test
    public void testGetSpecificOrder() throws Exception {
        mvc.perform(get("/orders/" + 1001))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idCust", is(1001)))
                .andExpect(jsonPath("quantity", is(5)))
                .andExpect(jsonPath("positionInQueue", is(3)))
                .andExpect(jsonPath("approximateWaitTimeMinutes", is(0)));
    }

    @Test
    public void testGetSpecificOrderFail() throws Exception {
        mvc.perform(get("/orders/" + TEST_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAndDeleteOrder() throws Exception {
        OrderDTO testOrder = new OrderDTO(TEST_ID, 10);

        mvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testOrder))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        is("http://localhost/orders/" + TEST_ID)));

        mvc.perform(get("/orders/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idCust", is(TEST_ID)))
                .andExpect(jsonPath("quantity", is(10)))
                .andExpect(jsonPath("positionInQueue", is(7)))
                .andExpect(jsonPath("approximateWaitTimeMinutes", is(5)));

        mvc.perform(delete("/orders/" + TEST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetNextDelivery() throws Exception {
        mvc.perform(get("/orders/nextDelivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].idCust", is(2)));
    }
}
