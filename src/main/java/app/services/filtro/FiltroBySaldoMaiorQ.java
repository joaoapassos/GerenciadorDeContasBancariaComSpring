package app.services.filtro;

import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Predicate;

import app.interfaces.FiltroInterface;
import app.model.contas.Conta;

/**
 *
 * @author joaoapassos
 */

public class FiltroBySaldoMaiorQ implements FiltroInterface{
    private double value;

    public FiltroBySaldoMaiorQ(Number valor){
        this.value = valor.doubleValue();
    }

    @Override
    public List<Conta> aplicar(List<Conta> contas){
        
        Predicate<Conta> filtro = f -> f.getSaldo().doubleValue() >= this.value;
        
        return contas.stream().filter(filtro).collect(Collectors.toList());
    }
}
