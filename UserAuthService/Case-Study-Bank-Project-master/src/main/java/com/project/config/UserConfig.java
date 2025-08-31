@Configuration
@EnableWebSecurity
public class UserConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // stronger hashing
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
            .cors(withDefaults()) // allow CORS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/user/register", 
                    "/user/register/verification",
                    "/user/login",
                    "/user/login/verification",
                    "/user/updPwd",
                    "/user/verifyUpd",
                    "/user/enable2fa",
                    "/actuator/**",               // health check
                    "/swagger-ui/**", "/v3/api-docs/**" // API docs
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
