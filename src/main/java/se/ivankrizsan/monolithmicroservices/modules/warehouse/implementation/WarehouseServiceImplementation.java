package se.ivankrizsan.monolithmicroservices.modules.warehouse.implementation;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.domain.Product;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.domain.ProductReservation;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.exceptions.ProductNotInWarehouseException;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductReservationRepository;

import java.util.Optional;

/**
 * Implementation of the {@link WarehouseService}.
 */
@RequiredArgsConstructor
public class WarehouseServiceImplementation implements WarehouseService {
    /* Constant(s): */

    /* Instance variable(s): */
    @NonNull
    protected ProductRepository mProductRepository;
    @NonNull
    protected ProductReservationRepository mProductReservationRepository;

    @Override
    public Optional<Double> retrieveProductAvailableAmount(final String inProductNumber) {
        final Optional<Product> theProductOptional =  mProductRepository.findByProductNumber(inProductNumber);
        if (theProductOptional.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(theProductOptional.get().availableAmount());
        }
    }

    @Override
    @Transactional
    public Optional<Long> reserveProduct(final String inProductNumber, final double inAmount) {
        final Optional<Product> theProductOptional =  mProductRepository.findByProductNumber(inProductNumber);
        if (theProductOptional.isEmpty()) {
            /* No matching product. */
            return Optional.empty();
        } else if (theProductOptional.get().availableAmount() < inAmount){
            /* Insufficient product amount available - cannot reserve. */
            return Optional.empty();
        } else {
            /* Reduce the available amount and increase the reserved amount. */
            Product theProductToReserve = theProductOptional.get();
            final double theNewAvailableAmount = theProductToReserve.availableAmount() - inAmount;
            final double theNewReservedAmount = theProductToReserve.reservedAmount() + inAmount;
            theProductToReserve.availableAmount(theNewAvailableAmount);
            theProductToReserve.reservedAmount(theNewReservedAmount);
            theProductToReserve = mProductRepository.save(theProductToReserve);

            /* Create a product reservation for the amount. */
            final ProductReservation theProductReservation = new ProductReservation(inProductNumber, inAmount);
            mProductReservationRepository.save(theProductReservation);

            return Optional.of(theProductReservation.getId());
        }
    }

    @Override
    public void createProductInWarehouse(final String inProductNumber, final String inProductName) {
        final Product theNewProduct = new Product()
            .productNumber(inProductNumber)
            .name(inProductName)
            .availableAmount(0)
            .reservedAmount(0);

        if (!mProductRepository.existByProductNumber(inProductNumber)) {
            mProductRepository.save(theNewProduct);
        }
    }

    @Override
    public void increaseProductStock(final String inProductNumber, final double inAmount)
        throws ProductNotInWarehouseException {

    }
}
