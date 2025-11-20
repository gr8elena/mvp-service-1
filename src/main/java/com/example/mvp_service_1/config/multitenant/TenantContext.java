package com.example.mvp_service_1.config.multitenant;

public class TenantContext {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        CONTEXT.set(tenant);
    }

    public static String getCurrentTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static String getCurrentRealm() {
        return getCurrentTenant() + "-realm";
    }
}