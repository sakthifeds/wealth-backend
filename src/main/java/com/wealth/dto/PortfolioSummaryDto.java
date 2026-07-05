package com.wealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PortfolioSummaryDto {
    private Long portfolioId;
    private String portfolioName;
    private Double totalValue;              // Grand total of all assets
    private List<AssetBreakdownDto> breakdown; // Per-asset-type breakdown
}
