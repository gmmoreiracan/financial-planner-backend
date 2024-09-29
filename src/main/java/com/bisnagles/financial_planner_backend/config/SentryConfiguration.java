package com.bisnagles.financial_planner_backend.config;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://cbcc39540abd9729da67041c002a9eb3@o4507933209001984.ingest.us.sentry.io/4507994521206784")
@Configuration
class SentryConfiguration {
}
