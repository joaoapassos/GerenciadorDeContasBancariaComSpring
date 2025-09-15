package interfaces;

import java.util.List;

import model.contas.Conta;

public interface FiltroInterface {
    public List<Conta> aplicar(List<Conta> contas);
}
