package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO;
import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.model.Transaction;
import com.bisnagles.financial_planner_backend.service.AuthService;
import com.bisnagles.financial_planner_backend.service.CategoryService;
import com.bisnagles.financial_planner_backend.service.TransactionService;
import com.bisnagles.financial_planner_backend.service.UIService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;


@Slf4j
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

    @Value("${spring.profiles.active}")
    private String activeProfile;

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

    @RequestMapping("/summary")
    public String getMonthlyTransactionSummary(Model model) {
        List<TransactionSummaryDTO> summaries = transactionService.getTransactionSummaryForCurrentMonth();
        model.addAttribute("summaries", summaries);
        return "summary"; // Corresponds to a Thymeleaf template named "summary.html"
    }

    @RequestMapping("/transactionService/create")
        public String createTransaction(@ModelAttribute TransactionRequestDTO transaction){
        transactionService.createTransaction(transaction);
        return "redirect:/ui/category/" + transaction.getCategory();
    }

    @RequestMapping("/transactionService/delete/{id}")
    public String createTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return "redirect:/ui/summary";
    }

    @RequestMapping("/transactionService/update")
    public String updateTransaction(@ModelAttribute TransactionRequestDTO transactionUpdateDTO){
        Long transactionId = transactionUpdateDTO.getId();
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(transactionId);
        Transaction transaction = optionalTransaction.orElseGet(Transaction::new);

        Optional<Category> optionalCategory = categoryService.getOrCreateCategoryByNameWithOwner(transactionUpdateDTO.getCategory(), transactionUpdateDTO.getOwnerId());

        transaction.setDate(transactionUpdateDTO.getDate());
        transaction.setDescription(transactionUpdateDTO.getDescription());
        transaction.setAmount(transactionUpdateDTO.getAmount());
        transaction.setCategory(optionalCategory.orElseThrow());
        transaction.setDate(transactionUpdateDTO.getDate());

        transactionService.updateTransaction(transaction);
        return "redirect:/ui/category/" + transaction.getCategory().getName();

    }

    @PostMapping("/upload-csv")
    @ResponseBody // Allows returning a plain response for the AJAX call
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        boolean dataStarted = false;
        int formatType = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while (!Objects.equals(line = reader.readLine(), "EOF")) {
                // Skip empty lines
                if (null == line || line.isEmpty() || line.trim().isEmpty()) continue;

                // Identify the table format and header row
                if (!dataStarted) {
                    if (line.contains("Bank Card,Transaction Type,Date Posted, Transaction Amount,Description")) {
                        formatType = 1;
                        dataStarted = true;
                        continue; // Skip header
                    } else if (line.contains("Item #,Card #,Transaction Date,Posting Date,Transaction Amount,Description")) {
                        formatType = 2;
                        dataStarted = true;
                        continue; // Skip header
                    } else {
                        // Continue searching for header row if not found
                        continue;
                    }
                }

                // Process rows based on identified format
                String[] fields = line.split(",");

                try {
                    if (formatType == 1 && fields.length >= 5) {
                        // Format 1 Parsing
                        String cardNumber = fields[0].replace("'", "").trim();
                        String transactionType = fields[1];
                        String transactionDate = fields[2];
                        double amount = Double.parseDouble(fields[3]) * -1;
                        String description = fields[4];

                        TransactionRequestDTO transaction = getTransactionRequestDTO(transactionDate,description,amount,description);

                        transactionService.createTransaction(transaction);

                    } else if (formatType == 2 && fields.length >= 6) {
                        // Format 2 Parsing
                        String itemNumber = fields[0];
                        String cardNumber = fields[1].replace("'", "").trim();
                        String transactionDate = fields[2];
                        String postingDate = fields[3];
                        double amount = Double.parseDouble(fields[4]);
                        String description = fields[5];

                        TransactionRequestDTO transaction = getTransactionRequestDTO(transactionDate,description,amount,description);

                        transactionService.createTransaction(transaction);
                    } else {
                        System.out.println("Skipping malformed line: " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing line: " + line + " - " + e.getMessage());
                }
            }
            return "Upload successful";
        } catch (Exception e) {
            return "Error processing file: " + e.getMessage();
        }
    }

    private static @NotNull TransactionRequestDTO getTransactionRequestDTO(String dateString, String merchant, double amount, String category) {
        TransactionRequestDTO transaction = new TransactionRequestDTO();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        formatter = formatter.withLocale(Locale.CANADA);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDate transactionDate = LocalDate.parse(dateString, formatter);

        transaction.setDate(transactionDate);
        transaction.setMerchant(merchant);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        return transaction;
    }


    @PostMapping("/login")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        // Generate JWT token
        String jwt = authService.authenticateLoginRequest(username,password);

        Cookie jwtCookie = new Cookie("auth_token", jwt);
        jwtCookie.setHttpOnly(true);  // This ensures that the cookie is not accessible by JavaScript
        jwtCookie.setSecure(!"local".equals(activeProfile));
        jwtCookie.setPath("/");       // Available to the entire application
        jwtCookie.setMaxAge(60*60); // 1 hour expiration

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        return "redirect:/ui/summary";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpServletResponse response) {
        // Clear the JWT cookie
        Cookie jwtCookie = new Cookie("auth_token", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        // Optionally invalidate the session
        SecurityContextHolder.clearContext();

        return "redirect:login";
    }
}

