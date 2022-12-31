package se.ivankrizsan.monolithmicroservices.modules.payment.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.ivankrizsan.monolithmicroservices.modules.payment.api.PaymentService;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link PaymentService}.
 * In this implementation, the payment service just manages one single purse with money.
 *
 * @author Ivan Krizsan
 */
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PaymentService {
    /** Amount of money available in the single purse managed by the service. */
    @NonNull
    protected Double assets;

    @Override
    public synchronized Optional<String> makePayment(double inPaymentAmount) {
        if (inPaymentAmount <= assets) {
            assets -= inPaymentAmount;
            return Optional.of(UUID.randomUUID().toString());
        }

        /* Not enough assets to cover payment. */
        return Optional.empty();
    }
}
