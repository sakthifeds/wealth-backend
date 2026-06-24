package com.wealth.controller;

import com.wealth.dto.PortfolioRequestDto;
import com.wealth.dto.PortfolioResponseDto;
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
}
