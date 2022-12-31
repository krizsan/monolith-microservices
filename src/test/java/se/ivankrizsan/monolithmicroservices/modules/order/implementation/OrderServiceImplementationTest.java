package se.ivankrizsan.monolithmicroservices.modules.order.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.order.api.OrderService;
import se.ivankrizsan.monolithmicroservices.modules.order.configuration.OrderServiceConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.configuration.WarehouseConfiguration;

import java.util.Optional;

/**
 * Tests the {@link OrderServiceImplementation}.
 *
 * @author Ivan Krizsan
 */
@DataJpaTest()
@ContextConfiguration(classes = { OrderServiceConfiguration.class, WarehouseConfiguration.class })
public class OrderServiceImplementationTest {
    public final static String PRODUCTA_PRODUCTNUMBER = "12345-1";
    public final static double PRODUCTA_AVAILABLEAMOUNT = 100;
    public final static double PRODUCTA_RESERVEAMOUNT = 55;
    public final static double PRODUCTA_UNITPRICE = 15.41;

    @Autowired
    protected OrderService mOrderService;
    @Autowired
    protected WarehouseService mWarehouseService;


    /**
     * Tests creation of a new order when a payment id has been supplied.
     * Expected result:
     * An id of the newly created order should be returned.
     */
    @Test
    public void createOrderWithGoodPaymentIdTest() {
        final Optional<String> theNewOrderIdOptional = mOrderService.createOrder("12343");

        Assertions.assertTrue(theNewOrderIdOptional.isPresent(), "An id of the new order should be created");
    }

    /**
     * Tests creation of a new order when no payment id is supplied.
     * Expected result:
     * No order id should be returned.
     */
    @Test
    public void createOrderWithNoPaymentIdTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            mOrderService.createOrder("");
        });
    }

    /**
     * Tests adding a previously reserved item to an order.
     * Expected result:
     * The item should be successfully added to the order.
     */
    @Test
    public void addItemToOrderSuccessfulTest() {
        /* Make a reservation for an item that will be part of the order. */
        mWarehouseService.createProductInWarehouse(
            PRODUCTA_PRODUCTNUMBER,
            "Product A",
            PRODUCTA_UNITPRICE);
        mWarehouseService.increaseProductStock(PRODUCTA_PRODUCTNUMBER, PRODUCTA_AVAILABLEAMOUNT);
        final Optional<Long> theProductReservationOptional = mWarehouseService.reserveProduct(
            PRODUCTA_PRODUCTNUMBER, PRODUCTA_RESERVEAMOUNT);
        final Long theReservationId = theProductReservationOptional.get();

        /* Create an order to which the reserved item will be added. */
        final Optional<String> theNewOrderIdOptional = mOrderService.createOrder("12343");
        final String theOrderId = theNewOrderIdOptional.get();

        /* Add the item to the order and verify the outcome. */
        final boolean theAddItemToOrderFlag = mOrderService.addItemToOrder(theOrderId, PRODUCTA_PRODUCTNUMBER,
            theReservationId);
        Assertions.assertTrue(theAddItemToOrderFlag, "The item should be successfully added to the order");
    }

    /**
     * Tests adding an item for which there is no reservation.
     * Expected result:
     * The item should not be added to the order.
     */
    @Test
    public void addItemNotReservedToOrderTest() {
        /* Create an order to which to attempt to add the item without reservation. */
        final Optional<String> theNewOrderIdOptional = mOrderService.createOrder("12343");
        final String theOrderId = theNewOrderIdOptional.get();

        /* Add the item to the order and verify the outcome. */
        final boolean theAddItemToOrderFlag = mOrderService.addItemToOrder(theOrderId, PRODUCTA_PRODUCTNUMBER,
            12345L);
        Assertions.assertFalse(theAddItemToOrderFlag, "The item should not be added to the order");
    }
}
