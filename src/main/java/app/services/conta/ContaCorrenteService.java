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
import app.exception.SaldoInsuficienteException;
import app.model.contas.ContaCorrente;
import app.exception.*;
import app.resources.dao.ContaCorrenteDAO;

import org.springframework.stereotype.Service;

//Classe atual responsavel pelo serviços de ContaCorrente

@Service
public class ContaCorrenteService {
    public List<ContaCorrente> carregarContas(){
        ContaCorrenteDAO contas = new ContaCorrenteDAO();
        return contas.listar();
    }

    public ContaCorrente loginConta(ContaCorrente conta){
        String email = conta.getEmail();
        String senha = conta.getSenha();

        ContaCorrenteDAO dao = new ContaCorrenteDAO();
        
        return dao.loginConta(email, senha);
        
    }

    public ContaCorrente buscarContaPorId(int id){
        ContaCorrenteDAO conta = new ContaCorrenteDAO();
        return conta.selectById(id);
    }
    
    public void cadastrarConta(ContaCorrente novaConta){
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.inserir(novaConta);
    }

    public void atualizarConta(ContaCorrente conta){    
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.atualizarConta(conta);
    }

    public void deletarConta(ContaCorrente conta){
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.remover(conta.getNumero());
    }

    public void atualizarSaldo(ContaCorrente conta){
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.atualizaSaldo(conta);
    }
   
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
        System.out.println(gerarStringParaImpressao(contas));
    }

    private String gerarStringParaImpressao(List<ContaCorrente> contas){
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
        Map<Integer, List<ContaCorrente>> agrupamento = agrupar(agruparTipo, contas);

        for (Integer chave : agrupamento.keySet()) {
            List<ContaCorrente> grupo = agrupamento.getOrDefault(chave, Collections.emptyList());
            imprimirContas(grupo);
        }
        
    }

    public void sacarValor(ContaCorrente conta, BigDecimal valor, TarifaEnum tarifa) throws SaldoInsuficienteException{
        conta.sacar(tarifa.aplicar(valor));

        atualizarSaldo(conta);
    }

    public void depositarValor(ContaCorrente conta, BigDecimal valor){
        conta.depositar(valor);

        atualizarSaldo(conta);
    }

    public void transacao(int numeroOrigem, int numeroDestino, BigDecimal valor) throws SaldoInsuficienteException{
        ContaCorrente contaOrigem = buscarContaPorId(numeroOrigem);
        ContaCorrente contaDestino = buscarContaPorId(numeroDestino);
        
        sacarValor(contaOrigem, valor, TarifaEnum.ISENTA);
        depositarValor(contaDestino, valor);
    }

    public BigDecimal saldoTotalDasContas(List<ContaCorrente> contas){
        Optional<BigDecimal> total =  contas.stream().map(ContaCorrente::getSaldo).reduce(BigDecimal::add);
        return total.orElse(BigDecimal.ZERO);
    }
}
