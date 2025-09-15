package interfaces;

import java.util.List;

import model.contas.Conta;

/**
 *
 * @author joaoapassos
 */

public interface OrdenacaoInterface {
    public List<Conta> aplicar(List<Conta> contas);
}
