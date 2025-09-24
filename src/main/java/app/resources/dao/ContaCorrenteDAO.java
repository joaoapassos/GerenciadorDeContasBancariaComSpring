package app.resources.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.exception.ContaInexistenteException;
import app.exception.SaldoInsuficienteException;
import app.model.Movimentacao;
import app.model.contas.ContaCorrente;
import app.services.conta.ContaCorrenteService;

public class ContaCorrenteDAO {
    public void inserir(ContaCorrente conta){
        String sql = "INSERT INTO contas (titular, email, senha, saldo) VALUES  (?,?,?,?)";
        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getEmail());
            stmt.setString(3, conta.getSenha());
            stmt.setDouble(4, conta.getSaldo().doubleValue());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("\n\nErro ao inserir conta\n\nDetalhes do erro: " + e.getSQLState());
        }
    }

    public List<ContaCorrente> listar(){
        List<ContaCorrente> contas = new ArrayList<>();
        String sql = "SELECT * FROM contas";
        try(Connection conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
            {
                while(rs.next()){
                    ContaCorrente c = new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                    contas.add(c);
                }
            }
            catch(SQLException e){
                System.out.println("\n\nErro ao listar contas\n\nDetalhes do erro: " + e.getSQLState() + "\n\nMais detalhes: " + e.getMessage());
            }

            return contas;
    }

    public ContaCorrente buscarContaPorNumero(int numero)  throws ContaInexistenteException{
        ContaCorrente conta = null;
        String sql = "SELECT * FROM contas WHERE numero = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conta = new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("\n\nErro ao buscar conta\n\nDetalhes do erro: " + e.getMessage());
            throw new ContaInexistenteException("numero não existe");
        }
        return conta;
    }

    public void atualizarConta(ContaCorrente conta){
        String sql = "UPDATE contas SET titular=?, email=?, senha=?, saldo=? WHERE numero=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getEmail());
            stmt.setString(3, conta.getSenha());
            stmt.setDouble(4, conta.getSaldo().doubleValue());
            stmt.setInt(5, conta.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n\nErro ao atualizar conta\n\nDetalhes do erro: " + e.getSQLState() + "\n\nMais detalhes: " + e.getMessage());
        }

    }

    public void atualizaSaldo(ContaCorrente conta){
        String sql = "UPDATE contas SET saldo=? WHERE numero=?";
        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setDouble(1, conta.getSaldo().doubleValue());
                stmt.setInt(2, conta.getNumero());
                stmt.executeUpdate();
            }
            catch(SQLException e){
                System.out.println("\n\nErro ao atualizar saldo\n\nDetalhes do erro: " + e.getSQLState() + "\n\nMais detalhes: " + e.getMessage());
            }
    }

    public void remover(int numero){
        String sql = "DELETE FROM contas WHERE numero=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n\nErro ao deletar conta\n\nDetalhes do erro: " + e.getSQLState() + "\n\nMais detalhes: " + e.getMessage());
        }
    }

    public ContaCorrente loginConta(String email, String senha)  throws ContaInexistenteException{
        ContaCorrente conta = null;
        String sql = "SELECT * FROM contas WHERE email = ? AND senha = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conta = new ContaCorrente(
                        rs.getInt("numero"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("\n\nErro ao realizar login\n\nDetalhes do erro: " + e.getMessage());
            throw new ContaInexistenteException("Email ou senha inválnumeroos");
        }
        return conta;
    }

    public void transferir(int numeroOrigem, int numeroDestino, BigDecimal valor) throws ContaInexistenteException, SaldoInsuficienteException{
        String contaOrigem = "UPDATE contas SET saldo = saldo - ? WHERE numero = ?";
        String contaDestino = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement origem = conn.prepareStatement(contaOrigem);
                 PreparedStatement destino = conn.prepareStatement(contaDestino)) {

                origem.setDouble(1, valor.doubleValue());
                origem.setInt(2, numeroOrigem);
                origem.executeUpdate();

                destino.setDouble(1, valor.doubleValue());
                destino.setInt(2, numeroDestino);
                destino.executeUpdate();

                conn.commit();
                System.out.println("Transferência realizada com sucesso!");

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erro na transação. Rollback realizado!");
                System.out.println("Erro ao transferir\n\nDetalhes do erro: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao transferir\n\nDetalhes do erro: " + e.getMessage());
        }
    }

    public void movimentacoes(Movimentacao movimentacao) throws SQLException{
        String sql = "INSERT INTO movimentacoes (numero_conta, tipo, valor) VALUES (?,?,?)";

        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, movimentacao.getNumero());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setDouble(3, movimentacao.getValor().doubleValue());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("\n\nErro ao registrar movimentação\n\nDetalhes do erro: " + e.getSQLState() + "\n" + e.getMessage());
        }
    }

    public List<Movimentacao> listarExtrato(int numero) throws SQLException{
        List<Movimentacao> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacoes WHERE numero_conta = ?";
        try(Connection conn = Conexao.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numero);
                try(ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        Movimentacao m = new Movimentacao(
                            rs.getInt("id"),
                            rs.getInt("numero_conta"),
                            rs.getString("tipo"),
                            rs.getBigDecimal("valor"),
                            rs.getString("data")
                        );
                        movimentacoes.add(m);
                    }
                }catch (SQLException e) {
                    System.out.println("\n\nErro ao listar extrato\n\nDetalhes do erro: " + e.getSQLState() + "\nMais detalhes: " + e.getMessage());
                }
            }
            catch(SQLException e){
                System.out.println("\n\nErro ao listar extrato\n\nDetalhes do erro: " + e.getSQLState() + "\nMais detalhes: " + e.getMessage());
            }

            return movimentacoes;
    }
}
