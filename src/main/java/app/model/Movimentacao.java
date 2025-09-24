package app.model;

import java.math.BigDecimal;

public class Movimentacao {
    private int id;
    private int numero;
    private String tipo;
    private BigDecimal valor;
    private String data;

    public Movimentacao(int id, int numero, String tipo, BigDecimal valor, String data){
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    
    public BigDecimal getValor() {
        return valor;
    }
    
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return id + " | " + numero + " | " + tipo + " | " + "R$ " + valor + " | " + data;
    }
}
