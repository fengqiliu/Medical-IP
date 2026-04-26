package com.medical360.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class DataPermissionInterceptor implements HandlerInterceptor {

    private final PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        if (path.startsWith("/api/patient/")) {
            String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
            if (username == null) return true;

            String[] parts = path.split("/");
            if (parts.length >= 4) {
                try {
                    Long patientId = Long.parseLong(parts[parts.length - 1]);
                    if (!permissionService.canAccessPatient(username, patientId)) {
                        response.setStatus(403);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Not a patient ID path, allow
                }
            }
        }
        return true;
    }
}
