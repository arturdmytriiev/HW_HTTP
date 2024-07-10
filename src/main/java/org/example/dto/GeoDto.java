package org.example.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record GeoDto(BigDecimal lat , BigDecimal lng) {
}
