package se.ivankrizsan.monolithmicroservices.payment.implementation;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import se.ivankrizsan.monolithmicroservices.payment.api.PaymentService;

import java.util.Optional;

/**
 * Implementation of the {@link PaymentService}.
 *
 * @author Ivan Krizsan
 */
@RequiredArgsConstructor
@NoArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    @Override
    public Optional<Long> makePayment(double inPaymentAmount) {
        return Optional.empty();
    }
}
