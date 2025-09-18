package app.controllers.contas;

import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.filtro.*;
import app.exception.*;
import app.interfaces.*;
import app.services.filtro.*;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/filter")
public class FiltroContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public FiltroContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }

    @GetMapping("/{typeFilter}")
    public List<ContaCorrente> filtrar(@PathVariable String typeFilter, @RequestParam(required = false) BigDecimal saldoMin) throws FiltroNaoExistenteException, FiltroRequerParametroException{
        FiltroInterface filtro;

        if("FiltroByNumberPar".equals(typeFilter)) filtro = new FiltroByNumberPar();
        else if("FiltroBySaldoMaiorQ".equals(typeFilter)) {
            if(saldoMin == null) throw new FiltroRequerParametroException("Saldo não informado");
            else filtro = new FiltroBySaldoMaiorQ(saldoMin);
        }
        else throw new FiltroNaoExistenteException("Filtro escolhido não existe");

        List<ContaCorrente> contas = contaCorrenteService.carregarContas();
        return contaCorrenteService.filtrar(filtro, contas);
    }
}
