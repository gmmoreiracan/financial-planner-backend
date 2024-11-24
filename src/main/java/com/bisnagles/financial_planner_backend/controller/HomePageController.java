package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO;
import com.bisnagles.financial_planner_backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.*;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public String getMonthlyTransactionSummary(Model model) {
        List<TransactionSummaryDTO> summaries = transactionService.getTransactionSummaryForCurrentMonth();
        model.addAttribute("summaries", summaries);
        return "summary"; // Corresponds to a Thymeleaf template named "summary.html"
    }
}

