package app.services.conta;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import app.model.contas.ContaCorrente;

/**
 *
 * @author joaoapassos
 */

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
}