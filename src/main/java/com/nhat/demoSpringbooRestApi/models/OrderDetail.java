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
//    @JoinColumn(name = "user_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
//    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Float price;

    @NotNull
    private Integer quantity;




}
