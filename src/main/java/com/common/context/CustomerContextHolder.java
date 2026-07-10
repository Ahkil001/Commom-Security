package com.common.context;

public final class CustomerContextHolder {

    private CustomerContextHolder() {
        // Prevent instantiation
    }

    private static final ThreadLocal<CustomerContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(CustomerContext customerContext) {
        CONTEXT.set(customerContext);
    }

    public static CustomerContext getContext() {
        return CONTEXT.get();
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}