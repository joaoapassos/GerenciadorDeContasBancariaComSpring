package app.controllers.contas;

import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.conta.ContaCorrenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/contas/corrente")
public class ContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public ContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }
    
    @GetMapping
    public List<ContaCorrente> getManyContas(){
        return contaCorrenteService.carregarContas();
    }

    @GetMapping("/{id}")
    public ContaCorrente getContaById(@PathVariable int id){
        return contaCorrenteService.buscarContaPorId(id);
    }

    @PostMapping("/acessar")
    public ContaCorrente loginConta(@RequestBody ContaCorrente conta){
        return contaCorrenteService.loginConta(conta);
    }

    @PostMapping("/cadastro")
    public void postConta(@RequestBody ContaCorrente newConta){
        contaCorrenteService.cadastrarConta(newConta);
    }

    @PutMapping
    public void putConta(@RequestBody ContaCorrente conta){
        contaCorrenteService.atualizarConta(conta);
    }

    @DeleteMapping
    public void deleteConta(@RequestBody ContaCorrente conta){
        contaCorrenteService.deletarConta(conta);
    }
}
