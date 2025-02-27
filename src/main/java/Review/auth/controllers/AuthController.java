package Review.auth.controllers;

import Review.auth.Dtos.AuthResponse;
import Review.auth.Dtos.LoginRequest;
import Review.auth.Dtos.RegisterRequest;
import Review.auth.services.AuthService;
import Review.auth.services.JwtService;
import Review.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
            return ResponseEntity.ok().body(this.authService.login(request));
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok().body(this.authService.register(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){


        try {
            if(token == null || !token.startsWith("Bearer ")) return ResponseEntity.badRequest().body(Map.of("valid", false));

            token = token.substring(7);
            UserDetails user = this.userDetailsService.loadUserByUsername(this.jwtService.getUsernameFromToken(token));

            if(!this.jwtService.isTokenValid(token, user)) return ResponseEntity.ok().body(Map.of("valid", false));

            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", user.getUsername(),
                    "role", user.getAuthorities(),
                    "permissions", this.roleService
                                    .getPermissions(user.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority() ).collect(Collectors.toList()))

            )
            );
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid",false));
        }

    }

}
