package org.example.gatewayserver.entity.dto;

public record RefreshResult(
        String newSessionId,
        boolean rememberMe,
        boolean success,
        String errorMsg
) {}
