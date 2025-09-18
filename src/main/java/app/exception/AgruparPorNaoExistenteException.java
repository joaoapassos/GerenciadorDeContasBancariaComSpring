package app.exception;

/**
 *
 * @author joaoapassos
 */


public class AgruparPorNaoExistenteException extends Exception{
    public AgruparPorNaoExistenteException(String mensagem){
        super(mensagem);
    }
}
