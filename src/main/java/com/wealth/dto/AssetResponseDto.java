package com.wealth.dto;

import com.wealth.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetResponseDto {
    private Long id;
    private String symbol;
    private String assetName;
    private AssetType assetType;
    private Double totalQuantity;
    private Double averageBuyPrice;
    private Double currentPrice;
    private Long portfolioId;   // which portfolio this asset belongs to
}
