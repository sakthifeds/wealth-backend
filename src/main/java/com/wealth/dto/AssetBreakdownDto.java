package com.wealth.dto;

import com.wealth.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetBreakdownDto {
    private AssetType assetType;    // STOCK, MUTUAL_FUND, BOND, CASH
    private Double totalValue;      // Total money invested in this asset type
    private Double percentage;      // % of the whole portfolio
}
