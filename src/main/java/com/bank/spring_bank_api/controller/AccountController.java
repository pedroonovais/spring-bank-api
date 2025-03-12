package com.bank.spring_bank_api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bank.spring_bank_api.model.Account;

@RestController
@RequestMapping("/account")
public class AccountController {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Account> repository = new ArrayList<>();

    @GetMapping
    public List<Account> index() {
        return repository;
    }

    @PostMapping
    public ResponseEntity<?> create (@RequestBody Account account) {
        log.info("Criando uma nova conta " + account);        

        Map<String, String> errorResponse = new HashMap<>();

        // Verificando se a data de criação da conta é valida ou está no futuro
        if (account.getOpeningDate().after(new java.util.Date())) {
            System.out.println("A data de abertura da conta nao pode ser no futuro");
            return ResponseEntity.badRequest().build();
        }

        // Verificando se o nome do titular, cpf existe
        if (account.getHolder() == null || account.getCpf() == 0) {
            System.out.println("O nome do titular e o cpf nao podem ser nulos");
            return ResponseEntity.badRequest().build();
        }

        // Verificando se o saldo da conta é valido
        if (account.getBalance() < 0) {
            System.out.println("O saldo nao pode ser negativo");
            return ResponseEntity.badRequest().build();
        }

        // Verificando se o tipo da conta é valido (Corrente, Poupança ou Salario)
        if (!account.getTypeAccount().equals("Corrente") && !account.getTypeAccount().equals("Poupanca") && !account.getTypeAccount().equals("Corrente")) {
            System.out.println("O tipo da conta nao eh valido: " + account.getTypeAccount());

            
            errorResponse.put("error", "O tipo da conta não é válido.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        repository.add(account);
        return ResponseEntity.status(201).body(account);
    }

    @GetMapping("{id}")
    public Account get(@PathVariable long id){
        log.info("Buscando categoria " + id);
        return getAccount(id);
    }

    private Account getAccount(Long id) {
        return repository.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria " + id + " não encontrada")
                );
    }
}
