package com.jango.assignmenttracker.security;

import com.jango.assignmenttracker.model.User;
import com.jango.assignmenttracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserImpl implements AuthenticatedUserFacade {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getName() {
        return this.getAuthentication().getName();
    }

    @Override
    public User getUser() {
        return this.getName() != null ? userRepo.findByEmail(this.getName()).get() : null;
    }

}
