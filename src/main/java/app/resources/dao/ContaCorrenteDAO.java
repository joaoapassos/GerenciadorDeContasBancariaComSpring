package app.resources.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.exception.ContaInexistenteException;
import app.exception.SaldoInsuficienteException;
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
                        rs.getInt("id"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                    contas.add(c);
                }
            }
            catch(SQLException e){
                System.out.println("\n\nErro ao listar contas\n\nDetalhes do erro: " + e.getSQLState());
            }

            return contas;
    }

    public ContaCorrente selectById(int id)  throws ContaInexistenteException{
        ContaCorrente conta = null;
        String sql = "SELECT * FROM contas WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    conta = new ContaCorrente(
                        rs.getInt("id"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("\n\nErro ao buscar conta\n\nDetalhes do erro: " + e.getMessage());
            throw new ContaInexistenteException("Id não existe");
        }
        return conta;
    }

    public void atualizarConta(ContaCorrente conta){
        String sql = "UPDATE contas SET titular=?, email=?, senha=?, saldo=? WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conta.getTitular());
            stmt.setString(2, conta.getEmail());
            stmt.setString(3, conta.getSenha());
            stmt.setDouble(4, conta.getSaldo().doubleValue());
            stmt.setInt(5, conta.getNumero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n\nErro ao atualizar conta\n\nDetalhes do erro: " + e.getSQLState());
        }

    }

    public void atualizaSaldo(ContaCorrente conta){
        String sql = "UPDATE contas SET saldo=? WHERE id=?";
        try(Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setDouble(1, conta.getSaldo().doubleValue());
                stmt.setInt(2, conta.getNumero());
                stmt.executeUpdate();
            }
            catch(SQLException e){
                System.out.println("\n\nErro ao atualizar saldo\n\nDetalhes do erro: " + e.getSQLState());
            }
    }

    public void remover(int id){
        String sql = "DELETE FROM contas WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n\nErro ao deletar conta\n\nDetalhes do erro: " + e.getSQLState());
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
                        rs.getInt("id"),
                        rs.getString("titular"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getBigDecimal("saldo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("\n\nErro ao realizar login\n\nDetalhes do erro: " + e.getMessage());
            throw new ContaInexistenteException("Email ou senha inválidos");
        }
        return conta;
    }

    public void transferir(int idOrigem, int idDestino, BigDecimal valor) throws ContaInexistenteException, SaldoInsuficienteException{
        String contaOrigem = "UPDATE contas SET saldo = saldo - ? WHERE id = ?";
        String contaDestino = "UPDATE contas SET saldo = saldo + ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement origem = conn.prepareStatement(contaOrigem);
                 PreparedStatement destino = conn.prepareStatement(contaDestino)) {

                origem.setDouble(1, valor.doubleValue());
                origem.setInt(2, idOrigem);
                origem.executeUpdate();

                destino.setDouble(1, valor.doubleValue());
                destino.setInt(2, idDestino);
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
}
