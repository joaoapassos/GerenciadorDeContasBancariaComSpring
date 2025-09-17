package services.conta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import enums.TarifaEnum;
import exception.*;
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

    public abstract ContaCorrente buscarContaPorId(int id);
    
    public abstract void cadastrarConta(ContaCorrente novaConta) throws IOException;

    public abstract void atualizarConta(ContaCorrente conta);

    public abstract void deletarConta(ContaCorrente conta) throws IOException;

    public List<ContaCorrente> filtrar(FiltroInterface tipoFiltro, List<ContaCorrente> contas) throws FiltroNaoExistenteException{
        if(tipoFiltro == null){
            throw FiltroNaoExistenteException("Filtro escolhido não existe");
        }
        
        List<Conta> contasFiltro = new ArrayList<>(contas);
        contasFiltro = tipoFiltro.aplicar(contasFiltro);

        return contasFiltro.stream()
                .filter(conta -> conta instanceof ContaCorrente)
                .map(conta -> (ContaCorrente) conta)
                .collect(Collectors.toList());
    }

    public List<ContaCorrente> ordenar(OrdenacaoInterface tipoOrdenacao, List<ContaCorrente> contas) throws OrdenacaoNaoExistenteException{
        if(tipoOrdenacao == null){
            throw OrdenacaoNaoExistenteException("Ordenação escolhida não existe");
        }

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

    public Map<Integer, List<ContaCorrente>> agrupar(AgruparPorInterface agruparTipo, List<ContaCorrente> contas) throws AgruparPorNaoExistenteException{
        if(agruparTipo == null) throw AgruparPorNaoExistenteException("Agrupamento escolhido não existe");

        List<Conta> contasAgrupar = new ArrayList<>(contas);
        return agruparTipo.aplicar(contasAgrupar);
    }

    public void agruparEImprimir(AgruparPorInterface agruparTipo, List<ContaCorrente> contas) throws AgruparPorNaoExistenteException{
        List<Conta> contasAgrupar = new ArrayList<>(contas);
        Map<Integer, List<ContaCorrente>> agrupamento = agruparTipo.aplicar(contasAgrupar);
        
        Map<Integer, String> legendas = Map.of(
            0, "Até R$ 5.000",
            1, "De R$ 5.001 a R$ 10.000",
            2, "Acima de R$ 10.000"
        );

        legendas.forEach((chave, descricao) -> {
            System.out.println(descricao + ":");
            List<Conta> grupo = agrupamento.getOrDefault(chave, Collections.emptyList());
            if (grupo.isEmpty()){
                System.out.println(" - Nenhuma conta nesta faixa.");
            } else {
                grupo.forEach(c -> System.out.println(" - " + c.toString()));
            }
        });
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
