package com.lumastyle.order.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
