package se.ivankrizsan.monolithmicroservices.modules.order.api;

import java.util.Optional;

/**
 * Order service handles orders.
 *
 * @author Ivan Krizsan
 */
public interface OrderService {

    /**
     * Creates a new order that has been paid for as indicated by the supplied payment id.
     *
     * @param inPaymentId Id of payment having paid for the order.
     * @return Order id of the new order or empty if no order created.
     */
    Optional<String> createOrder(String inPaymentId);

    /**
     * Adds the item with the supplied product number for which there is a reservation with the supplied
     * reservation id to the order with the supplied order id.
     * Will fail if there is no existing order with the supplied id or if there is no product reservation
     * with the supplied id.
     *
     * @param inOrderId Id of order to which the item is to be added.
     * @param inProductNumber Product number of the item to be added to the order.
     * @param inReservationId Id of the product reservation.
     * @return True if item was successfully added to the order, false otherwise.
     */
    boolean addItemToOrder(String inOrderId, String inProductNumber, Long inReservationId);
}
