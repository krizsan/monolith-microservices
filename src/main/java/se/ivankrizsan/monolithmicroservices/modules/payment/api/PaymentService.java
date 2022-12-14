package se.ivankrizsan.monolithmicroservices.modules.payment.api;

import java.util.Optional;

/**
 * Payment service administrates payments.
 *
 * @author Ivan Krizsan
 */
public interface PaymentService {

    /**
     * Makes a payment of the supplied amount of money.
     *
     * @return Payment identifier if payment successful, empty otherwise.
     */
    Optional<Long> makePayment(double inPaymentAmount);
}
