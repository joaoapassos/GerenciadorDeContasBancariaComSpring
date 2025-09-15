package services.agruparpor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.AgruparPorInterface;
import model.contas.Conta;
import model.contas.ContaCorrente;

public class AgruparContasPorSaldo implements AgruparPorInterface {

    public String aplicar(List<Conta> contas){
        Map<String, List<Conta>> agrupados = contas.stream()
            .collect(Collectors.groupingBy(conta -> {
                double saldo = conta.getSaldo().doubleValue();
                if (saldo <= 5000) return "Até R$ 5.000";
                else if (saldo <= 10000) return "De R$ 5.001 a R$ 10.000";
                else return "Acima de R$ 10.000";
            }));

        StringBuilder string = new StringBuilder();

        agrupados.forEach((faixa, lista) -> {
            string.append(faixa).append(":\n");
            lista.forEach(conta -> string.append(" - Conta: ").append(conta.getNumero())
                                        .append(", Titular: ").append(conta.getTitular())
                                        .append(", Email: ").append(conta.getEmail())
                                        .append(", Saldo: R$ ").append(conta.getSaldo()).append("\n"));
        });

        return string.toString();
    }
}