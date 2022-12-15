package se.ivankrizsan.monolithmicroservices.modules.warehouse.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.configuration.WarehouseConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductReservationRepository;

import java.util.Optional;

/**
 * Tests the {@link WarehouseServiceImplementation}.
 *
 * @author Ivan Krizsan
 */
@DataJpaTest()
@ContextConfiguration(classes = { WarehouseConfiguration.class })
class WarehouseServiceImplementationTest {
    /* Constant(s): */
    public final static String PRODUCTA_PRODUCTNUMBER = "12345-1";
    public final static String NONEXISTING_PRODUCTNUMBER = "00000-0";
    public final static double PRODUCTA_AVAILABLEAMOUNT = 100;
    public final static double PRODUCTA_RESERVEAMOUNT = 55;

    /* Instance variable(s): */
    @Autowired
    protected ProductRepository mProductRepository;
    @Autowired
    protected ProductReservationRepository mProductReservationsRepository;
    @Autowired
    protected WarehouseService mWarehouseService;

    /**
     * Sets up information in database tables before each test.
     */
    @BeforeEach
    void setUpBeforeEachTest() {
        mWarehouseService.createProductInWarehouse(PRODUCTA_PRODUCTNUMBER, "Product A");
        mWarehouseService.increaseProductStock(PRODUCTA_PRODUCTNUMBER, PRODUCTA_AVAILABLEAMOUNT);
    }

    /**
     * Cleans up after each test by deleting information in database tables.
     */
    @AfterEach
    void cleanUpAfterEachTest() {
        mProductRepository.deleteAll();
        mProductReservationsRepository.deleteAll();
    }

    /**
     * Tests creation of a product that does not previously exist in the warehouse.
     * Expected result:
     * A product should be created in the warehouse
     */
    @Test
    void createProductInWarehouseTest() {
        Assertions.assertTrue(mProductRepository.existsByProductNumber(PRODUCTA_PRODUCTNUMBER),
            "A product should have been created in the warehouse");
    }

    /**
     * Tests increasing the stock of a product that exists in the warehouse.
     * Expected result:
     * The available amount of the product should be increased.
     */
    @Test
    void increaseProductStockTest() {
        final Optional<Double> theAmountBeforeIncrease =
            mWarehouseService.retrieveProductAvailableAmount(PRODUCTA_PRODUCTNUMBER);
        mWarehouseService.increaseProductStock(PRODUCTA_PRODUCTNUMBER, PRODUCTA_AVAILABLEAMOUNT);
        final Optional<Double> theAmountAfterIncrease =
            mWarehouseService.retrieveProductAvailableAmount(PRODUCTA_PRODUCTNUMBER);

        Assertions.assertTrue(theAmountBeforeIncrease.isPresent());
        Assertions.assertTrue(theAmountAfterIncrease.isPresent());
        Assertions.assertTrue(theAmountBeforeIncrease.get() < theAmountAfterIncrease.get(),
            "The available amount of the product should have increased");
    }

    /**
     * Tests retrieving product available amount for an existing product.
     * Expected result:
     * A product available amount should be retrieved and should have the expected value.
     */
    @Test
    void retrieveProductAvailableAmountTest() {
        final Optional<Double> theProductAAmountOptional =
            mWarehouseService.retrieveProductAvailableAmount(PRODUCTA_PRODUCTNUMBER);

        Assertions.assertTrue(theProductAAmountOptional.isPresent(), "There should be an amount for the product");
        Assertions.assertEquals(PRODUCTA_AVAILABLEAMOUNT, theProductAAmountOptional.get(),
            "The available amount for the product should be the expected");
    }

    /**
     * Tests reserving an existing product for which there is a sufficient amount available.
     * Expected result:
     * The reservation should be successful.
     * The available amount of the product should be reduced by the reserved amount.
     */
    @Test
    void reserveProductTest() {
        final Optional<Long> theProductReservationOptional = mWarehouseService.reserveProduct(
            PRODUCTA_PRODUCTNUMBER, PRODUCTA_RESERVEAMOUNT);
        final Optional<Double> theProductAAmountOptional =
            mWarehouseService.retrieveProductAvailableAmount(PRODUCTA_PRODUCTNUMBER);

        Assertions.assertTrue(theProductReservationOptional.isPresent(), "The product reservation should be successful");
        Assertions.assertTrue(theProductAAmountOptional.isPresent());
        Assertions.assertEquals(PRODUCTA_AVAILABLEAMOUNT - PRODUCTA_RESERVEAMOUNT,
            theProductAAmountOptional.get(),
            "The reserved amount should have been deduced from the available amount");
    }

    /**
     * Tests attempting a reservation for a product that does not exist.
     * Expected result:
     * The reservation should be unsuccessful.
     */
    @Test
    void reserveNonexistingProductTest() {
        final Optional<Long> theProductReservationOptional = mWarehouseService.reserveProduct(
            NONEXISTING_PRODUCTNUMBER, PRODUCTA_RESERVEAMOUNT);

        Assertions.assertTrue(
            theProductReservationOptional.isEmpty(),
            "The product reservation should be unsuccessful");
    }

    /**
     * Tests attempting a reservation for an existing product which does not have a sufficient amount available.
     * Expected result:
     * The reservation should be unsuccessful.
     * The available amount of the product should not change.
     */
    @Test
    void reserveProductInsufficientAmountAvailableTest() {
        final Optional<Long> theProductReservationOptional = mWarehouseService.reserveProduct(
            PRODUCTA_PRODUCTNUMBER, PRODUCTA_AVAILABLEAMOUNT * 2);
        final Optional<Double> theProductAAmountOptional =
            mWarehouseService.retrieveProductAvailableAmount(PRODUCTA_PRODUCTNUMBER);

        Assertions.assertTrue(
            theProductReservationOptional.isEmpty(),
            "The product reservation should be unsuccessful");
        Assertions.assertTrue(theProductAAmountOptional.isPresent());
        Assertions.assertEquals(PRODUCTA_AVAILABLEAMOUNT, theProductAAmountOptional.get(),
            "The available amount of the product should not change");
    }
}