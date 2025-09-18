package app.exception;

/**
 *
 * @author joaoapassos
 */


public class FiltroNaoExistenteException extends Exception{
    public FiltroNaoExistenteException(String mensagem){
        super(mensagem);
    }
}
