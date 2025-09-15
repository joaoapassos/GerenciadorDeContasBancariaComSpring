package services.conta;

import java.io.IOException;
import java.util.List;

import model.contas.ContaCorrente;
import resources.dao.ContaCorrenteDAO;

/**
 *
 * @author joaoapassos
 */

public class ContaCorrenteServiceSql extends ContaCorrenteService{
    
    public List<ContaCorrente> carregarContas(){
        ContaCorrenteDAO contas = new ContaCorrenteDAO();
        return contas.listar();
    }
    
    public void cadastrarConta(int numero, String titular, String email, String senha) throws IOException{
        ContaCorrente conta = new ContaCorrente(numero, titular, email, senha);

        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.inserir(conta);
    }

    public void atualizarContas(ContaCorrente conta){    
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.atualizarConta(conta);
    }

    public void deletarConta(ContaCorrente conta){
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.remover(conta.getNumero());
    }

    public void atualizarSaldo(ContaCorrente conta) throws IOException{
        ContaCorrenteDAO dao = new ContaCorrenteDAO();

        dao.atualizaSaldo(conta);
    }

}
