package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

record ApiErrorResponse(int status, String message, long timestamp) {}

@RestControllerAdvice
public class RestExceptionHandler{

    // Para exceções de Filtro
    @ExceptionHandler({FiltroNaoExistenteException.class, FiltroRequerParametroException.class})
    public ResponseEntity<ApiErrorResponse> handleFiltroException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(), // 400
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Para exceções de Agrupamento
    @ExceptionHandler({AgruparPorNaoExistenteException.class, AgruparPorRequerParametroException.class})
    public ResponseEntity<ApiErrorResponse> handleAgruparPorException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(), // 400
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Para exceção de Ordenação
    @ExceptionHandler(OrdenacaoNaoExistenteException.class)
    public ResponseEntity<ApiErrorResponse> handleOrdenacaoException(OrdenacaoNaoExistenteException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(), // 400
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Para exceção de Saldo Insuficiente
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ApiErrorResponse> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(), // 400
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Para exceção de Conta Inexistente
    @ExceptionHandler(ContaInexistenteException.class)
    public ResponseEntity<ApiErrorResponse> handleContaInexistenteException(ContaInexistenteException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(), // 404 - Not Found
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
            "Ocorreu um erro inesperado no servidor.",
            System.currentTimeMillis()
        );
        ex.printStackTrace(); // Importante para logar o erro real no console
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}