package app.services.ordenacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.interfaces.OrdenacaoInterface;
import app.model.contas.Conta;

/**
 *
 * @author joaoapassos
 */

public class OrdenacaoByName implements OrdenacaoInterface{
    @Override
    public List<Conta> aplicar(List<Conta> contas) {
        Comparator<Conta> ordenacao = (c1, c2) -> c1.getTitular().compareTo(c2.getTitular());
        List<Conta> ordenados = new ArrayList<>(contas);
        ordenados.sort(ordenacao);
        return ordenados;
    }
}