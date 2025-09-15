package services.filtro;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import interfaces.FiltroInterface;
import model.contas.Conta;

/**
 *
 * @author joaoapassos
 */

public class FiltroByNumberPar implements FiltroInterface{
    @Override
    public List<Conta> aplicar(List<Conta> contas){
        Predicate<Conta> filtro = f -> (f.getNumero()%2) == 0;
        
        return contas.stream().filter(filtro).collect(Collectors.toList());
    }
}