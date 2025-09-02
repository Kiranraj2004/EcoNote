package EcoNote.com.Econote.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all endpoints without login
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable Basic Auth prompt
                .formLogin(formLogin -> formLogin.disable()); // Disable form login

        return http.build();
    }
}

