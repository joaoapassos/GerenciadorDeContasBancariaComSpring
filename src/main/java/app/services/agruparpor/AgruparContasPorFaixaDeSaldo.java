package app.services.agruparpor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.interfaces.AgruparPorInterface;
import app.model.contas.Conta;

public class AgruparContasPorFaixaDeSaldo implements AgruparPorInterface{
    
    private BigDecimal min;
    private BigDecimal max;

    public AgruparContasPorFaixaDeSaldo(BigDecimal min, BigDecimal max){
        this.min = min;
        this.max = max;
    }   

    public Map<Integer, List<Conta>> aplicar(List<Conta> contas){
        List<Conta> filtradas = contas.stream()
            .filter(conta -> {
                BigDecimal saldo = conta.getSaldo();
                return saldo.compareTo(min) >= 0 && saldo.compareTo(max) <= 0;
            })
            .collect(Collectors.toList());

        if (filtradas.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, List<Conta>> resultado = new HashMap<>();
        resultado.put(0, filtradas);
        
        return resultado;
    }
}
