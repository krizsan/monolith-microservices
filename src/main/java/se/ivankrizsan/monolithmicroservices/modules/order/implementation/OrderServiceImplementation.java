package se.ivankrizsan.monolithmicroservices.modules.order.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import se.ivankrizsan.monolithmicroservices.modules.order.api.OrderService;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.Order;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.OrderItem;
import se.ivankrizsan.monolithmicroservices.modules.order.persistence.OrderRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link OrderService}.
 *
 * @author Ivan Krizsan
 */
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    @NonNull
    protected OrderRepository mOrderRepository;
    @NonNull
    protected WarehouseService mWarehouseService;

    @Override
    public Optional<Long> createOrder(final String inPaymentId) {
        Assert.hasText(inPaymentId, "A payment id is required");

        Order theNewOrder = new Order(inPaymentId);
        theNewOrder = mOrderRepository.saveAndFlush(theNewOrder);
        return Optional.of(theNewOrder.getId());
    }

    @Override
    public void deleteOrder(final Long inOrderId) {
        mOrderRepository.deleteById(inOrderId);
    }

    @Override
    public boolean addItemsToOrder(final Long inOrderId, final String inProductNumber,
                                   final List<Long> inReservationIds) {
        Assert.notNull(inOrderId, "An order id is required");
        Assert.hasText(inProductNumber, "A product number is required");
        Assert.notEmpty(inReservationIds, "One or more product reservations are required");

        final Optional<Order> theOrderOptional = mOrderRepository.findById(inOrderId);
        if (theOrderOptional.isPresent()) {
            for (Long theReservationId : inReservationIds) {
                /* Verify that there is a reservation with the supplied id. */
                final Optional<Double> theReservationAmountOptional =
                    mWarehouseService.retrieveReservationAmount(theReservationId);
                if (theReservationAmountOptional.isPresent()) {
                    final Order theOrder = theOrderOptional.get();
                    final OrderItem theOrderItem = new OrderItem(inProductNumber, theReservationId);
                    theOrder.addOrderItem(theOrderItem);
                    mOrderRepository.saveAndFlush(theOrder);
                } else {
                    throw new IllegalStateException("No reservation found for id " + theReservationId);
                }
            }
            return true;
        }

        /* No order available with the supplied order id. */
        return false;
    }
}
