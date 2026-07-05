package com.wealth.repository;

import com.wealth.entity.StockCatalog;
import com.wealth.enums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCatalogRepository extends JpaRepository<StockCatalog, Long> {

    // Filter catalog by asset type (for STOCK / MUTUAL_FUND / BOND tabs in Angular)
    List<StockCatalog> findByAssetType(AssetType assetType);
}
