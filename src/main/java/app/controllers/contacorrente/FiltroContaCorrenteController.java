package app.controllers.contacorrente;


import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.filtro.*;
import app.exception.FiltroNaoExistenteException;
import app.exception.FiltroRequerParametroException;
import app.interfaces.FiltroInterface;
import app.services.conta.ContaCorrenteService;
import app.services.filtro.FiltroByNumberPar;
import app.services.filtro.FiltroBySaldoMaiorQ;
import app.exception.*;
import app.interfaces.*;
import app.services.filtro.*;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Classe resposavel por gerenciar as rotas que o frontend utiliza para filtrar as contas usando o metodo filtrar de ContaCorrenteService

@RestController
@RequestMapping("/api/services/corrente/filter")
public class FiltroContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public FiltroContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping("/{typeFilter}")
    public List<ContaCorrente> filtrar(@PathVariable String typeFilter, @RequestBody List<ContaCorrente> contas, @RequestParam(required = false) BigDecimal saldoMin) throws FiltroNaoExistenteException, FiltroRequerParametroException{
        FiltroInterface filtro;

        if("FiltroByNumberPar".equals(typeFilter)) filtro = new FiltroByNumberPar();
        else if("FiltroBySaldoMaiorQ".equals(typeFilter)) {
            if(saldoMin == null) throw new FiltroRequerParametroException("Saldo não informado");
            else filtro = new FiltroBySaldoMaiorQ(saldoMin);
        }
        else throw new FiltroNaoExistenteException("Filtro escolhido não existe");

        return contaCorrenteService.filtrar(filtro, contas);
    }
}
