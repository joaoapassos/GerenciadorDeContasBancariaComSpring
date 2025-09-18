package app.controllers.contas;

import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.filtro.*;
import app.exception.*;
import app.interfaces.*;
import app.services.agruparpor.*;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/groupby")
public class AgruparPorContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public AgruparPorContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }

    @GetMapping("/{typeGroupBy}")
    public Map<Integer, List<ContaCorrente>> agrupar(
        @PathVariable String typeGroupBy, 
        @RequestParam(required = false) BigDecimal min, 
        @RequestParam(required = false) BigDecimal max) throws AgruparPorRequerParametroException, AgruparPorNaoExistenteException{
        AgruparPorInterface agruparPor;

        if("AgruparContasPorFaixaDeSaldo".equals(typeGroupBy)) {
            if(min == null || max == null) throw new AgruparPorRequerParametroException("Saldo min e saldo max nao informados");
            else agruparPor = new AgruparContasPorFaixaDeSaldo(min, max);
        }
        else if("AgruparContasPorSaldo".equals(typeGroupBy)) agruparPor = new AgruparContasPorSaldo();
        else throw new AgruparPorNaoExistenteException("Agrupamento escolhido não existe");

        List<ContaCorrente> contas = contaCorrenteService.carregarContas();
        return contaCorrenteService.agrupar(agruparPor, contas);
    }
}
