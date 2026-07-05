package com.wealth.controller;

import com.wealth.dto.PortfolioRequestDto;
import com.wealth.dto.PortfolioResponseDto;
import com.wealth.dto.PortfolioSummaryDto;
import com.wealth.service.AssetService;
import com.wealth.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private AssetService assetService; // needed for summary

    // 1. Create a new Portfolio
    @PostMapping
    public ResponseEntity<?> createPortfolio(@RequestBody PortfolioRequestDto request) {
        try {
            PortfolioResponseDto response = portfolioService.createPortfolio(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // If the user ID is invalid, return a 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Get all portfolios for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PortfolioResponseDto>> getUserPortfolios(@PathVariable Long userId) {
        List<PortfolioResponseDto> portfolios = portfolioService.getUserPortfolios(userId);
        return ResponseEntity.ok(portfolios);
    }

    // 3. Get portfolio summary (for pie/donut chart)
    //    Returns total invested value broken down by asset type with percentages
    @GetMapping("/{portfolioId}/summary")
    public ResponseEntity<?> getPortfolioSummary(@PathVariable Long portfolioId) {
        try {
            PortfolioSummaryDto summary = assetService.getPortfolioSummary(portfolioId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
