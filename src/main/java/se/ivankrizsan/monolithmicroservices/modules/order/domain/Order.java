package se.ivankrizsan.monolithmicroservices.modules.order.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
