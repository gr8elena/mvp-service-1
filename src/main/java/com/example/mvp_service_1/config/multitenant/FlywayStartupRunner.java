package com.example.mvp_service_1.config.multitenant;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class FlywayStartupRunner implements CommandLineRunner {

    private final TenantRoutingDataSource routingDataSource;

    public FlywayStartupRunner(TenantRoutingDataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }

    @Override
    public void run(String... args) {
        Map<Object, DataSource> dataSources = routingDataSource.getResolvedDataSources();
        dataSources.forEach((tenantId, ds) -> {
            System.out.println("[Flyway] Starting migration for tenant: " + tenantId);
            Flyway flyway = Flyway.configure()
                    .dataSource(ds)
                    .locations("classpath:db/migration")
                    .schemas("public")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            System.out.println("[Flyway] Migration completed for tenant: " + tenantId);
        });
        System.out.println("[Flyway] All tenants migrated successfully.");
    }
}
