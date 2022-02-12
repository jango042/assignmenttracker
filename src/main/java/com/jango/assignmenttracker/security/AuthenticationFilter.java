package com.jango.assignmenttracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jango.assignmenttracker.SpringApplicationContext;
import com.jango.assignmenttracker.config.SecurityConstants;
import com.jango.assignmenttracker.model.Roles;
import com.jango.assignmenttracker.model.User;
import com.jango.assignmenttracker.pojo.LoginDetailsPojo;
import com.jango.assignmenttracker.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Gson gson = new Gson();




    public AuthenticationFilter(AuthenticationManager manager) {
        super.setAuthenticationManager(manager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginDetailsPojo creds = new ObjectMapper().readValue(req.getInputStream(), LoginDetailsPojo.class);
            UserRepository userLoginRepo = (UserRepository) SpringApplicationContext.getBean("userRepository");
            User user = userLoginRepo.findByEmail(creds.getEmail()).orElseThrow(() -> new BadCredentialsException("User Does not exist"));

            List<Roles> roles = user.getRolesList();
            List<GrantedAuthority> grantedAuthorities = roles.stream().map(r -> {

                return new SimpleGrantedAuthority(r.getName());
            }).collect(Collectors.toList());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), grantedAuthorities));
            // usersService.getAuthorities(usersService.getUserRoles(creds.getEmail()))

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException, SignatureException {

        String userName = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();

        log.info("==============About Login in==========");
        String token = Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getSecret()).compact();

        UserRepository userLoginRepo = (UserRepository) SpringApplicationContext.getBean("userRepository");

        User user = userLoginRepo.findByEmail(userName).get();

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);


        Map m = new HashMap();
        m.put("code", 0);
        m.put("message", "Login successful");
        m.put("token", SecurityConstants.TOKEN_PREFIX + token);
        List<String> roles = new ArrayList<>();
        List<Roles> rs = user.getRolesList();
        //userLoginRepo.findByUsername(userName)
        for (int i = 0; i < rs.size(); i++) {
            roles.add(rs.get(i).getName());

        }
        m.put("roles", roles);

        String str = gson.toJson(m);
        PrintWriter pr = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        pr.write(str);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.ALL_VALUE);
        Map m = new HashMap();
        m.put("code", -1);
        m.put("message", "usuccessful, invalid username or password");
        String str = gson.toJson(m);
        PrintWriter pr = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        pr.write(str);
    }

}
