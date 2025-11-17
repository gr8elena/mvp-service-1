package com.example.mvp_service_1.config.security;

import com.example.mvp_service_1.config.multitenant.TenantContext;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MultiTenantJwtDecoder implements JwtDecoder {

    private final Map<String, JwtDecoder> decoders;

    public MultiTenantJwtDecoder(Map<String, JwtDecoder> decoders) {
        this.decoders = decoders;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        String tenant = TenantContext.getCurrentTenant();

        if (tenant == null) {
            throw new JwtException("Tenant not resolved – cannot validate token");
        }

        JwtDecoder decoder = decoders.get(tenant);

        if (decoder == null) {
            throw new JwtException("No JWT decoder configured for tenant: " + tenant);
        }

        return decoder.decode(token);
    }
}
