package securbank.config;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import securbank.authentication.CustomAuthenticationProvider;

/**
 * @author Ayush Gupta
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {


//        http
//        .authorizeRequests()
//	        .antMatchers("/", "/home").permitAll()
//	        .anyRequest().authenticated()
//	        .and()
//        .formLogin()
//            .loginPage("/login")
//            .failureForwardUrl("/login?error")
//            .permitAll()
//            .and()
//        .logout()
//            .permitAll();

        http
        .authorizeRequests()
//        	.antMatchers("/admin/**").access("hasRole('ADMIN')")
	        .antMatchers("/", "/home").permitAll()
//	        .anyRequest().authenticated()
	        .and()
        .formLogin()
            .loginPage("/login")
            //.failureForwardUrl("/login?error")
            .permitAll()
            
            .and()
        .logout()
            .permitAll();

    }

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(this.customAuthenticationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
}
