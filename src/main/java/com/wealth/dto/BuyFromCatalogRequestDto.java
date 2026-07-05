package com.wealth.dto;

import lombok.Data;

@Data
public class BuyFromCatalogRequestDto {
    private Long catalogItemId;     // Which stock/MF/bond to buy
    private Long portfolioId;       // Which portfolio to buy into
    private Double investmentAmount; // How much money to invest (e.g., 10000.0)
}
