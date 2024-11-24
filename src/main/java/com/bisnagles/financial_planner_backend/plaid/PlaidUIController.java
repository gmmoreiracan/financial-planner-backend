package com.bisnagles.financial_planner_backend.plaid;

import com.bisnagles.financial_planner_backend.config.AuditorAwareImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/plaid")
public class PlaidUIController {

    @Autowired
    private AuditorAwareImpl auditorAware;

    @GetMapping("/connect-bank")
    public String getConnectBank(Model model){
        Long clientId = auditorAware.getCurrentAuditorId().orElseThrow();

        model.addAttribute("clientId",clientId);
        return "connect-bank";
    }
}
