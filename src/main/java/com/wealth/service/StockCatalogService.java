package com.wealth.service;

import com.wealth.dto.AssetResponseDto;
import com.wealth.dto.BuyFromCatalogRequestDto;
import com.wealth.dto.StockCatalogDto;
import com.wealth.entity.Asset;
import com.wealth.entity.Portfolio;
import com.wealth.entity.StockCatalog;
import com.wealth.enums.AssetType;
import com.wealth.repository.AssetRepository;
import com.wealth.repository.PortfolioRepository;
import com.wealth.repository.StockCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockCatalogService {

    @Autowired
    private StockCatalogRepository catalogRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetService assetService; // for portfolio-type validation

    // ─────────────────────────────────────────────────────────────────────
    // LIST ALL CATALOG ITEMS (optionally filtered by type)
    // ─────────────────────────────────────────────────────────────────────
    public List<StockCatalogDto> getAllCatalogItems() {
        return catalogRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<StockCatalogDto> getCatalogByType(AssetType assetType) {
        return catalogRepository.findByAssetType(assetType)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────
    // BUY FROM CATALOG (Idea 1 — Invest by Amount)
    // User provides: catalogItemId + portfolioId + investmentAmount
    // System calculates: quantity = investmentAmount / currentPrice
    // ─────────────────────────────────────────────────────────────────────
    public AssetResponseDto buyFromCatalog(BuyFromCatalogRequestDto request) {
        // 1. Fetch the catalog item (has symbol, name, type, price)
        StockCatalog catalogItem = catalogRepository.findById(request.getCatalogItemId())
                .orElseThrow(() -> new RuntimeException("Catalog item not found with ID: " + request.getCatalogItemId()));

        // 2. Fetch the target portfolio
        Portfolio portfolio = portfolioRepository.findById(request.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + request.getPortfolioId()));

        // 3. Validate asset type against portfolio type rules
        assetService.validateAssetTypePublic(portfolio.getType(), catalogItem.getAssetType());

        // 4. Calculate quantity from investment amount
        //    e.g., ₹10,000 / ₹192.50 per share = 51.94 units
        if (request.getInvestmentAmount() <= 0) {
            throw new IllegalArgumentException("Investment amount must be greater than zero.");
        }
        double quantity = request.getInvestmentAmount() / catalogItem.getCurrentPrice();
        quantity = Math.round(quantity * 100.0) / 100.0; // round to 2 decimal places

        // 5. Check if user already owns this asset in the same portfolio
        //    If yes → update quantity and recalculate average buy price
        //    If no  → create a new asset entry
        Optional<Asset> existingAsset = assetRepository
                .findByPortfolioIdAndSymbol(request.getPortfolioId(), catalogItem.getSymbol());

        Asset asset;
        if (existingAsset.isPresent()) {
            // UPDATE existing asset
            asset = existingAsset.get();
            double oldTotalCost = asset.getTotalQuantity() * asset.getAverageBuyPrice();
            double newTotalCost = oldTotalCost + request.getInvestmentAmount();
            double newTotalQty  = asset.getTotalQuantity() + quantity;

            asset.setTotalQuantity(Math.round(newTotalQty * 100.0) / 100.0);
            asset.setAverageBuyPrice(Math.round((newTotalCost / newTotalQty) * 100.0) / 100.0);
            asset.setCurrentPrice(catalogItem.getCurrentPrice());
        } else {
            // CREATE new asset
            asset = new Asset();
            asset.setSymbol(catalogItem.getSymbol());
            asset.setAssetName(catalogItem.getName());
            asset.setAssetType(catalogItem.getAssetType());
            asset.setTotalQuantity(quantity);
            asset.setAverageBuyPrice(catalogItem.getCurrentPrice());
            asset.setCurrentPrice(catalogItem.getCurrentPrice());
            asset.setPortfolio(portfolio);
        }

        Asset saved = assetRepository.save(asset);
        return mapAssetToDto(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────
    private StockCatalogDto mapToDto(StockCatalog item) {
        return new StockCatalogDto(
                item.getId(),
                item.getSymbol(),
                item.getName(),
                item.getAssetType(),
                item.getCurrentPrice(),
                item.getDescription()
        );
    }

    private AssetResponseDto mapAssetToDto(Asset asset) {
        return new AssetResponseDto(
                asset.getId(),
                asset.getSymbol(),
                asset.getAssetName(),
                asset.getAssetType(),
                asset.getTotalQuantity(),
                asset.getAverageBuyPrice(),
                asset.getCurrentPrice(),
                asset.getPortfolio().getId()
        );
    }
}
