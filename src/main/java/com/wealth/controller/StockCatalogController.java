package com.wealth.controller;

import com.wealth.dto.AssetResponseDto;
import com.wealth.dto.BuyFromCatalogRequestDto;
import com.wealth.dto.StockCatalogDto;
import com.wealth.enums.AssetType;
import com.wealth.service.StockCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class StockCatalogController {

    @Autowired
    private StockCatalogService stockCatalogService;

    // 1. Get all catalog items (optional ?type= filter)
    //    GET /api/catalog              → all items
    //    GET /api/catalog?type=STOCK   → only stocks
    //    GET /api/catalog?type=MUTUAL_FUND
    //    GET /api/catalog?type=BOND
    @GetMapping
    public ResponseEntity<List<StockCatalogDto>> getCatalog(
            @RequestParam(required = false) AssetType type) {
        if (type != null) {
            return ResponseEntity.ok(stockCatalogService.getCatalogByType(type));
        }
        return ResponseEntity.ok(stockCatalogService.getAllCatalogItems());
    }

    // 2. Buy from catalog — user enters investment amount, system calculates quantity
    //    POST /api/catalog/buy
    @PostMapping("/buy")
    public ResponseEntity<?> buyFromCatalog(@RequestBody BuyFromCatalogRequestDto request) {
        try {
            AssetResponseDto response = stockCatalogService.buyFromCatalog(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
