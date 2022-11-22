package se.ivankrizsan.monolithmicroservices.modules.shoppingcart.implementation;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.ivankrizsan.monolithmicroservices.modules.shoppingcart.api.ShoppingCartService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;

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
}
