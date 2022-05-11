package com.example.employeeserver.config;


import com.example.employeeserver.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Qualifier("userService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // We don't want to use the default username-password authentication
                .httpBasic().disable()
                // disable csrf
                .csrf().disable()
                // enable cors for Spring Security
                .cors()
                .and()
                // We don't use session anymore
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // before we apply antMatchers
                .authorizeRequests()
//                .antMatchers("/*").permitAll()  // all users are permitted
//                .antMatchers("/**").permitAll()  // all users are permitted
                .antMatchers("/employee-service/**").hasRole("USER")
                .antMatchers("/employee-service/*").hasRole("USER")
//                .antMatchers("/**").hasRole("USER")
//                .antMatchers("/*").hasRole("USER")

                .anyRequest().authenticated()  // any request need to be authenticated
                .and()
                .apply(new JwtConfig(jwtTokenProvider));  // apply our jwt filter
    }
}
