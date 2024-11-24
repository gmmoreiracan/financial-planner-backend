package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.dto.BillRequestDTO;
import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Bill;
import com.bisnagles.financial_planner_backend.repository.persistence.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill createBill(BillRequestDTO billRequestDTO, Account account) {
        // Map the DTO to a Bill entity
        Bill bill = new Bill();
        bill.setName(billRequestDTO.getName());
        bill.setAmount(billRequestDTO.getAmount());
        bill.setDueDate(billRequestDTO.getDueDate());
        bill.setPaid(billRequestDTO.isPaid());
        bill.setAccount(account);  // Set the associated account

        // Save and return the bill
        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    // Other account-related methods...
}

