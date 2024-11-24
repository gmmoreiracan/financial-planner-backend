package com.bisnagles.financial_planner_backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class FinancialPlannerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialPlannerBackendApplication.class, args);
	}

}
