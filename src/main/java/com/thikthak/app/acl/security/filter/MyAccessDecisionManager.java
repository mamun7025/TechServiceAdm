package com.thikthak.app.acl.security.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import java.util.Collection;
import java.util.Iterator;

public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {


/*        // Traversal role
        for (ConfigAttribute ca : configAttributes) {
            // ① permissions required for the current url request
            String needRole = ca.getAttribute();
            if (Constants.ROLE_LOGIN.equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("Not logged in!");
                } else {
                    throw new AccessDeniedException("Unauthorized url!");
                }
            }

            // ② the role of the current user
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                // Just include one of the roles to access
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("Please contact the administrator to assign permissions!");*/

        System.out.println("I am here... DecicionMgr............555");
        System.out.println(configAttributes);
        System.out.println("I am here... DecicionMgr............666");

        if (CollectionUtils.isEmpty(configAttributes)) {
            throw new AccessDeniedException("not allow");
        }

        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
//            String needRole =  ca.getAttribute();
            String needRole = ((org.springframework.security.access.SecurityConfig) ca).getAttribute();

            for (GrantedAuthority ga : authentication.getAuthorities()) {

                System.out.println("Login user authority: ");
                System.out.println(ga.getAuthority());
                if(ga.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("not allow");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
