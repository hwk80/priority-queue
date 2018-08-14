package com.somecompany.priorityqueue.api;

import com.somecompany.priorityqueue.service.OrderDTO;
import com.somecompany.priorityqueue.service.OrderService;
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
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO order) {
        order = orderService.save(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + order.getIdCust())
                .build().toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/orders/{idCusts}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable List<Integer> idCusts) {
        idCusts.forEach(orderService::deleteById);
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OrderDTO> findAll() {
        return orderService.getAllOrdersSorted();
    }

    @GetMapping("/orders/{idCust}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderDTO findById(@PathVariable int idCust) {
        return orderService.findById(idCust);
    }

    @GetMapping("/orders/nextDelivery")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OrderDTO> getNextDelivery() {
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
