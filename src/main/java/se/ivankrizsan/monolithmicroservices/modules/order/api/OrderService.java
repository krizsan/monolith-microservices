package se.ivankrizsan.monolithmicroservices.modules.order.api;

import java.util.List;
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
    Optional<Long> createOrder(String inPaymentId);

    /**
     * Deletes the order with the supplied id.
     * Does nothing if no corresponding order exists.
     *
     * @param inOrderId Id of order to delete.
     */
    void deleteOrder(Long inOrderId);

    /**
     * Adds the item with the supplied product number for which there is one or more reservations with the supplied
     * reservation ids to the order with the supplied order id.
     * Will fail if there is no existing order with the supplied id or if there is no product reservation
     * with the supplied id.
     *
     * @param inOrderId Id of order to which the item is to be added.
     * @param inProductNumber Product number of the item to be added to the order.
     * @param inReservationIds Ids of the product reservations.
     * @return True if items were successfully added to the order, false otherwise.
     */
    boolean addItemsToOrder(Long inOrderId, String inProductNumber, List<Long> inReservationIds);
}
