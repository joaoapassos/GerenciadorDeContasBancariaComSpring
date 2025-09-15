package exception;

/**
 *
 * @author joaoapassos
 */

public class ContaInexistenteException extends Exception{
    public ContaInexistenteException(String mensagem){
        super(mensagem);
    }
}
