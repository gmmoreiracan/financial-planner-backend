package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.BillRequestDTO;
import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Bill;
import com.bisnagles.financial_planner_backend.service.AccountService;
import com.bisnagles.financial_planner_backend.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Bill createBill(@org.jetbrains.annotations.NotNull @RequestBody BillRequestDTO billRequestDTO) {
        // Fetch the account using accountId from the DTO
        Account account = accountService.getAccountById(billRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Call the service to create a Bill with the associated Account
        return billService.createBill(billRequestDTO, account);
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Other account-related endpoints...
}

