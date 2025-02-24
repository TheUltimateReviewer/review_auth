package Review.auth.config;

import Review.auth.config.jwt.JwtAuthenticationFilter;
import Review.auth.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private  CorsConfig corsConfig;


    //filter chain primer elemento a crear
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailService userDetailService) throws Exception {



        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors ->cors.configurationSource(this.corsConfig.corsConfigurationSource()) )
                .httpBasic(Customizer.withDefaults())   
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpconf ->{

                    //donde defino el funcionamiento
                    httpconf.requestMatchers(HttpMethod.GET,"test/uno").permitAll();
                    httpconf.requestMatchers("test/dos").authenticated();
                    httpconf.requestMatchers(HttpMethod.POST,"/auth/**").permitAll();
                    httpconf.anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider(userDetailService))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // usando el authrentication configuration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    //creando el provider
    @Bean
    //uso mi user detail servide tuneado
    public AuthenticationProvider authenticationProvider(UserDetailService userDetailService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        //los dos componentes del authentication provider
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        return daoAuthenticationProvider;
    }



    //userDetailsService
    /*
    public UserDetailsService userDetailsService() {
        List<UserDetails> userDetails = new ArrayList<UserDetails>();
        userDetails.add(User.withUsername("user")
                .password("123")
                .roles("USER")
                .authorities("READ","CREATE")
                .build()
        );
        return new InMemoryUserDetailsManager(userDetails);

    }
    */

    //password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
