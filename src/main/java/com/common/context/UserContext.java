package com.common.context;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class UserContext {
	  private final UUID userId;
	    private final String username;
	    private final String email;
	    private final List<String> roles;
	    private final String active;

	    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

	    public static void setContext(UserContext context) {
	        CONTEXT.set(context);
	    }

	    public static UserContext getContext() {
	        return CONTEXT.get();
	    }

	    public static void clear() {
	        CONTEXT.remove();
	    }
}