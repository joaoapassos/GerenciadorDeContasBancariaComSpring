package app.controllers.contas;

import app.enums.TarifaEnum;
import app.exception.SaldoInsuficienteException;
import app.model.contas.ContaCorrente;
import app.services.conta.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PutMapping("/sacar")
    public ResponseEntity sacarSaldo(@RequestBody ContaCorrente conta, @RequestParam double valor){
        try{
            contaCorrenteService.sacarValor(conta, BigDecimal.valueOf(valor), TarifaEnum.ISENTA);
            return ResponseEntity.ok("Saque feita com exito");
        }
        catch(SaldoInsuficienteException e){
            System.out.println("Saldo insuficiente para saque");
            Map<String, String> erro = Map.of("erro", "Saldo insuficiente para saque");
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
        }
    }

    @PutMapping("/depositar")
    public void depositarSaldo(@RequestBody ContaCorrente conta, @RequestParam double valor){
        contaCorrenteService.depositarValor(conta, BigDecimal.valueOf(valor));
    }

    @PostMapping("/transacao")
    public ResponseEntity transacaoSaldo(@RequestBody ArrayList<ContaCorrente> contas, @RequestParam double valor) throws SaldoInsuficienteException{
              
        try{
            contaCorrenteService.transacao(contas, BigDecimal.valueOf(valor));
            return ResponseEntity.ok("Transação feita com exito");
        }
        catch(SaldoInsuficienteException e){
            System.out.println("Saldo insuficiente para transação");
            Map<String, String> erro = Map.of("erro", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
        }
    }

    @DeleteMapping
    public void deleteConta(@RequestBody ContaCorrente conta){
        contaCorrenteService.deletarConta(conta);
    }
}
