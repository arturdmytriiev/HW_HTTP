package org.example.utils;

import org.example.dto.AddressDto;
import org.example.dto.CompanyDto;
import org.example.dto.GeoDto;
import org.example.dto.UserDto;

import java.math.BigDecimal;

public class UserUtils {
    public static UserDto getUser()
    {
        UserDto userDTO = UserDto.builder()
                .id(1)
                .name("Jane Doe")
                .username("janedoe")
                .email("jane.doe@example.com")
                .phone("123-456-7890")
                .website("example.com")
                .address(AddressDto.builder()
                        .street("123 Elm St")
                        .suite("Apt 9")
                        .city("Some City")
                        .zipcode("12345")
                        .geo(GeoDto.builder()
                                .lat(BigDecimal.valueOf(40.7128))
                                .lng(BigDecimal.valueOf(-74.0060))
                                .build())
                        .build())
                .company(CompanyDto.builder()
                        .name("Doe Enterprises")
                        .catchPhrase("Excellence in Everything")
                        .bs("innovate strategically")
                        .build())
                .build();
        return userDTO;
    }
    public static UserDto getNewUser()
    {
        UserDto userDTO = UserDto.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+4219073457")
                .address(AddressDto.builder()
                        .city("New York")
                        .geo(GeoDto.builder()
                                .lng(BigDecimal.valueOf(3123121))
                                .lat(BigDecimal.valueOf(3221123))
                                .build())
                        .suite("Apt. 559")
                        .street("Wolf Street")
                        .zipcode("84100")
                        .build())
                .username("Willy")
                .website("arronconsalt.com")
                .company(CompanyDto.builder()
                        .bs("synergize scalable supply-chains")
                        .catchPhrase("Proactive contingency")
                        .name("Crew")
                        .build())
                .build();
        return userDTO;
    }
}
