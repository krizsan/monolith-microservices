package se.ivankrizsan.monolithmicroservices.modules.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an order.
 *
 * @author Ivan Krizsan
 */
@Entity
@Table(name = "placed_orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "payment_id", nullable = false)
    @NonNull
    protected String paymentId;
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
        name = "OrderItemsAssociation",
        joinColumns = {@JoinColumn(name = "order_id")},
        inverseJoinColumns = {@JoinColumn(name = "orderitem_id")})
    protected List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Adds the supplied order item to the order.
     *
     * @param inOrderItem Item to add to order.
     */
    public void addOrderItem(final OrderItem inOrderItem) {
        orderItems.add(inOrderItem);
    }
}
