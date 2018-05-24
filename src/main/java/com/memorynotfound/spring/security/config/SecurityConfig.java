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

/**
 * @author Mikkel
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;

    /**
     * Denne metode ignorerer de routes, som er defineret.
     * Det er vi nødt til at gøre for at kunne bruge HTTP POST requests.
     */
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
        web.ignoring().antMatchers("/user-update-password");
        web.ignoring().antMatchers("/user-update-group");
        web.ignoring().antMatchers("/delete-group-post");
    }

    /**
     * Her bliver der defineret hvilke routes man kan tilgå uden at være logget ind, når man er logget ind og med
     * hvilken user role man kan se hvad.
     *
     * Log ind og log ud routes bliver også defineret og der bliver sat en access denied handler, til at håndtere,
     * hvis man ikke har rettigheder til at se den side man ønsker.
     */
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

    /**
     * Her sætter vi de brugere der skal have lov til at logge ind, samt hvilken user role de får når de logger ind.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT email, role FROM Person INNER JOIN PersonRole ON " +
                        "Person.role_id = PersonRole.role_id WHERE email=?")
                .usersByUsernameQuery("select email, password as password, 1 FROM Person where email=?")
                .passwordEncoder(passEncoder());

    }

    /**
     * Her sætter vi password encoderen vi har brugt til at hashe de passwords som allerede er i tabellen.
     * Denne metode bliver brugt til at sammenligne 2 hashede passwords.
     */
    @Bean
    public PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }
}