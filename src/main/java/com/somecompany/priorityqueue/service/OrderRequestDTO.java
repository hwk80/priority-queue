package com.somecompany.priorityqueue.service;

import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Data transfer object for REST API requests.
 *
 * @author hweitekamp
 */
public class OrderRequestDTO {

    @Id
    @DecimalMin(value = "1", message = "Invalid customer id. Minimum value is 1.")
    @DecimalMax(value = "20000", message = "Invalid customer id. Maximum value is 20000.")
    private int idCust;

    @NotNull
    @DecimalMin(value = "1", message = "Invalid quantity. Minimum value is 1.")
    @DecimalMax(value = "25", message = "Invalid quantity. Maximum value is 25.")
    private int quantity;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(int idCust, int quantity) {
        this.idCust = idCust;
        this.quantity = quantity;
    }

    public int getIdCust() {
        return idCust;
    }

    public void setIdCust(int idCust) {
        this.idCust = idCust;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
