package com.reonfernandes.Taskify.configuration;

import com.reonfernandes.Taskify.services.impl.CustomSecurityUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    private final CustomSecurityUserDetailsService userDetailsService;
    private final OAuthSuccessHandler oAuthSuccessHandler;

    public SecurityConfiguration(CustomSecurityUserDetailsService userDetailsService, OAuthSuccessHandler oAuthSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
       httpSecurity.authorizeHttpRequests(authorize -> {
          authorize.requestMatchers("/user/**").authenticated();
          authorize.anyRequest().permitAll();
       });

       httpSecurity.formLogin(login ->{
           login.loginPage("/login");
           login.loginProcessingUrl("/authenticate");
           login.defaultSuccessUrl("/user/dashboard");
           login.usernameParameter("email");
           login.passwordParameter("password");
       });

       httpSecurity.logout(logout ->{
          logout.logoutUrl("/logout");
          logout.logoutSuccessUrl("/login?logout=true");
       });

       httpSecurity.oauth2Login(oauth ->{
          oauth.loginPage("/login");
          oauth.successHandler(oAuthSuccessHandler);
       });

       return httpSecurity.build();
    }
}
