package com.wealth.entity;

import com.wealth.enums.AssetType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol; // e.g., AAPL, VTSAX

    @Column(name = "asset_name", nullable = false)
    private String assetName; // e.g., Apple Inc., Vanguard Total Stock Market

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private AssetType assetType; // STOCK, MUTUAL_FUND, BOND, CASH

    // For CASH: stores the cash balance (e.g. 50000.0 = ₹50,000)
    // For STOCK/BOND/MUTUAL_FUND: stores number of units held
    @Column(name = "total_quantity", nullable = false)
    private Double totalQuantity;

    // NULL for CASH (cash has no buy price concept)
    @Column(name = "average_buy_price")
    private Double averageBuyPrice;

    // Auto-set to 1.0 for CASH; actual market price for others
    @Column(name = "current_price")
    private Double currentPrice;

    // Many Assets belong to One Portfolio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
}
