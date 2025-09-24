package app.enums;

import java.math.BigDecimal;
import java.sql.SQLException;

import app.exception.ContaInexistenteException;
import app.model.Movimentacao;
import app.resources.dao.ContaCorrenteDAO;

public enum RegistroOperacoesEnum {
    SAQUE {
        public void registrar(int numero, BigDecimal valor) throws SQLException{
            ContaCorrenteDAO dao = new ContaCorrenteDAO();
            
            dao.movimentacoes(new Movimentacao(0, numero, "SAQUE", valor, ""));
        }
    },
    DEPOSITO {
        public void registrar(int numero, BigDecimal valor) throws SQLException{
            ContaCorrenteDAO dao = new ContaCorrenteDAO();

            dao.movimentacoes(new Movimentacao(0, numero, "DEPOSITO", valor, ""));
        }
    },
    TRANSACAO {
        public void registrar(int numero, BigDecimal valor) throws SQLException{
            ContaCorrenteDAO dao = new ContaCorrenteDAO();

            dao.movimentacoes(new Movimentacao(0, numero, "TRANSACAO", valor, ""));
        }
    };

    public abstract void registrar(int numero, BigDecimal valor) throws SQLException;
}
