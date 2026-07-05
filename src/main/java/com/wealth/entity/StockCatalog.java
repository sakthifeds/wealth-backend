package com.wealth.entity;

import com.wealth.enums.AssetType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_catalog")
@Data
@NoArgsConstructor
public class StockCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;          // e.g., AAPL, VTSAX

    @Column(name = "name", nullable = false)
    private String name;            // e.g., Apple Inc.

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private AssetType assetType;    // STOCK, MUTUAL_FUND, BOND

    @Column(name = "current_price", nullable = false)
    private Double currentPrice;    // Hardcoded price in DB

    private String description;     // Short description shown in UI
}
