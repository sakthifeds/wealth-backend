package com.wealth.repository;

import com.wealth.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // Fetch all assets belonging to a specific portfolio
    List<Asset> findByPortfolioId(Long portfolioId);

    // Check if a specific symbol already exists in a portfolio (to avoid duplicates)
    Optional<Asset> findByPortfolioIdAndSymbol(Long portfolioId, String symbol);
}
