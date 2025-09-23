package app.controllers.contacorrente;

import app.enums.TarifaEnum;
import app.exception.ContaInexistenteException; // Supondo que você tenha esta exceção
import app.exception.SaldoInsuficienteException;
import app.model.contas.ContaCorrente;
import app.services.conta.ContaCorrenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    public List<ContaCorrente> getManyContas() throws SQLException{
        return contaCorrenteService.carregarContas();
    }

    @GetMapping("/{id}")
    public ContaCorrente getContaById(@PathVariable int id) throws ContaInexistenteException, SQLException {
        return contaCorrenteService.buscarContaPorId(id);
    }

    @GetMapping("/saldototal")
    public BigDecimal saldoTotalDasContas() {
        List<ContaCorrente> contas = contaCorrenteService.carregarContas();
        return contaCorrenteService.saldoTotalDasContas(contas);
    }
    
    @PostMapping("/acessar")
    public ContaCorrente loginConta(@RequestBody Map<String, Object> acesso) throws ContaInexistenteException, SQLException {
        String email = "";
        String senha = "";
        if(acesso.containsKey("email")) email = (String) acesso.get("email");
        if(acesso.containsKey("senha")) senha = (String) acesso.get("senha");
        
        return contaCorrenteService.loginConta(email, senha);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> postConta(@RequestBody ContaCorrente newConta) throws SQLException{
        contaCorrenteService.cadastrarConta(newConta);
        return ResponseEntity.ok("Conta cadastrada com sucesso.");
    }

    @PutMapping
    public ResponseEntity<String> putConta(@RequestBody ContaCorrente conta) throws SQLException{
        contaCorrenteService.atualizarConta(conta);
        return ResponseEntity.ok("Conta atualizada com sucesso.");
    }

    @PostMapping("/sacar/{id}")
    public ResponseEntity<String> sacarSaldo(@PathVariable int id, @RequestParam BigDecimal valor) throws SaldoInsuficienteException, ContaInexistenteException, SQLException {
        contaCorrenteService.sacarValor(id, valor, TarifaEnum.ISENTA);
        return ResponseEntity.ok("Saque feito com êxito");
    }

    @PostMapping("/depositar/{id}")
    public ResponseEntity<String> depositarSaldo(@PathVariable int id, @RequestParam BigDecimal valor) throws ContaInexistenteException, SQLException {
        contaCorrenteService.depositarValor(id, valor);
        return ResponseEntity.ok("Depósito feito com êxito.");
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestBody Map<String, Object> transacao) throws SaldoInsuficienteException, ContaInexistenteException, SQLException {
        if (!transacao.containsKey("contaOrigem") || !transacao.containsKey("contaDestino") || !transacao.containsKey("valor")) {
            // Lançamos um erro de argumento inválido que o handler genérico pode pegar.
            throw new IllegalArgumentException("Dados da transação incompletos. 'contaOrigem', 'contaDestino' e 'valor' são obrigatórios.");
        }

        int origem = Integer.parseInt(transacao.get("contaOrigem").toString());
        int destino = Integer.parseInt(transacao.get("contaDestino").toString());

        Number valorRecebido = (Number) transacao.get("valor");
        BigDecimal valor = BigDecimal.valueOf(valorRecebido.doubleValue());

        contaCorrenteService.transferir(origem, destino, valor);

        return ResponseEntity.ok("Transação feita com êxito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable int id) throws ContaInexistenteException {
        contaCorrenteService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }
}