package app.exception;

/**
 *
 * @author joaoapassos
 */


public class OrdenacaoNaoExistenteException extends Exception{
    public OrdenacaoNaoExistenteException(String mensagem){
        super(mensagem);
    }
}
