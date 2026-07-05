package com.wealth.dto;

import com.wealth.enums.AssetType;
import lombok.Data;

@Data
public class AssetRequestDto {
    private String symbol;          // e.g., AAPL | Use "CASH" for cash entries
    private String assetName;       // e.g., Apple Inc. | Use "Cash Balance" for cash
    private AssetType assetType;    // STOCK, MUTUAL_FUND, BOND, CASH

    private Double totalQuantity;   // For CASH: the cash balance amount (e.g. 50000.0)
                                    // For others: number of units

    private Double averageBuyPrice; // Optional for CASH (will be set to null automatically)
    private Double currentPrice;    // Optional for CASH (will be set to 1.0 automatically)
}

