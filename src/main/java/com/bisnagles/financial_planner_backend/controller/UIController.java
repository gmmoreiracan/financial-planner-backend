package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO;
import com.bisnagles.financial_planner_backend.dto.TransactionUpdateDTO;
import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.model.Merchant;
import com.bisnagles.financial_planner_backend.model.Transaction;
import com.bisnagles.financial_planner_backend.service.AuthService;
import com.bisnagles.financial_planner_backend.service.CategoryService;
import com.bisnagles.financial_planner_backend.service.TransactionService;
import com.bisnagles.financial_planner_backend.service.UIService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ui")
public class UIController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UIService uiService;

    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "login";
    }

    @GetMapping("/category/{categoryName}")
    public String getCategoryPage(Model model, @PathVariable String categoryName){
        List<Category> categoryList = categoryService.getAllCategories();
        List<Transaction> transactionsList = transactionService.getTransactionSummaryForCurrentMonthAndCategory(categoryName,null,null);
        model.addAttribute("transactions",transactionsList);
        model.addAttribute("categories",categoryList);
        model.addAttribute("transaction",new Transaction());
        return "category-details";
    }

    @GetMapping("/summary")
    public String getMonthlyTransactionSummary(Model model) {
        List<TransactionSummaryDTO> summaries = transactionService.getTransactionSummaryForCurrentMonth();
        model.addAttribute("summaries", summaries);
        return "summary"; // Corresponds to a Thymeleaf template named "summary.html"
    }

    @RequestMapping("/transaction/create")
        public String createTransaction(@ModelAttribute TransactionRequestDTO transaction){
        transactionService.createTransaction(transaction);
        return "redirect:/ui/category/" + transaction.getCategory();
    }

    @RequestMapping("/transaction/delete/{id}")
    public String createTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return "redirect:/ui/summary";
    }

    @RequestMapping("/transaction/update")
    public String updateTransaction(@ModelAttribute TransactionUpdateDTO transactionUpdateDTO){
        Long transactionId = transactionUpdateDTO.getId();
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(transactionId);
        Transaction transaction = optionalTransaction.orElseGet(Transaction::new);;

        Optional<Category> optionalCategory = categoryService.getOrCreateCategoryByName(transactionUpdateDTO.getCategory());

        transaction.setDate(transactionUpdateDTO.getDate());
        transaction.setDescription(transactionUpdateDTO.getDescription());
        transaction.setAmount(transactionUpdateDTO.getAmount());
        transaction.setCategory(optionalCategory.orElseThrow());
        transaction.setDate(transactionUpdateDTO.getDate());

        transactionService.updateTransaction(transaction);
        return "redirect:/ui/category/" + transaction.getCategory().getName();

    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        // Generate JWT token
        String jwt = authService.authenticateLoginRequest(username,password);

        Cookie jwtCookie = new Cookie("auth_token", jwt);
        jwtCookie.setHttpOnly(true);  // This ensures that the cookie is not accessible by JavaScript
        jwtCookie.setSecure(false);    // Ensures the cookie is only sent over HTTPS (set to true in production)
        jwtCookie.setPath("/");       // Available to the entire application
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 day expiration

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        return "summary";
    }
}

