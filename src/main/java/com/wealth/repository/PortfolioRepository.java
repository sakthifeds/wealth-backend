package com.wealth.repository;

import com.wealth.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // Derived Query: Find all portfolios that belong to a specific user ID
    List<Portfolio> findByUserId(Long userId);
}
