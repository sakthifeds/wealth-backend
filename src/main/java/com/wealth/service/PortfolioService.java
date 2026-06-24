package com.wealth.service;

import com.wealth.dto.PortfolioRequestDto;
import com.wealth.dto.PortfolioResponseDto;
import com.wealth.entity.Portfolio;
import com.wealth.entity.User;
import com.wealth.enums.PortfolioType;
import com.wealth.repository.PortfolioRepository;
import com.wealth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    public PortfolioResponseDto createPortfolio(PortfolioRequestDto request) {
        // 1. Fetch the user from the database
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: User not found with ID " + request.getUserId()));

        // 2. Create and map the new Portfolio entity
        Portfolio portfolio = new Portfolio();
        portfolio.setName(request.getName());
        portfolio.setDescription(request.getDescription());
        portfolio.setUser(user); // Map the relationship!
        // Default to GENERAL if no type is provided
        portfolio.setType(request.getType() != null ? request.getType() : PortfolioType.GENERAL);

        // 3. Save to database
        Portfolio saved = portfolioRepository.save(portfolio);

        // 4. Return the safe DTO
        return new PortfolioResponseDto(saved.getId(), saved.getName(), saved.getDescription(), user.getId(), saved.getType());
    }

    public List<PortfolioResponseDto> getUserPortfolios(Long userId) {
        // 1. Fetch all portfolios for this user
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);

        // 2. Map the List of Entities to a List of DTOs
        return portfolios.stream()
                .map(p -> new PortfolioResponseDto(p.getId(), p.getName(), p.getDescription(), p.getUser().getId(), p.getType()))
                .collect(Collectors.toList());
    }
}
