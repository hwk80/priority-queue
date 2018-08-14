package com.somecompany.priorityqueue.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

/**
 * Entity class for a single order in the priority queue.
 * 
 * @author hweitekamp
 */
@Entity
@Table(name = "orders")
public class Order implements Persistable, Serializable {

    @Id
    private int idCust;

    @NotNull
    private int quantity;
    @NotNull
    private int priority;
    @NotNull
    private long datetime;

    public Order() {
    }

    public int getIdCust() {
        return idCust;
    }

    public void setIdCust(int idCust) {
        this.idCust = idCust;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Object getId() {
        return idCust;
    }

    @Override
    public boolean isNew() {
        return true; // forces inserts instead of updates
    }

}
