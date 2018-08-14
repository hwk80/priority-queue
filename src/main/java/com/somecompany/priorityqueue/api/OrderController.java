package com.somecompany.priorityqueue.api;

import com.somecompany.priorityqueue.service.OrderRequestDTO;
import com.somecompany.priorityqueue.service.OrderResponseDTO;
import com.somecompany.priorityqueue.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for the REST interface to the priority queue.
 *
 * @author hweitekamp
 */
@RestController
@Api(description = "Operations on the Rubber Duck Order Queue")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    @ApiOperation(value = "Insert a new order into the queue. Only one order per customer can be in the queue at any time.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully inserted order."),
        @ApiResponse(code = 400, message = "An order is already queued for this customer.")
    })
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderRequestDTO order) {
        orderService.save(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + order.getIdCust())
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/orders/{idCusts}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete one or several orders from the queue.", 
            notes="Use a comma separated list for multiple ids.")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the order.")})
    public void deleteOrder(@PathVariable List<Integer> idCusts) {
        idCusts.forEach(orderService::deleteById);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "View a list of orders with queue position and approximate wait time.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list.")})
    public List<OrderResponseDTO> findAll() {
        return orderService.getAllOrdersSorted();
    }

    @GetMapping("/orders/{idCust}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "View an order with queue position and approximate wait time.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved order."),
        @ApiResponse(code = 404, message = "No order found for the specified customer.")})
    public OrderResponseDTO findById(@PathVariable int idCust) {
        return orderService.findById(idCust);
    }

    @GetMapping("/orders/nextDelivery")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Retrieve a list of orders for the next delivery.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list.")})
    public List<OrderResponseDTO> getNextDelivery() {
        return orderService.getNextDelivery();
    }

    @ExceptionHandler({NoSuchElementException.class,
        EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "No order found for this customer.")
    public void handleResourceNotFoundException() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "An order is already queued for this customer.")
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleDuplicateKeyException() {
    }

}
