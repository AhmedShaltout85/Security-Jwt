package com.a08r.Security_Jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/free-access")
@RequiredArgsConstructor
public class FreeAccessController {


    @GetMapping()
    public ResponseEntity<String> get() {
        return ResponseEntity.ok().body("GET: Any User Can GET All Data");
    }

    @PostMapping()
    public ResponseEntity<String> post() {
        return ResponseEntity.ok().body("POST: Any User Can POST Data");
    }

    @PutMapping()
    public ResponseEntity<String> put() {
        return ResponseEntity.ok().body("PUT: Any User Can PUT Data");
    }

    @DeleteMapping()
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok().body("DELETE: Any User Can DELETE Data");
    }


}
