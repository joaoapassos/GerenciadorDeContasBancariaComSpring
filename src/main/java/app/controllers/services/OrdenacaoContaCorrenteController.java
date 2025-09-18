package app.controllers.contas;

import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.filtro.*;
import app.exception.*;
import app.interfaces.*;
import app.services.ordenacao.*;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/order")
public class OrdenacaoContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public OrdenacaoContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }


    @GetMapping("/{typeOrdenacao}")
    public List<ContaCorrente> ordenar(@PathVariable String typeOrdenacao) throws OrdenacaoNaoExistenteException{
        OrdenacaoInterface ordenacao;

        if("OrdenacaoByName".equals(typeOrdenacao)) ordenacao = new OrdenacaoByName();
        else if("OrdenacaoDecrecente".equals(typeOrdenacao)) ordenacao = new OrdenacaoDecrecente();
        else throw new OrdenacaoNaoExistenteException("Ordenação escolhida não existe");

        
        List<ContaCorrente> contas = contaCorrenteService.carregarContas();
        return contaCorrenteService.ordenar(ordenacao, contas);  
    }
}
