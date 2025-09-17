package controllers.contas;

import model.contas.ContaCorrente;
import services.conta.*;
import services.filtro.*;
import exception.FiltroNaoExistenteException;
import interfaces.FiltroInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/filter")
public class FiltroContaCorrenteController {
    @Autowired
    private final ContaCorrenteService contaCorrenteService = new ContaCorrenteServiceSql();


    @GetMapping("/{typeFilter}")
    public ResponseEntity<ContaCorrente> filtrar(@PathVariable String typeFilter){
        FiltroInterface filtro;

        if(typeFilter == "FiltroByNumberPar") filtro = new FiltroByNumberPar();
        else if(typeFilter == "FiltroBySaldoMaiorQ") filtro = new FiltroBySaldoMaiorQ();
        else filtro = null;

        try{
            List<ContaCorrente> contas = contaCorrenteService.carregarContas();
            return contaCorrenteService.filtrar(filtro, contas)
                    .map(ResponseEntity::ok) // Se achar, retorna o usuário com status 200 OK
                    .orElse(ResponseEntity.notFound().build());
        }
        catch(FiltroNaoExistenteException e){
            System.out.println("Erro ao filtrar! Detalhes do erro: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
