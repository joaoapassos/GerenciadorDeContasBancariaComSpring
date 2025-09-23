package app.controllers.contacorrente;

import app.model.contas.ContaCorrente;
import app.services.conta.*;
import app.services.filtro.*;
import app.exception.AgruparPorNaoExistenteException;
import app.exception.AgruparPorRequerParametroException;
import app.interfaces.AgruparPorInterface;
import app.services.agruparpor.AgruparContasPorFaixaDeSaldo;
import app.services.agruparpor.AgruparContasPorSaldo;
import app.services.conta.ContaCorrenteService;
import app.exception.*;
import app.interfaces.*;
import app.services.agruparpor.*;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Classe resposavel por gerenciar as rotas que o frontend utiliza para agrupar as contas usando o metodo agrupar de ContaCorrenteService

@RestController
@RequestMapping("/api/services/corrente/groupby")
public class AgruparPorContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;
    @Autowired
    public AgruparPorContaCorrenteController(ContaCorrenteService contaCorrenteService){
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping("/{typeGroupBy}")
    public Map<Integer, List<ContaCorrente>> agrupar(
        @PathVariable String typeGroupBy,
        @RequestParam(required = false) BigDecimal min, 
        @RequestParam(required = false) BigDecimal max,
        @RequestBody List<ContaCorrente> contas) throws AgruparPorRequerParametroException, AgruparPorNaoExistenteException{
        AgruparPorInterface agruparPor;

        if("AgruparContasPorFaixaDeSaldo".equals(typeGroupBy)) {
            if(min == null || max == null) throw new AgruparPorRequerParametroException("Saldo min e saldo max nao informados");
            else if(min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)  throw new AgruparPorRequerParametroException("Saldo min e saldo max são inválidos, informe valores positivos");
            else agruparPor = new AgruparContasPorFaixaDeSaldo(min, max);
        }
        else if("AgruparContasPorSaldo".equals(typeGroupBy)) agruparPor = new AgruparContasPorSaldo();
        else throw new AgruparPorNaoExistenteException("Agrupamento escolhido não existe");

        return contaCorrenteService.agrupar(agruparPor, contas);
    }
}
