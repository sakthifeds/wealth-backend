package com.wealth.dto;

import com.wealth.enums.PortfolioType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private PortfolioType type;
}
