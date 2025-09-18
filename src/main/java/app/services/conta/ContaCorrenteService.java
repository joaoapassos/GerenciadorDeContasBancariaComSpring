package app.services.conta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import app.enums.TarifaEnum;
import app.exception.*;
import app.interfaces.AgruparPorInterface;
import app.interfaces.FiltroInterface;
import app.interfaces.OrdenacaoInterface;
import app.model.contas.Conta;
import app.model.contas.ContaCorrente;

public abstract class ContaCorrenteService {
    public abstract List<ContaCorrente> carregarContas();

    public ContaCorrente loginConta(List<ContaCorrente> contas, String email, String senha) throws ContaInexistenteException{
        for(ContaCorrente conta : contas){
            if(conta.getEmail().equals(email.trim()) && conta.getSenha().equals(senha.trim())) return conta;
        }

        throw new ContaInexistenteException("Email ou senha inválido");
    }

    public abstract ContaCorrente buscarContaPorId(int id);
    
    public abstract void cadastrarConta(ContaCorrente novaConta);

    public abstract void atualizarConta(ContaCorrente conta);

    public abstract void deletarConta(ContaCorrente conta);

    public List<ContaCorrente> filtrar(FiltroInterface tipoFiltro, List<ContaCorrente> contas){
        
        List<Conta> contasFiltro = new ArrayList<>(contas);
        contasFiltro = tipoFiltro.aplicar(contasFiltro);

        return contasFiltro.stream()
                .filter(conta -> conta instanceof ContaCorrente)
                .map(conta -> (ContaCorrente) conta)
                .collect(Collectors.toList());
    }

    public List<ContaCorrente> ordenar(OrdenacaoInterface tipoOrdenacao, List<ContaCorrente> contas){

        List<Conta> contasOrdenacao = new ArrayList<>(contas);
        contasOrdenacao = tipoOrdenacao.aplicar(contasOrdenacao);

        return contasOrdenacao.stream()
                .filter(conta -> conta instanceof ContaCorrente)
                .map(conta -> (ContaCorrente) conta)
                .collect(Collectors.toList());
    }

    public void imprimirContas(List<ContaCorrente> contas){
        System.out.println(gerarStringFormat(contas));
    }

    private String gerarStringFormat(List<ContaCorrente> contas){
        StringBuilder string = new StringBuilder();
        string.append(String.format("\nImprimindo Contas Na Tela ------------"));

        contas.forEach(conta -> string.append(" - Conta: ").append(conta.getNumero())
                                        .append(", Titular: ").append(conta.getTitular())
                                        .append(", Email: ").append(conta.getEmail())
                                        .append(", Saldo: R$ ").append(String.format("%.2f", conta.getSaldo()))
                                        .append("\n"));

        return string.toString();
    }

    public Map<Integer, List<ContaCorrente>> agrupar(AgruparPorInterface agruparTipo, List<ContaCorrente> contas){

        List<Conta> contasAgrupar = new ArrayList<>(contas);
        Map<Integer, List<Conta>> agrupamento = agruparTipo.aplicar(contasAgrupar);

        return agrupamento.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                .filter(ContaCorrente.class::isInstance)
                .map(ContaCorrente.class::cast)
                .collect(Collectors.toList())
        ));
    }

    public void agruparEImprimir(AgruparPorInterface agruparTipo, List<ContaCorrente> contas){
        List<Conta> contasAgrupar = new ArrayList<>(contas);
        Map<Integer, List<Conta>> agrupar = agruparTipo.aplicar(contasAgrupar);

        Map<Integer, List<ContaCorrente>> agrupamento = agrupar.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .filter(ContaCorrente.class::isInstance)
                    .map(ContaCorrente.class::cast)
                    .collect(Collectors.toList())
            ));

        for (Integer chave : agrupamento.keySet()) {
            List<ContaCorrente> grupo = agrupamento.getOrDefault(chave, Collections.emptyList());
            imprimirContas(grupo);
        }
        
    }

    public void sacarValor(ContaCorrente conta, BigDecimal valor, TarifaEnum tarifa) throws SaldoInsuficienteException, IOException{
        conta.sacar(tarifa.aplicar(valor));

        atualizarSaldo(conta);
    }

    public void depositarValor(ContaCorrente conta, BigDecimal valor){
        conta.depositar(valor);

        atualizarSaldo(conta);
    }

    public abstract void atualizarSaldo(ContaCorrente conta);
}
