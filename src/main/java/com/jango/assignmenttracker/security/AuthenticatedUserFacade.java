package com.jango.assignmenttracker.security;

import com.jango.assignmenttracker.model.User;
import org.springframework.security.core.Authentication;

public interface AuthenticatedUserFacade {

    Authentication getAuthentication();

    String getName();
    User getUser();
}
