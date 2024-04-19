/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Configuration;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author nqhkt
 */
public class CustomAuthentication implements Authentication {

    private String apiKey;
    private boolean authenticated = true;

    public CustomAuthentication(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return apiKey;
    }

    // Cài đặt các phương thức khác của Authentication
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về một danh sách các quyền truy cập dựa trên API key
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_BACKEND")); // Ví dụ: quyền ROLE_BACKEND
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
