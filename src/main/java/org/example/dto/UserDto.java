package org.example.dto;

import lombok.Builder;

@Builder
public record UserDto(Integer id , String name , String username ,
                      String email, AddressDto address, String phone,
                      String website , CompanyDto company) {
}
