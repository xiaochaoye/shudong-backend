package com.shudong.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private List<String> publicEndpoints;
    private List<String> adminEndpoints;

    public List<String> getPublicEndpoints() {
        return publicEndpoints;
    }

    public void setPublicEndpoints(List<String> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    public List<String> getAdminEndpoints() {
        return adminEndpoints;
    }

    public void setAdminEndpoints(List<String> adminEndpoints) {
        this.adminEndpoints = adminEndpoints;
    }
}
