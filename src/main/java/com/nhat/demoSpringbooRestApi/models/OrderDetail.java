package com.nhat.demoSpringbooRestApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderProductKey id = new OrderProductKey();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("orderId")
//    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
//    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    @NotNull(message = "{error.order_detail.price.null}")
    private Float price;

    @Column(name = "quantity")
    @NotNull(message = "{error.order_detail.quantity.null}")
    private Integer quantity;




}
