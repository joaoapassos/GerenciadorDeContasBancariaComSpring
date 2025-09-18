package app.interfaces;

import java.util.List;
import java.util.Map;

import app.model.contas.Conta;

public interface AgruparPorInterface {
    public Map<Integer, List<Conta>> aplicar(List<Conta> contas);
}
