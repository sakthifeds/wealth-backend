package com.wealth.controller;

import com.wealth.dto.AssetRequestDto;
import com.wealth.dto.AssetResponseDto;
import com.wealth.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    // 1. Add an asset to a specific portfolio
    @PostMapping("/api/portfolios/{portfolioId}/assets")
    public ResponseEntity<?> addAsset(
            @PathVariable Long portfolioId,
            @RequestBody AssetRequestDto request) {
        try {
            AssetResponseDto response = assetService.addAsset(portfolioId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Portfolio-type validation failure → 400 with clear message
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Portfolio not found → 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Get all assets for a specific portfolio
    @GetMapping("/api/portfolios/{portfolioId}/assets")
    public ResponseEntity<List<AssetResponseDto>> getAssets(@PathVariable Long portfolioId) {
        List<AssetResponseDto> assets = assetService.getAssetsByPortfolio(portfolioId);
        return ResponseEntity.ok(assets);
    }

    // 3. Update an asset (e.g., refresh current price)
    @PutMapping("/api/assets/{assetId}")
    public ResponseEntity<?> updateAsset(
            @PathVariable Long assetId,
            @RequestBody AssetRequestDto request) {
        try {
            AssetResponseDto response = assetService.updateAsset(assetId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Delete an asset
    @DeleteMapping("/api/assets/{assetId}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long assetId) {
        try {
            assetService.deleteAsset(assetId);
            return ResponseEntity.ok("Asset deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
