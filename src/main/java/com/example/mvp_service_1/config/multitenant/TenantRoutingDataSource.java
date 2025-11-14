package com.example.mvp_service_1.config.multitenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;


public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.getCurrentTenant();
        return tenant != null ? tenant : "tenant1";
    }

    public DataSource resolveDataSource() {
        Object key = determineCurrentLookupKey();
        Map<Object, DataSource> resolved = getResolvedDataSources();
        DataSource ds = resolved.get(key);
        if (ds == null) {
            throw new IllegalArgumentException("No DataSource configured for tenant: " + key);
        }
        return ds;
    }
}