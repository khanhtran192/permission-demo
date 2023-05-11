package com.example.permissiondemo.config.filter;


import com.example.permissiondemo.redis.Permission;
import com.example.permissiondemo.redis.service.CacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String id =  request.getHeader("user-id");
        String role;
        if (token == null){
            log.error("token not found, permission fail");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Optional<Permission> permission = cacheService.getPermissionCache(token);
            if (permission.isPresent()){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        null, null, permission.get().getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                /**
                 * request keycloak get role by id
                 * List<MemberGroup> memberGroups = keyCloak.getUserService(id);
                 * --> response: group: PM, sub function = SF_PM
                 * role = PM;
                 * **/
                role = "PM";
                cacheService.createPermissionCache(Permission.builder()
                                .token(token)
                                .role(role)
                                .id(id)
                                .build());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        null, null, permission.get().getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
