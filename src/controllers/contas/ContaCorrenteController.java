package controllers.contas;

import model.contas.ContaCorrente;
import services.conta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas/corrente")
public class ContaCorrenteController {
    @Autowired
    private final ContaCorrenteService contaCorrenteService = new ContaCorrenteServiceSql();

    @GetMapping
    public List<ContaCorrente> getManyContas(){
        return contaCorrenteService.carregarContas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaCorrente> getContaById(@PathVariable int id){
        return contaCorrenteService.buscarContaPorId()
                .map(ResponseEntity::ok) // Se achar, retorna o usuário com status 200 OK
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ContaCorrente postConta(@RequestBody ContaCorrente newConta){
        return ContaCorrenteService.cadastrarConta(newConta);
    }

    @PutMapping
    public ContaCorrente putConta(@RequestBody ContaCorrente conta){
        return ContaCorrenteService.atualizarConta(conta);
    }

    @DeleteMapping
    public ContaCorrente deleteConta(@RequestBody ContaCorrente conta){
        return ContaCorrenteService.deletarConta(conta);
    }
}
