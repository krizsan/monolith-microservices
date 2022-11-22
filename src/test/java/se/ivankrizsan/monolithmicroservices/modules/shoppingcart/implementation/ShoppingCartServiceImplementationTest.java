package se.ivankrizsan.monolithmicroservices.modules.shoppingcart.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.shoppingcart.domain.ShoppingCartItem;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.configuration.WarehouseConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.domain.Product;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.implementation.WarehouseServiceImplementation;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductReservationRepository;

/**
 * Tests the {@link WarehouseServiceImplementation}.
 *
 * @author Ivan Krizsan
 */
@DataJpaTest()
@EnableJpaRepositories(basePackageClasses = ProductRepository.class)
@ContextConfiguration(classes = { WarehouseConfiguration.class })
@EntityScan(basePackageClasses = ShoppingCartItem.class)
class ShoppingCartServiceImplementationTest {
    /* Constant(s): */
    public final static String PRODUCTA_PRODUCTNUMBER = "12345-1";
    public final static String NONEXISTING_PRODUCTNUMBER = "00000-0";
    public final static double PRODUCTA_AVAILABLEAMOUNT = 100;
    public final static double PRODUCTA_RESERVEAMOUNT = 55;

    /* Instance variable(s): */
    @Autowired
    protected ProductRepository mProductRepository;
    @Autowired
    protected ProductReservationRepository mProductReservationRepository;
    @Autowired
    protected WarehouseService mWarehouseService;

    /**
     * Sets up information in database tables etc before each test.
     */
    @BeforeEach
    void setUpBeforeEachTest() {
        final Product theProduct = new Product()
            .name("Product A")
            .productNumber(PRODUCTA_PRODUCTNUMBER)
            .availableAmount(PRODUCTA_AVAILABLEAMOUNT)
            .reservedAmount(0);

        mProductRepository.save(theProduct);
    }

    /**
     * Cleans up after each test by deleting information in database tables etc.
     */
    @AfterEach
    void cleanUpAfterEachTest() {
        mProductRepository.deleteAll();
        mProductReservationRepository.deleteAll();
    }

    @Test
    void addItemToCartTest() {
    }
}