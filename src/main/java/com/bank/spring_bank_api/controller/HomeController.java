package com.bank.spring_bank_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "<strong>Spring Bank API</strong> <br>Integrantes: <br>RM555276 - Pedro Henrique Mendonça de Novais <br> Rodrigo Alcides Bohac Rios";
    }
}
