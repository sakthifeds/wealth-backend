package com.wealth.dto;

import com.wealth.enums.PortfolioType;
import lombok.Data;

@Data
public class PortfolioRequestDto {
    private String name;
    private String description;
    private Long userId; // Which user does this portfolio belong to?
    private PortfolioType type; // e.g., GENERAL, RETIREMENT, SPECULATIVE
}
