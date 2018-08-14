package com.somecompany.priorityqueue.service;

/**
 * Data transfer object for REST API responses.
 *
 * @author hweitekamp
 */
public class OrderResponseDTO {

    private int idCust;
    private int quantity;
    private int positionInQueue;
    private int approximateWaitTimeMinutes;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(int idCust, int quantity) {
        this.idCust = idCust;
        this.quantity = quantity;
    }

    public int getApproximateWaitTimeMinutes() {
        return approximateWaitTimeMinutes;
    }

    public void setApproximateWaitTimeMinutes(int approximateWaitTimeMinutes) {
        this.approximateWaitTimeMinutes = approximateWaitTimeMinutes;
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

    public int getPositionInQueue() {
        return positionInQueue;
    }

    public void setPositionInQueue(int positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

}
