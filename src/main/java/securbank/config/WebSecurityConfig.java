package securbank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import securbank.authentication.CustomAuthenticationProvider;
import securbank.authentication.CustomAuthenticationSuccessHandler;
import securbank.services.UserService;

/**
 * @author Ayush Gupta
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
//	@Autowired
//	CustomAuthenticationSuccessHandler authSuccessHandler;
	
//	@Autowired
//	private CustomAuthenticationProvider customAuthenticationProvider;
//	

	@Autowired
	private UserService userService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {


		http.formLogin().
		loginPage("/login")
        .permitAll()
        .and()
        .authorizeRequests()
	    	.antMatchers("/admin/**").hasRole("ADMIN")
	    	.antMatchers("/user/**").hasRole("INDIVIDUAL")
	    	.antMatchers("/merchant/**").hasRole("MERCHANT")
	    	.antMatchers("/employee/**").hasRole("EMPLOYEE")
	    	.antMatchers("/manager/**").hasRole("MANAGER")
	    	.antMatchers("/changepassword/").hasAnyRole()
    	.and()
        .logout()
            .permitAll()
		.and()
			.exceptionHandling().accessDeniedPage("/error/access-denied")
		.and()
		.x509().userDetailsService((UserDetailsService)userService);

    }
//
//	@Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    	auth.authenticationProvider(this.customAuthenticationProvider);
//    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationTrustResolver trustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
}
