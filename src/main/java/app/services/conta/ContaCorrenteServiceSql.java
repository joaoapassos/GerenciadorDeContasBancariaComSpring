package app.services.conta;

import java.io.IOException;
import java.util.*;

import app.model.contas.ContaCorrente;
import app.exception.*;
import app.resources.dao.ContaCorrenteDAO;

import org.springframework.stereotype.Service; 

/**
 *
 * @author joaoapassos
 */

@Service
public class ContaCorrenteServiceSql extends ContaCorrenteService{
    
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

}
