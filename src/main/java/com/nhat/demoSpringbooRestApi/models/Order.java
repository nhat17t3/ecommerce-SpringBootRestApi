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

    private String nameReceiver;

    private String phoneReceiver;

    private String addressReceiver;

    private Float totalPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "paymentMethod_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    private String paymentStatus;

//    private EPaymentStatus paymentStatus;

    @OneToMany(mappedBy = "order", cascade =  CascadeType.ALL )
    private List<OrderDetail> orderDetails;

    private String orderStatus;

    private String trackingNumber;

    private String trackerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



}
