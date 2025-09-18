package app.model.contas;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import app.exception.SaldoInsuficienteException;

/**
 *
 * @author joaoapassos
 */

public abstract class Conta {
    protected int numero;
    protected String titular;
    protected String email;
    protected String senha;
    protected BigDecimal saldo;

    public Conta(){
    }

    Conta(int numero, String titular, String email, String senha, BigDecimal saldo){
        this.numero = numero;
        this.titular = titular;
        this.email = email;
        this.senha = senha;
        this.saldo = saldo;
    }

    abstract void sacar(BigDecimal valor) throws SaldoInsuficienteException;

    public void depositar(BigDecimal valor){
        this.saldo.add(valor);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
    public int getNumero() {
        return numero;
    }
    public String getTitular() {
        return titular;
    }

    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }

    public abstract void imprimirDados();

    @Override
    public String toString() {
        return numero + "," + titular + "," + email + "," + senha + "," + saldo;
    }
}
