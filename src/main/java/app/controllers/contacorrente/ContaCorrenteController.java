package app.controllers.contacorrente;

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

//Classe responsavel por gerenciar as rotas que o frontend se comunica com relação a conta corrente (ações como cadastro, listagem, entre outras)

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

    @GetMapping("/saldototal")
    public BigDecimal getMethodName() {
        List<ContaCorrente> contas = contaCorrenteService.carregarContas();
        return contaCorrenteService.saldoTotalDasContas(contas);
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
    public ResponseEntity transacaoSaldo(@RequestBody ArrayList<Integer> numeros, @RequestParam double valor) throws SaldoInsuficienteException{
        int numeroOrigem = numeros.get(0);
        int numeroDestino = numeros.get(1);
        try{
            contaCorrenteService.transacao(numeroOrigem, numeroDestino, BigDecimal.valueOf(valor));
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
