package com.bisnagles.financial_planner_backend;
import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FinancialPlannerBackendApplication {

	public static void main(String[] args) {
//		Sentry.init(options -> {
//			options.setDsn("https://cbcc39540abd9729da67041c002a9eb3@o4507933209001984.ingest.us.sentry.io/4507994521206784");
//			options.setTracesSampleRate(1.0);
//			options.setDebug(true); // Enable debug mode
//			options.getOutboxPath();
//		});

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				Sentry.close(); // Ensure Sentry is properly closed on shutdown
			} catch (Exception e) {
				// Handle exception if needed
			}
		}));

		SpringApplication.run(FinancialPlannerBackendApplication.class, args);
	}

}
