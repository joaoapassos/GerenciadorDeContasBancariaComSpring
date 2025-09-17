package controllers.contas;

import model.contas.ContaCorrente;
import services.conta.*;
import services.filtro.*;
import exception.OrdenacaoNaoExistenteException;
import interfaces.OrdenacaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/order")
public class OrdenacaoContaCorrenteController {
    @Autowired
    private final ContaCorrenteService contaCorrenteService = new ContaCorrenteServiceSql();


    @GetMapping("/{typeOrdenacao}")
    public ResponseEntity<ContaCorrente> ordenar(@PathVariable String typeOrdenacao){
        OrdenacaoInterface ordenacao;

        if(typeOrdenacao == "OrdenacaoByName") ordenacao = new OrdenacaoByName();
        else if(typeOrdenacao == "OrdenacaoDecrecente") ordenacao = new OrdenacaoDecrecente();
        else ordenacao = null;

        try{
            List<ContaCorrente> contas = contaCorrenteService.carregarContas();
            return contaCorrenteService.ordenar(ordenacao, contas)
                    .map(ResponseEntity::ok) // Se achar, retorna o usuário com status 200 OK
                    .orElse(ResponseEntity.notFound().build());
        }
        catch(OrdenacaoNaoExistenteException e){
            System.out.println("Erro ao ordenar! Detalhes do erro: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
