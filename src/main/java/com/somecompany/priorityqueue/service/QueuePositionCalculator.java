package com.somecompany.priorityqueue.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for calculation of queue positions and approximate wait times.
 * 
 * @author hweitekamp
 */
@Service
public class QueuePositionCalculator {

    @Value("${orderqueue.max_delivery_size}")
    private int maxDeliverySize;
    @Value("${orderqueue.delivery_period_minutes}")
    private int deliveryPeriod;

    public List<OrderResponseDTO> process(List<OrderResponseDTO> orderDtos) {
        int currentDeliverySize = 0;
        int deliveryNumber = 0;
        int positionInQueue = 0;

        for (OrderResponseDTO o : orderDtos) {
            currentDeliverySize += o.getQuantity();
            if (currentDeliverySize > maxDeliverySize) {
                currentDeliverySize = 0;
                deliveryNumber++;
            }
            positionInQueue++;

            o.setPositionInQueue(positionInQueue);
            o.setApproximateWaitTimeMinutes(deliveryNumber * deliveryPeriod);
        }

        return orderDtos;
    }
}
