package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "order1")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Float totalPrice;

    private EOrderStatus orderStatus;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "paymentMethod_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    private EPaymentStatus paymentStatus;

    private String nameReceiver;

    private String phoneReceiver;

    private String addressReceiver;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade =  CascadeType.ALL )
    private List<OrderDetail> orderDetails;


}
