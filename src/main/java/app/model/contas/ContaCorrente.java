package app.model.contas;

import java.math.BigDecimal;

import app.exception.SaldoInsuficienteException;

/**
 *
 * @author joaoapassos
 */

public class ContaCorrente extends Conta{
    
    public ContaCorrente(){
        
    }
    
    public ContaCorrente(int numero, String titular, String email, String senha){
        super(numero, titular, email, senha, null);
    }
    public ContaCorrente(int numero, String titular, String email, String senha, BigDecimal saldo){
        super(numero, titular, email, senha, saldo);
    }

    @Override
    public void sacar(BigDecimal valor) throws SaldoInsuficienteException{
        if(saldo.doubleValue() < valor.doubleValue()) throw new SaldoInsuficienteException("Saldo insuficiente, não é possivel sacar!");
        else saldo.add(valor);
    }

    public void imprimirDados(){
        System.out.println(toString());
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
