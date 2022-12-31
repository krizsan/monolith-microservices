package se.ivankrizsan.monolithmicroservices.modules.order.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.ivankrizsan.monolithmicroservices.modules.order.domain.Order;

/**
 * Repository containing orders.
 *
 * @author Ivan Krizsan
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
