package se.ivankrizsan.monolithmicroservices.warehouse.implementation;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.ivankrizsan.monolithmicroservices.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.warehouse.domain.Product;
import se.ivankrizsan.monolithmicroservices.warehouse.domain.ProductReservation;
import se.ivankrizsan.monolithmicroservices.warehouse.exceptions.ProductNotInWarehouseException;
import se.ivankrizsan.monolithmicroservices.warehouse.persistence.ProductRepository;
import se.ivankrizsan.monolithmicroservices.warehouse.persistence.ProductReservationRepository;

import java.util.Optional;

/**
 * Implementation of the {@link WarehouseService}.
 *
 * @author Ivan Krizsan
 */
@RequiredArgsConstructor
@NoArgsConstructor
public class WarehouseServiceImplementation implements WarehouseService {
    /* Constant(s): */

    /* Instance variable(s): */
    @NonNull
    protected ProductRepository mProductRepository;
    @NonNull
    protected ProductReservationRepository mProductReservationRepository;

    @Override
    public Optional<Double> retrieveProductAvailableAmount(final String inProductNumber) {
        final Optional<Product> theProductOptional = mProductRepository.findByProductNumber(inProductNumber);
        return theProductOptional.map(Product::availableAmount);
    }

    @Override
    public Optional<Double> retrieveProductUnitPrice(final String inProductNumber) {
        final Optional<Product> theProductOptional = mProductRepository.findByProductNumber(inProductNumber);
        return theProductOptional.map(Product::unitPrice);
    }

    @Override
    @Transactional
    public Optional<Long> reserveProduct(final String inProductNumber, final double inAmount) {
        final Optional<Product> theProductOptional = mProductRepository.findByProductNumber(inProductNumber);
        if (theProductOptional.isEmpty()) {
            /* No matching product. */
            return Optional.empty();
        } else if (theProductOptional.get().availableAmount() < inAmount) {
            /* Insufficient product amount available - cannot reserve. */
            return Optional.empty();
        } else {
            /* Reduce the available amount and increase the reserved amount. */
            Product theProductToReserve = theProductOptional.get();
            final double theNewAvailableAmount = theProductToReserve.availableAmount() - inAmount;
            final double theNewReservedAmount = theProductToReserve.reservedAmount() + inAmount;
            theProductToReserve.availableAmount(theNewAvailableAmount);
            theProductToReserve.reservedAmount(theNewReservedAmount);

            /* Create a product reservation for the amount. */
            final ProductReservation theProductReservation = new ProductReservation(inProductNumber, inAmount);
            mProductReservationRepository.save(theProductReservation);

            return Optional.of(theProductReservation.getId());
        }
    }

    @Override
    public Optional<Double> retrieveReservationAmount(final Long inProductReservation) {
        final Optional<ProductReservation> theProductReservationOptional =
            mProductReservationRepository.findById(inProductReservation);
        return theProductReservationOptional.map(ProductReservation::getReservedAmount);
    }

    @Override
    public void createProductInWarehouse(final String inProductNumber,
                                         final String inProductName,
                                         final Double inProductUnitPrice) {
        if (!mProductRepository.existsByProductNumber(inProductNumber)) {
            final Product theNewProduct = new Product()
                .productNumber(inProductNumber)
                .name(inProductName)
                .availableAmount(0)
                .reservedAmount(0)
                .unitPrice(inProductUnitPrice);
            mProductRepository.save(theNewProduct);
        }
    }

    @Override
    public void increaseProductStock(final String inProductNumber, final double inAmount)
        throws ProductNotInWarehouseException {
        final Optional<Product> theProductOptional = mProductRepository.findByProductNumber(inProductNumber);
        if (theProductOptional.isEmpty()) {
            throw new ProductNotInWarehouseException(inProductNumber);
        }

        /* Increase the product's available amount. */
        final Product theProduct = theProductOptional.get();
        final double theNewProductAmount = theProduct.availableAmount() + inAmount;
        theProduct.availableAmount(theNewProductAmount);
        mProductRepository.save(theProduct);
    }
}

