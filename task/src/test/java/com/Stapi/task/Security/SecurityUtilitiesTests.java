package com.Stapi.task.Security;


import com.Stapi.task.security.GenerateJWT;
import com.Stapi.task.security.JWTAuthenticator;
import com.Stapi.task.security.UserDetailsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SecurityUtilitiesTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTAuthenticator jwtAuthenticator;
    private GenerateJWT generateJWT;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder2;

    public static final SecretKey KEY = Keys.hmacShaKeyFor("YourSuperSecretKeyYourSuperSecretKey".getBytes());

    @Test
    public void TestPasswordEncoder_FunctionsWork(){
        String raw = "test";
        String secret1 = passwordEncoder.encode(raw);
        String secret2 = passwordEncoder.encode(raw);

        Assertions.assertThat(secret1).isNotEqualTo(secret2);
        Assertions.assertThat(raw).isNotEqualTo(secret1);
        Assertions.assertThat(secret1).isNotNull();
    }

    @Test
    public void TestAuthenticationManager_Correct(){
        UserDetails user = User.withUsername("Stapi")
                .password("Test")
                .build();

        when(userDetailsService.loadUserByUsername("Stapi")).thenReturn(user);
        when(passwordEncoder2.matches("Test", user.getPassword())).thenReturn(true);

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("Stapi", "Test");

        Authentication authentication = authenticationManager.authenticate(authRequest);

        assertNotNull(authentication);
        assertEquals("Stapi", authentication.getName());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    public void TestAuthenticationManager_Wrong(){
        UserDetails user = User.withUsername("Stapi")
                .password("Test")
                .build();

        when(userDetailsService.loadUserByUsername("Stapi")).thenReturn(user);
        when(passwordEncoder2.matches("test", user.getPassword())).thenReturn(false);

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("Stapi", "test");


        assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(authRequest));
    }

    @Test
    public void TestGenerator(){
        generateJWT = new GenerateJWT();
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("Stapi", "test");
        String token = generateJWT.generateToken(authRequest);

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(generateJWT.extractUsername(token)).isEqualTo("Stapi");


    }
}
