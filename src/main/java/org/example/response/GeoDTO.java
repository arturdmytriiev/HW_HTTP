package org.example.response;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record GeoDTO(BigDecimal lat , BigDecimal lng) {
}
