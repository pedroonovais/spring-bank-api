package com.bank.spring_bank_api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bank.spring_bank_api.dto.PixRequest;
import com.bank.spring_bank_api.dto.TransactionRequest;
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
    public ResponseEntity<Account> create (@RequestBody Account account) {
        log.info("Criando uma nova conta " + account);        
        isValidAccount(account);
        repository.add(account);
        return ResponseEntity.status(201).body(account);
    }

    @GetMapping("{id}")
    public Account get(@PathVariable long id){
        log.info("Buscando conta " + id);
        System.out.println(getAccount(id).isActive());
        return getAccount(id);
    }

    @GetMapping("cpf/{cpf}")
    public Account getByCpf(@PathVariable String cpf){
        log.info("Buscando conta " + cpf);
        return getAccountByCpf(cpf);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Alterando status da conta para inativa " + id);
        
        var account = getAccount(id);
        account.setActive('n');
    }

    @PutMapping("{id}")
    public Account update(@PathVariable Long id, @RequestBody Account account) {
        log.info("Atualizando conta " + id + " " + account);	

        repository.remove(getAccount(id));
        account.setId(id);
        repository.add(account);

        return account;
    }

    @PatchMapping("deposit")
    public Account deposit(@RequestBody TransactionRequest depositInfo) {
        var account = getAccount(depositInfo.getId());
        
        if (depositInfo.getValue() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do deposito deve ser maior que 0");
        }

        log.info("Depositando valor R$" + String.valueOf(depositInfo.getValue()) + " na conta " + depositInfo.getId());

        account.setBalance(account.getBalance() + depositInfo.getValue());
        return account;
    }

    @PatchMapping("withdrawal")
    public Account withdrawal(@RequestBody TransactionRequest withdrawalInfo) {
        var account = getAccount(withdrawalInfo.getId());

        if (account.getBalance() < withdrawalInfo.getValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        log.info("Sacando valor R$" + String.valueOf(withdrawalInfo.getValue()) + " da conta " + withdrawalInfo.getId());

        account.setBalance(account.getBalance() - withdrawalInfo.getValue());
        return account;
    }

    @PostMapping("pix")
    public Account pix(@RequestBody PixRequest pixInfo) {
        var originAccount = getAccount(pixInfo.getOriginId());
        var destinationAccount = getAccount(pixInfo.getDestinationId());

        if (originAccount.getBalance() < pixInfo.getValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        log.info("Enviando valor R$" + String.valueOf(pixInfo.getValue()) + " da conta " + pixInfo.getOriginId() + " para a conta " + pixInfo.getDestinationId());

        originAccount.setBalance(originAccount.getBalance() - pixInfo.getValue());
        destinationAccount.setBalance(destinationAccount.getBalance() + pixInfo.getValue());
        return originAccount;
    }

    private Account getAccount(Long id) {
        return repository.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta " + id + " não encontrada")
                );
    }

    private Account getAccountByCpf(String cpf) {
        return repository.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta com CPF " + cpf + " não encontrada")
                );
    }

    public void isValidAccount(Account account) {
        validateOpeningDate(account.getOpeningDate());
        validateHolderAndCpf(account.getHolder(), account.getCpf());
        validateBalance(account.getBalance());
        validateAccountType(account.getTypeAccount());
        validateAccountStatus(account.isActive());
        validateUniqueCpf(account.getCpf());
    }

    private void validateOpeningDate(Date openingDate) {
        if (openingDate.after(new Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de abertura da conta não pode ser no futuro");
        }
    }

    private void validateHolderAndCpf(String holder, String cpf) {
        if (holder == null || cpf == "" || cpf == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do titular e o CPF não podem ser nulos");
        }
    }

    private void validateBalance(double balance) {
        if (balance < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O saldo não pode ser negativo");
        }
    }

    private void validateAccountType(String typeAccount) {
        Set<String> validTypes = Set.of("Corrente", "Poupanca", "Salario");
        if (!validTypes.contains(typeAccount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo da conta não é válido: " + typeAccount);
        }
    }

    private void validateAccountStatus(char status) {
        if (status != 's' && status != 'n') {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo conta ativa deve ser 's' ou 'n'");
        } 
    }

    private void validateUniqueCpf(String cpf) {
        if (repository.stream().anyMatch(a -> a.getCpf().equals(cpf))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
        }
    }    

}
