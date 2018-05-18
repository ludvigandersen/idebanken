package com.memorynotfound.spring.security.config;

import com.memorynotfound.spring.security.web.LoggingAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/create-user-post");
        web.ignoring().antMatchers("/create-idea-post");
        web.ignoring().antMatchers("/delete-user-post");
        web.ignoring().antMatchers("/create-group-post");
        web.ignoring().antMatchers("/aply-for-idea-post");
        web.ignoring().antMatchers("/edit-idea-post");
        web.ignoring().antMatchers("/delete-idea-post");
        web.ignoring().antMatchers("/add-group-member-post");
        web.ignoring().antMatchers("/user-update-person");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**",
                            "/create-developer",
                            "/all-developers",
                            "/contact",
                            "/about",
                            "/all-ideas",
                            "/create-user",
                            "/create-user-email",
                            "user/confirm-created-user").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/idea/**").hasRole("IDEA")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/redirect")
                    .permitAll()
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
    }

    @Autowired
    @Qualifier("datasource")
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // password = $2a$10$GkHRhh4AHWS.WHUzRucUIeBoEmowH7qZ2HLVas544VbXFscstpEE6
        auth.jdbcAuthentication().dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT email, role FROM Person INNER JOIN PersonRole ON " +
                        "Person.role_id = PersonRole.role_id WHERE email=?")
                .usersByUsernameQuery("select email, password as password, 1 FROM Person where email=?")
                .passwordEncoder(passEncoder());

    }

    @Bean
    public PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }
}