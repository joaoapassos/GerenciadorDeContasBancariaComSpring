package services.agruparpor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.AgruparPorInterface;
import model.contas.Conta;

public class AgruparContasPorFaixaDeSaldo implements AgruparPorInterface{
    
    private BigDecimal min;
    private BigDecimal max;

    public AgruparContasPorFaixaDeSaldo(BigDecimal min, BigDecimal max){
        this.min = min;
        this.max = max;
    }   

    public String aplicar(List<Conta> contas){
        List<Conta> filtradas = contas.stream()
            .filter(conta -> {
                double saldo = conta.getSaldo().doubleValue();
                return saldo >= min.doubleValue() && saldo <= max.doubleValue();
            })
            .collect(Collectors.toList());

        if (filtradas.isEmpty()) {
            return String.format("\nNenhuma conta encontrada na faixa de R$%.2f a R$%.2f.\n", min, max);
        }

        StringBuilder string = new StringBuilder();
        string.append(String.format("\nContas na faixa de R$%.2f a R$%.2f:\n", min, max));

        filtradas.forEach(conta -> string.append(" - Conta: ").append(conta.getNumero())
                                        .append(", Titular: ").append(conta.getTitular())
                                        .append(", Email: ").append(conta.getEmail())
                                        .append(", Saldo: R$ ").append(String.format("%.2f", conta.getSaldo()))
                                        .append("\n"));

        return string.toString();
    }
}
