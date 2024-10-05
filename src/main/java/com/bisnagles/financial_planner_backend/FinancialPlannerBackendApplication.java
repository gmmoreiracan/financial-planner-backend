package com.bisnagles.financial_planner_backend;
import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FinancialPlannerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialPlannerBackendApplication.class, args);
	}

}
