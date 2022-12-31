package se.ivankrizsan.monolithmicroservices.modules.order.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import se.ivankrizsan.monolithmicroservices.modules.order.api.OrderService;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.OrderItem;
import se.ivankrizsan.monolithmicroservices.modules.order.implementation.OrderServiceImplementation;
import se.ivankrizsan.monolithmicroservices.modules.order.persistence.OrderRepository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.api.WarehouseService;

/**
 * Configuration that creates the beans for the order service.
 *
 * @author Ivan Krizsan
 */
@Configuration
@EntityScan(basePackageClasses = OrderItem.class)
@EnableJpaRepositories(basePackageClasses = OrderRepository.class)
public class OrderServiceConfiguration {

    /**
     * Creates an order service for creating and managing orders.
     *
     * @param inOrderRepository Repository in which to store orders.
     *
     * @return Order service.
     */
    @Bean
    public OrderService orderService(final OrderRepository inOrderRepository, final WarehouseService inWarehouseService) {
        return new OrderServiceImplementation(inOrderRepository, inWarehouseService);
    }
}
