package org.example.response;

import lombok.Builder;

@Builder
public record AddressDTO(String street, String city, String suite,
                         String zipcode, GeoDTO geo) {
}
