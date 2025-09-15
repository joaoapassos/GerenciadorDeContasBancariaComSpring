package services.conta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import enums.TarifaEnum;
import exception.ContaInexistenteException;
import exception.SaldoInsuficienteException;
import interfaces.AgruparPorInterface;
import interfaces.FiltroInterface;
import interfaces.OrdenacaoInterface;
import model.contas.Conta;
import model.contas.ContaCorrente;

public abstract class ContaCorrenteService {
    public abstract List<ContaCorrente> carregarContas();

    public ContaCorrente loginConta(List<ContaCorrente> contas, String email, String senha) throws ContaInexistenteException{
        for(ContaCorrente conta : contas){
            if(conta.getEmail().equals(email.trim()) && conta.getSenha().equals(senha.trim())) return conta;
        }

        throw new ContaInexistenteException("Email ou senha inválido");
    }
    
    public abstract void cadastrarConta(int numero, String titular, String email, String senha) throws IOException;

    public abstract void atualizarContas(ContaCorrente conta) throws IOException;

    public abstract void deletarConta(ContaCorrente conta) throws IOException;

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

    public String agrupar(AgruparPorInterface agruparTipo, List<ContaCorrente> contas){
        List<Conta> contasAgrupar = new ArrayList<>(contas);
        return agruparTipo.aplicar(contasAgrupar);
    }

    public void agruparEImprimir(AgruparPorInterface agruparTipo, List<ContaCorrente> contas){
        List<Conta> contasAgrupar = new ArrayList<>(contas);
        System.out.println(agruparTipo.aplicar(contasAgrupar));
    }

    public void sacarValor(ContaCorrente conta, BigDecimal valor, TarifaEnum tarifa) throws SaldoInsuficienteException, IOException{
        conta.sacar(tarifa.aplicar(valor));

        atualizarSaldo(conta);
    }

    public void depositarValor(ContaCorrente conta, BigDecimal valor) throws IOException{
        conta.depositar(valor);

        atualizarSaldo(conta);
    }

    public abstract void atualizarSaldo(ContaCorrente conta) throws IOException;
}
