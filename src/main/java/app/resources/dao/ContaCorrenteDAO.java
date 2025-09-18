package app.resources.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public ContaCorrente selectById(int id){
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
}
