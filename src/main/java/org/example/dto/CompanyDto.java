package org.example.dto;

import lombok.Builder;

@Builder
public record CompanyDto(String name , String catchPhrase , String bs) {
}
