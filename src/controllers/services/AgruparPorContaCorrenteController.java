package controllers.contas;

import model.contas.ContaCorrente;
import services.conta.*;
import services.filtro.*;
import exception.AgruparPorNaoExistenteException;
import interfaces.AgruparPorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services/groupby")
public class AgruparPorContaCorrenteController {
    @Autowired
    private final ContaCorrenteService contaCorrenteService = new ContaCorrenteServiceSql();


    @GetMapping("/{typeGroupBy}")
    public ResponseEntity<ContaCorrente> ordenar(@PathVariable String typeGroupBy){
        AgruparPorInterface agruparPor;

        if(typeGroupBy == "AgruparContasPorFaixaDeSaldo") agruparPor = new AgruparContasPorFaixaDeSaldo();
        else if(typeGroupBy == "AgruparContasPorSaldo") agruparPor = new AgruparContasPorSaldo();
        else agruparPor = null;

        try{
            List<ContaCorrente> contas = contaCorrenteService.carregarContas();
            return contaCorrenteService.ordenar(agruparPor, contas)
                    .map(ResponseEntity::ok) // Se achar, retorna o usuário com status 200 OK
                    .orElse(ResponseEntity.notFound().build());
        }
        catch(AgruparPorNaoExistenteException e){
            System.out.println("Erro ao agrupar! Detalhes do erro: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
