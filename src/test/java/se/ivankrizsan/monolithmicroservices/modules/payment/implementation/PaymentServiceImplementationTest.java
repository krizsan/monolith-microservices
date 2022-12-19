package se.ivankrizsan.monolithmicroservices.modules.payment.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import se.ivankrizsan.monolithmicroservices.modules.payment.api.PaymentService;
import se.ivankrizsan.monolithmicroservices.modules.payment.configuration.PaymentServiceConfiguration;

import java.util.Optional;

/**
 * Tests the {@link PaymentServiceImplementation}.
 *
 * @author Ivan Krizsan
 */
@DataJpaTest()
@ContextConfiguration(classes = { PaymentServiceConfiguration.class })
public class PaymentServiceImplementationTest {
    /* Constant(s): */
    public static final Double SMALL_PAYMENT_AMOUNT = PaymentServiceConfiguration.DEFAULT_ASSETS_AMOUNT / 10;
    public static final Double TOOBIG_PAYMENT_AMOUNT = PaymentServiceConfiguration.DEFAULT_ASSETS_AMOUNT * 2;

    /* Instance variable(s): */
    @Autowired
    protected PaymentService mPaymentService;

    /**
     * Tests making a payment when there are enough assets to cover the payment.
     * Expected result:
     * The payment should be approved.
     */
    @Test
    public void makePaymentEnoughAssetsTest() {
        final Optional<String> thePaymentIdOptional = mPaymentService.makePayment(SMALL_PAYMENT_AMOUNT);
        Assertions.assertTrue(thePaymentIdOptional.isPresent(), "The payment should be approved");
    }

    /**
     * Tests making a payment when there are not enough assets to cover the payment.
     * Expected result:
     * The payment should not be approved.
     */
    @Test
    public void makePaymentNotEnoughAssetsTest() {
        final Optional<String> thePaymentIdOptional = mPaymentService.makePayment(TOOBIG_PAYMENT_AMOUNT);
        Assertions.assertFalse(thePaymentIdOptional.isPresent(), "The payment should not be approved");
    }
}
