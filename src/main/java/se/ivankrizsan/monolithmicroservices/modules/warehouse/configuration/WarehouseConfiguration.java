package se.ivankrizsan.monolithmicroservices.modules.warehouse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.implementation.WarehouseServiceImplementation;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence.ProductReservationRepository;

/**
 * Configuration that creates the necessary beans etc needed for the warehouse service.
 *
 * @author Ivan Krizsan
 */
@Configuration
public class WarehouseConfiguration {
    /* Constant(s): */

    /* Dependencies: */
    @Autowired
    protected ProductRepository mProductRepository;
    @Autowired
    protected ProductReservationRepository mProductReservationRepository;

    /**
     * Creates the {@code WarehouseService} bean.
     *
     * @return Warehouse service.
     */
    @Bean
    protected WarehouseService warehouseService() {
        return new WarehouseServiceImplementation(mProductRepository, mProductReservationRepository);
    }
}
