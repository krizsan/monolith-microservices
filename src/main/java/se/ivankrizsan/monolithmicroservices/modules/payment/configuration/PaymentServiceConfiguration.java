package se.ivankrizsan.monolithmicroservices.modules.payment.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import se.ivankrizsan.monolithmicroservices.modules.payment.api.PaymentService;
import se.ivankrizsan.monolithmicroservices.modules.payment.implementation.PaymentServiceImplementation;

/**
 * Configuration that creates the necessary beans needed for the shoppingcart service.
 *
 * @author Ivan Krizsan
 */
@Configuration
@EntityScan()
@EnableJpaRepositories()
public class PaymentServiceConfiguration {
    /* Constant(s): */
    public final static double DEFAULT_ASSETS_AMOUNT = 100;

    /* Dependencies: */


    /**
     * Creates a payment service with assets set to a default amount.
     *
     * @return Payment service.
     */
    @Bean
    protected PaymentService paymentService() {
        return new PaymentServiceImplementation(DEFAULT_ASSETS_AMOUNT);
    }
}
