package app.services.agruparpor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.interfaces.AgruparPorInterface;
import app.model.contas.Conta;
import app.model.contas.ContaCorrente;

public class AgruparContasPorSaldo implements AgruparPorInterface {

    public Map<Integer, List<Conta>> aplicar(List<Conta> contas){
        return contas.stream()
            .collect(Collectors.groupingBy(conta -> {
                double saldo = conta.getSaldo().doubleValue();
                if (saldo <= 5000) return 0; // Chave 0 para "Até R$ 5.000"
                else if (saldo <= 10000) return 1; // Chave 1 para "De R$ 5.001 a R$ 10.000"
                else return 2; // Chave 2 para "Acima de R$ 10.000"
            }));
    }
}