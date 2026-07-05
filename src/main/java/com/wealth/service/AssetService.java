package com.wealth.service;

import com.wealth.dto.AssetBreakdownDto;
import com.wealth.dto.AssetRequestDto;
import com.wealth.dto.AssetResponseDto;
import com.wealth.dto.PortfolioSummaryDto;
import com.wealth.entity.Asset;
import com.wealth.entity.Portfolio;
import com.wealth.enums.AssetType;
import com.wealth.enums.PortfolioType;
import com.wealth.repository.AssetRepository;
import com.wealth.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    // ─────────────────────────────────────────────────────────────────────
    // Business Rules: which asset types are allowed per portfolio type
    // ─────────────────────────────────────────────────────────────────────
    private static final Set<AssetType> RETIREMENT_ALLOWED =
            EnumSet.of(AssetType.MUTUAL_FUND, AssetType.BOND, AssetType.CASH);

    private static final Set<AssetType> SPECULATIVE_ALLOWED =
            EnumSet.of(AssetType.STOCK, AssetType.MUTUAL_FUND);

    private static final Set<AssetType> GENERAL_ALLOWED =
            EnumSet.allOf(AssetType.class); // STOCK, MUTUAL_FUND, BOND, CASH

    // ─────────────────────────────────────────────────────────────────────
    // ADD ASSET
    // ─────────────────────────────────────────────────────────────────────
    public AssetResponseDto addAsset(Long portfolioId, AssetRequestDto request) {
        // 1. Fetch the portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        // 2. Validate asset type against portfolio type rules
        validateAssetType(portfolio.getType(), request.getAssetType());

        // 3. Apply CASH-specific defaults
        handleCashDefaults(request);

        // 4. Map DTO → Entity
        Asset asset = new Asset();
        asset.setSymbol(request.getSymbol());
        asset.setAssetName(request.getAssetName());
        asset.setAssetType(request.getAssetType());
        asset.setTotalQuantity(request.getTotalQuantity());
        asset.setAverageBuyPrice(request.getAverageBuyPrice());
        asset.setCurrentPrice(request.getCurrentPrice());
        asset.setPortfolio(portfolio);

        // 4. Save and return
        Asset saved = assetRepository.save(asset);
        return mapToDto(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET ALL ASSETS FOR A PORTFOLIO
    // ─────────────────────────────────────────────────────────────────────
    public List<AssetResponseDto> getAssetsByPortfolio(Long portfolioId) {
        // Verify portfolio exists
        portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        return assetRepository.findByPortfolioId(portfolioId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────
    // UPDATE ASSET (e.g., update current price or quantity)
    // ─────────────────────────────────────────────────────────────────────
    public AssetResponseDto updateAsset(Long assetId, AssetRequestDto request) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + assetId));

        // If assetType is being changed, re-validate against portfolio rules
        if (request.getAssetType() != null && request.getAssetType() != asset.getAssetType()) {
            validateAssetType(asset.getPortfolio().getType(), request.getAssetType());
            asset.setAssetType(request.getAssetType());
        }

        // Re-apply CASH defaults in case assetType changed
        handleCashDefaults(request);

        if (request.getSymbol() != null)        asset.setSymbol(request.getSymbol());
        if (request.getAssetName() != null)     asset.setAssetName(request.getAssetName());
        if (request.getTotalQuantity() != null) asset.setTotalQuantity(request.getTotalQuantity());
        if (request.getAverageBuyPrice() != null) asset.setAverageBuyPrice(request.getAverageBuyPrice());
        if (request.getCurrentPrice() != null)  asset.setCurrentPrice(request.getCurrentPrice());

        Asset updated = assetRepository.save(asset);
        return mapToDto(updated);
    }

    // ─────────────────────────────────────────────────────────────────────
    // DELETE ASSET
    // ─────────────────────────────────────────────────────────────────────
    public void deleteAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new RuntimeException("Asset not found with ID: " + assetId);
        }
        assetRepository.deleteById(assetId);
    }

    // ─────────────────────────────────────────────────────────────────────
    // PORTFOLIO SUMMARY (for pie/donut chart)
    // ─────────────────────────────────────────────────────────────────────
    public PortfolioSummaryDto getPortfolioSummary(Long portfolioId) {
        // 1. Fetch portfolio
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        // 2. Fetch all assets in this portfolio
        List<Asset> assets = assetRepository.findByPortfolioId(portfolioId);

        // 3. Calculate total value per asset type
        //    CASH: value = totalQuantity (it's already a money amount)
        //    Others: value = totalQuantity × currentPrice
        Map<AssetType, Double> valueByType = assets.stream()
                .collect(Collectors.groupingBy(
                        Asset::getAssetType,
                        Collectors.summingDouble(a -> {
                            if (a.getAssetType() == AssetType.CASH) {
                                return a.getTotalQuantity(); // cash amount directly
                            }
                            return a.getTotalQuantity() * a.getCurrentPrice();
                        })
                ));

        // 4. Grand total
        double grandTotal = valueByType.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // 5. Build breakdown list with percentages
        List<AssetBreakdownDto> breakdown = valueByType.entrySet().stream()
                .map(entry -> {
                    double pct = grandTotal > 0
                            ? Math.round((entry.getValue() / grandTotal) * 10000.0) / 100.0
                            : 0.0;
                    return new AssetBreakdownDto(entry.getKey(), entry.getValue(), pct);
                })
                .sorted((a, b) -> Double.compare(b.getTotalValue(), a.getTotalValue())) // highest first
                .collect(Collectors.toList());

        return new PortfolioSummaryDto(portfolioId, portfolio.getName(), grandTotal, breakdown);
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────

    /**
     * For CASH asset type:
     *  - averageBuyPrice is forced to null (concept doesn't apply to cash)
     *  - currentPrice is forced to 1.0  (1 unit of cash = face value)
     * Client only needs to provide totalQuantity (the cash balance amount).
     */
    private void handleCashDefaults(AssetRequestDto request) {
        if (request.getAssetType() == AssetType.CASH) {
            request.setAverageBuyPrice(null);   // Not applicable
            request.setCurrentPrice(1.0);        // Cash is always worth face value
        }
    }

    /**
     * PUBLIC method — allows other services (e.g., StockCatalogService) to
     * reuse the same portfolio-type validation rules.
     */
    public void validateAssetTypePublic(PortfolioType portfolioType, AssetType assetType) {
        validateAssetType(portfolioType, assetType);
    }

    /**
     * Validates that the given assetType is allowed for the given portfolioType.
     * Throws IllegalArgumentException with a clear message on violation.
     */
    private void validateAssetType(PortfolioType portfolioType, AssetType assetType) {
        Set<AssetType> allowed = switch (portfolioType) {
            case RETIREMENT  -> RETIREMENT_ALLOWED;
            case SPECULATIVE -> SPECULATIVE_ALLOWED;
            case GENERAL     -> GENERAL_ALLOWED;
        };

        if (!allowed.contains(assetType)) {
            throw new IllegalArgumentException(
                    String.format("'%s' portfolios do not allow asset type '%s'. Allowed types: %s",
                            portfolioType, assetType, allowed)
            );
        }
    }

    /** Maps an Asset entity to an AssetResponseDto */
    private AssetResponseDto mapToDto(Asset asset) {
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
