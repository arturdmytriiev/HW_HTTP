package org.example.response;

import lombok.Builder;

@Builder
public record CompanyDTO(String name , String catchPhrase , String bs) {
}
