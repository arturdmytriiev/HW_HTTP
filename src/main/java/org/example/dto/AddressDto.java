package org.example.dto;

import lombok.Builder;

@Builder
public record AddressDto(String street, String city, String suite,
                         String zipcode, GeoDto geo) {
}
