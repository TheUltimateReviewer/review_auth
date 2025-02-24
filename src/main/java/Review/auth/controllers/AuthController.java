package Review.auth.controllers;

import Review.auth.Dtos.AuthResponse;
import Review.auth.Dtos.LoginRequest;
import Review.auth.Dtos.RegisterRequest;
import Review.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
            return ResponseEntity.ok().body(this.authService.login(request));
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws Exception {
        return ResponseEntity.ok().body(this.authService.register(request));
    }
}
