package se.ivankrizsan.monolithmicroservices.modules.order.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an item in an order.
 *
 * @author Ivan Krizsan
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "product_number", nullable = false)
    @NonNull
    protected String productNumber;
    @Column(name = "reservation_id", nullable = false)
    @NonNull
    protected Long reservationId;
}
