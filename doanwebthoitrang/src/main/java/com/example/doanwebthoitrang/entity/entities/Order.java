package com.example.doanwebthoitrang.entity.entities;

import com.example.doanwebthoitrang.entity.enums.OrderStatus;
import com.example.doanwebthoitrang.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nullable
    private User user;

    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id") // Nullable
    private Address shippingAddress;

    @Column(name = "shipping_address_snapshot", columnDefinition = "NVARCHAR(MAX)")
    private String shippingAddressSnapshot;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate = LocalDateTime.now(); // Hoặc dùng @CreationTimestamp nếu trùng created_at

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.pending;

    @Column(name = "sub_total", nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal subTotal = java.math.BigDecimal.ZERO;

    @Column(name = "shipping_fee", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal shippingFee = java.math.BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal discountAmount = java.math.BigDecimal.ZERO;

    @Column(name = "total_money", nullable = false, precision = 15, scale = 2)
    private java.math.BigDecimal totalMoney = java.math.BigDecimal.ZERO;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.pending;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails = new HashSet<>();
}

