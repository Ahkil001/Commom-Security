package com.common.context;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long customerId;
    private String username;
    private String role;

}