package app.interfaces;

import java.util.List;

import app.model.contas.Conta;

public interface FiltroInterface {
    public List<Conta> aplicar(List<Conta> contas);
}
