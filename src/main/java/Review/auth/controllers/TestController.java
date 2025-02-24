package Review.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("/uno")
    public ResponseEntity<String> uno() {
        return ResponseEntity.ok("uno");
    }

    @GetMapping("/dos")
    public ResponseEntity<String> dos() {
        return ResponseEntity.ok("dos");
    }

    @GetMapping("/tres")
    public ResponseEntity<String> tres() {
        return ResponseEntity.ok("tres");
    }
}
