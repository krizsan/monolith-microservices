package se.ivankrizsan.monolithmicroservices.modules.warehouse.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.ivankrizsan.monolithmicroservices.modules.warehouse.domain.ProductReservation;

/**
 * Repository containing reservations of products in a warehouse.
 *
 * @author Ivan Krizsan
 */
@Repository
public interface ProductReservationRepository extends JpaRepository<ProductReservation, Long> {
}
