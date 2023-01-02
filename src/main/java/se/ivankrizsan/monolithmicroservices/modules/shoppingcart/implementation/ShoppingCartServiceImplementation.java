package se.ivankrizsan.monolithmicroservices.modules.shoppingcart.implementation;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.ivankrizsan.monolithmicroservices.modules.order.api.OrderService;
import se.ivankrizsan.monolithmicroservices.modules.shoppingcart.api.ShoppingCartService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link ShoppingCartService}.
 *
 * @author Ivan Krizsan
 */
@RequiredArgsConstructor
public class ShoppingCartServiceImplementation implements ShoppingCartService {
    /** Warehouse from which items placed in the shopping cart will be taken. */
    @NonNull
    protected WarehouseService mWarehouseService;
    /** Service handling orders. */
    @NonNull
    protected OrderService mOrderService;
    /** Product reservations associated with the shopping cart. */
    protected MultiValueMap<String, Long> mProductReservationIds = new LinkedMultiValueMap<>() {};

    @Transactional
    @Override
    public boolean addItemToCart(final String inProductNumber, final double inAmount) {
        final Optional<Long> theReservationIdOptional = mWarehouseService.reserveProduct(inProductNumber, inAmount);
        if (theReservationIdOptional.isPresent()) {
            mProductReservationIds.add(inProductNumber, theReservationIdOptional.get());
            return true;
        }

        return false;
    }

    @Override
    public void emptyCart() {
        mProductReservationIds.clear();
    }

    @Override
    public Double calculateCartPrice() {
        double theCartPrice = 0.0;

        for (Map.Entry<String, List<Long>> theProductIdReservationsEntry : mProductReservationIds.entrySet()) {
            final String theProductNumber = theProductIdReservationsEntry.getKey();
            final Optional<Double> theProductPriceOptional = mWarehouseService.retrieveProductUnitPrice(theProductNumber);

            if (theProductPriceOptional.isPresent()) {
                final Double theProductPrice = theProductPriceOptional.get();

                final List<Long> theProductReservations = theProductIdReservationsEntry.getValue();
                for (Long theProductReservation : theProductReservations) {
                    final Optional<Double> theReservationAmountOptional =
                        mWarehouseService.retrieveReservationAmount(theProductReservation);
                    if (theReservationAmountOptional.isPresent()) {
                        theCartPrice = theCartPrice + (theProductPrice * theReservationAmountOptional.get());
                    }
                }
            } else {
                throw new RuntimeException("No price found for product with number " + theProductNumber);
            }
        }
        return theCartPrice;
    }

    @Override
    public Optional<Long> placeOrder(String inPaymentId) {
        /* No meaning in placing an order with an empty shopping cart. */
        if (mProductReservationIds.isEmpty()) {
            return Optional.empty();
        }
        /* The order must be paid for prior to placing it. */
        Assert.hasText(inPaymentId, "A payment id is required");

        final Optional<Long> theOrderIdOptional = mOrderService.createOrder(inPaymentId);
        if (theOrderIdOptional.isPresent()) {
            final Long theOrderId = theOrderIdOptional.get();
            for (String theProductNumber : mProductReservationIds.keySet()) {
                final List<Long> theReservationIds = mProductReservationIds.get(theProductNumber);
                final boolean theSuccessFlag = mOrderService.addItemsToOrder(theOrderId, theProductNumber,
                    theReservationIds);
                if (!theSuccessFlag) {
                    mOrderService.deleteOrder(theOrderId);
                    return Optional.empty();
                }
            }

            return Optional.of(theOrderId);
        }

        return Optional.empty();
    }
}
