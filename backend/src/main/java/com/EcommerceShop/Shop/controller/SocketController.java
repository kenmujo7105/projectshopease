package com.EcommerceShop.Shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
public class SocketController {
    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok().body("{\"websocket\": true}");
    }
}
