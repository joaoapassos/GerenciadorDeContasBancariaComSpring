package app.exception;

/**
 *
 * @author joaoapassos
 */

public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensagem){
        super(mensagem);
    }
}
