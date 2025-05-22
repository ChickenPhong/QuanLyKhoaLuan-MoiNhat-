/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.filters;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtFilter implements Filter{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI();

        // Chỉ kiểm tra token với các request bắt đầu bằng /api/secure
        if (uri.startsWith(contextPath + "/api/secure")
                || uri.startsWith(contextPath + "/api/tieuchi")
                || uri.startsWith(contextPath + "/api/hoidong")) {
            
            String header = httpRequest.getHeader("Authorization");
            
            if (header == null || !header.startsWith("Bearer ")) {
                ((HttpServletResponse) response).sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Missing or invalid Authorization header.");
                return;
            } else {
                String token = header.substring(7);
                try {
                    String username = JwtUtils.validateTokenAndGetUsername(token);
                    if (username != null) {
                        httpRequest.setAttribute("username", username);
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    // Log lỗi nếu cần
                }
            }

            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_UNAUTHORIZED, 
                "Token không hợp lệ hoặc hết hạn");
            return;
        }

        // Các request không thuộc /api/secure bỏ qua filter
        chain.doFilter(request, response);
    }
}
