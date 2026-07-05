package com.wealth.dto;

import com.wealth.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockCatalogDto {
    private Long id;
    private String symbol;
    private String name;
    private AssetType assetType;
    private Double currentPrice;
    private String description;
}
