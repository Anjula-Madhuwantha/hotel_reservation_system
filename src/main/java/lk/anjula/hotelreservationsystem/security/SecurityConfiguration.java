//package lk.anjula.hotelreservationsystem.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfiguration {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final AuthenticationProvider authenticationProvider;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors() // ✅ Enable CORS support
//                .and()
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers(antMatchers(HttpMethod.POST, "/api/customers/sign-up")).permitAll();
//                    auth.requestMatchers(antMatchers(HttpMethod.POST, "/api/customers/sign-in")).permitAll();
//                    auth.requestMatchers("/error/**").permitAll();
//                    auth.requestMatchers("/api/customers/**").permitAll();
//                    auth.anyRequest().authenticated();
//                })
//                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    private RequestMatcher antMatchers(HttpMethod httpMethod, String antPattern) {
//        return new AntPathRequestMatcher(antPattern, httpMethod.name());
//    }
//}


package lk.anjula.hotelreservationsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // ✅ Enable CORS support
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    // Public endpoints
                    auth.requestMatchers(antMatchers(HttpMethod.POST, "/api/customers/sign-up")).permitAll();
                    auth.requestMatchers(antMatchers(HttpMethod.POST, "/api/customers/sign-in")).permitAll();
                    auth.requestMatchers("/error/**").permitAll();
                    auth.requestMatchers("/api/customers/**").permitAll();

                    // Reservation endpoints for ADMIN and CUSTOMER roles
                    auth.requestMatchers(antMatchers(HttpMethod.GET, "/api/reservations/**")).hasAnyRole("ADMIN", "CUSTOMER");
                    auth.requestMatchers(antMatchers(HttpMethod.POST, "/api/reservations/**")).hasAnyRole("ADMIN", "CUSTOMER");
                    auth.requestMatchers(antMatchers(HttpMethod.PUT, "/api/reservations/**")).hasAnyRole("ADMIN", "CUSTOMER");
                    auth.requestMatchers(antMatchers(HttpMethod.DELETE, "/api/reservations/**")).hasAnyRole("ADMIN", "CUSTOMER");

                    // Room endpoints for ADMIN and CUSTOMER roles
                    auth.requestMatchers(antMatchers(HttpMethod.GET, "/api/rooms/**")).hasAnyRole("ADMIN", "CUSTOMER");

                    // All other requests require authentication
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private RequestMatcher antMatchers(HttpMethod httpMethod, String antPattern) {
        return new AntPathRequestMatcher(antPattern, httpMethod.name());
    }
}