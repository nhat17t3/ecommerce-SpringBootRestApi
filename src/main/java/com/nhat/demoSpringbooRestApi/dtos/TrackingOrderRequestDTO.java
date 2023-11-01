package com.nhat.demoSpringbooRestApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingOrderRequestDTO {

    private String trackingNumber;

    private String[] courierCode;

}
