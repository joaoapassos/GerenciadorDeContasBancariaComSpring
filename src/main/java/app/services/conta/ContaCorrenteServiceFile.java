package app.services.conta;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import app.enums.TarifaEnum;
import app.exception.*;
import app.interfaces.AgruparPorInterface;
import app.interfaces.FiltroInterface;
import app.interfaces.OrdenacaoInterface;
import app.model.contas.Conta;
import app.model.contas.ContaCorrente;
import app.exception.SaldoInsuficienteException;

/**
 *
 * @author joaoapassos
 */

// Classe antiga responsavel pelo serviços de ContaCorrente com contas salva em um arquivo

public class ContaCorrenteServiceFile{
    private String contasCaminho;

    public List<ContaCorrente> carregarContas(String caminho){
        contasCaminho = caminho;
        try (Stream<String> linhas = Files.lines(Paths.get(caminho))){
            return linhas
                .map(linha -> linha.split(","))
                .filter(p -> p.length == 5)
                .map(p -> new ContaCorrente(
                    Integer.parseInt(p[0].trim()),
                    p[1].trim(),
                    p[2].trim(),
                    p[3].trim(),
                    BigDecimal.valueOf(Double.parseDouble(p[4].trim()))
                ))
                .collect(Collectors.toList());
        }
        catch(IOException e){
            System.err.println("\n\nErro ao ler o arquivo\n\nDetalhes do erro " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void cadastrarNovaConta(int numero, String titular, String email, String senha) throws IOException{
        ContaCorrente conta = new ContaCorrente(numero, titular, email, senha);
        List<ContaCorrente> contas = carregarContas(contasCaminho);
        contas.add(conta);

        StringBuilder dados = new StringBuilder();
        for (ContaCorrente c : contas) {
            dados.append(c.toString()).append("\n");
        }
        Files.write(Paths.get(contasCaminho), dados.toString().getBytes());
    }

    public void atualizarContas(ContaCorrente conta) throws IOException {    
        List<ContaCorrente> contas = carregarContas(contasCaminho);
        for (int i = 0; i < contas.size(); i++) {
            ContaCorrente atual = contas.get(i);
            if (atual.getEmail().equals(conta.getEmail()) && atual.getSenha().equals(conta.getSenha())) {
                contas.set(i, conta);
                break;
            }
        }
        StringBuilder dados = new StringBuilder();
        for (ContaCorrente c : contas) {
            dados.append(c.toString()).append("\n");
        }
        Files.write(Paths.get(contasCaminho), dados.toString().getBytes());
    }

    public void deletarConta(ContaCorrente conta) throws IOException{
        List<ContaCorrente> contas = carregarContas(contasCaminho);

        contas.remove(conta);

        StringBuilder dados = new StringBuilder();
        for (ContaCorrente c : contas) {
            dados.append(c.toString()).append("\n");
        }
        Files.write(Paths.get(contasCaminho), dados.toString().getBytes());
    }

    public void atualizarSaldo(ContaCorrente conta) throws IOException{
        atualizarContas(conta);
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

    public void sacarValor(ContaCorrente conta, BigDecimal valor, TarifaEnum tarifa) throws SaldoInsuficienteException, IOException{
        conta.sacar(tarifa.aplicar(valor));

        atualizarSaldo(conta);
    }

    public void depositarValor(ContaCorrente conta, BigDecimal valor) throws IOException{
        conta.depositar(valor);

        atualizarSaldo(conta);
    }

    public void transacao(ArrayList<ContaCorrente> contas, BigDecimal valor) throws SaldoInsuficienteException, IOException{
        ContaCorrente contaOrigem = contas.get(0);
        ContaCorrente contaDestino = contas.get(1);
        
        sacarValor(contaOrigem, valor, TarifaEnum.ISENTA);
        depositarValor(contaDestino, valor);
    }

    public BigDecimal saldoTotalDasContas(List<ContaCorrente> contas){
        Optional<BigDecimal> total =  contas.stream().map(ContaCorrente::getSaldo).reduce(BigDecimal::add);
        return total.orElse(BigDecimal.ZERO);
    }
    
}