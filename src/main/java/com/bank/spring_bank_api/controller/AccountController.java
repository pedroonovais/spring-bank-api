package com.bank.spring_bank_api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    // Verificar o porque ele não está retornando o campo active na resposta
    @PostMapping
    public ResponseEntity<Account> create (@RequestBody Account account) {
        log.info("Criando uma nova conta " + account);        
        isValidAccount(account);
        repository.add(account);
        return ResponseEntity.status(201).body(account);
    }

    @GetMapping("{id}")
    public Account get(@PathVariable long id){
        log.info("Buscando categoria " + id);
        return getAccount(id);
    }

    @GetMapping("cpf/{cpf}")
    public Account getByCpf(@PathVariable long cpf){
        log.info("Buscando categoria " + cpf);
        return getAccountByCpf(cpf);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando conta " + id);
        repository.remove(getAccount(id));
    }

    @PutMapping("{id}")
    public Account update(@PathVariable Long id, @RequestBody Account account) {
        log.info("Atualizando conta " + id + " " + account);	

        repository.remove(getAccount(id));
        account.setId(id);
        repository.add(account);

        return account;
    }

    private Account getAccount(Long id) {
        return repository.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria " + id + " não encontrada")
                );
    }

    private Account getAccountByCpf(Long cpf) {
        return repository.stream()
                .filter(c -> c.getCpf() == cpf)
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria " + cpf + " nao encontrada")
                );
    }

    public void isValidAccount(Account account) {
        if (account.getOpeningDate().after(new java.util.Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de abertura da conta nao pode ser no futuro");
        }
        if (account.getHolder() == null || account.getCpf() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do titular e o cpf nao podem ser nulos");
        }
        if (account.getBalance() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo nao pode ser negativo");
        }
        if (!account.getTypeAccount().equals("Corrente") && !account.getTypeAccount().equals("Poupanca") && !account.getTypeAccount().equals("Corrente")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo da conta nao eh valido: " + account.getTypeAccount());
        }
        if (!account.isActive().equals("s") && !account.isActive().equals("n")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo conta ativa deve ser passado como 's' ou 'n'");
        }
        if (repository.stream().anyMatch(a -> a.getCpf() == account.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
        }        
    }
}
