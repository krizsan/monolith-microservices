package se.ivankrizsan.monolithmicroservices.modules.order.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import se.ivankrizsan.monolithmicroservices.modules.order.api.OrderService;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.Order;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.OrderItem;
import se.ivankrizsan.monolithmicroservices.modules.order.persistence.OrderRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;

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
    public Optional<String> createOrder(final String inPaymentId) {
        Assert.hasText(inPaymentId, "A payment id is required");

        Order theNewOrder = new Order(inPaymentId);
        theNewOrder = mOrderRepository.saveAndFlush(theNewOrder);
        return Optional.of(theNewOrder.getId().toString());
    }

    @Override
    public boolean addItemToOrder(final String inOrderId, final String inProductNumber, final Long inReservationId) {
        Assert.hasText(inOrderId, "An order id is required");
        Assert.hasText(inProductNumber, "A product number is required");
        Assert.notNull(inReservationId, "A product reservation id is required");

        final Optional<Order> theOrderOptional = mOrderRepository.findById(Long.parseLong(inOrderId));
        if (theOrderOptional.isPresent()) {
            /* Verify that there is a reservation with the supplied id. */
            final Optional<Double> theReservationAmountOptional =
                mWarehouseService.retrieveReservationAmount(inReservationId);
            if (theReservationAmountOptional.isPresent()) {
                final Order theOrder = theOrderOptional.get();
                final OrderItem theOrderItem = new OrderItem(inProductNumber, inReservationId);
                theOrder.addOrderItem(theOrderItem);
                mOrderRepository.saveAndFlush(theOrder);
                return true;
            }
        }

        /* No order available with the supplied order id or no matching reservation for the product. */
        return false;
    }
}
