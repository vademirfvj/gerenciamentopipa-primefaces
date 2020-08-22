package opp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import opp.impl.CidadeDAOImpl;
import opp.util.Propriedades;

public class ConexaoMySQL {
	
	private static  Connection connection = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static Propriedades rpcProperties = Propriedades.getInstance();
	private static final Logger logger = Logger.getLogger(CidadeDAOImpl.class);
	
	public static Connection getConnection() {
		System.out.println("Conectando ao banco de dados");
		logger.info("Conectando ao banco de dados");
		//Conexão com o banco de dados
		String servidor = rpcProperties.buscaPropriedade("url");
		String usuario = rpcProperties.buscaPropriedade("user");
		String senha = rpcProperties.buscaPropriedade("password");
		String driver = rpcProperties.buscaPropriedade("driver");
		try {

			Class.forName("com.mysql.jdbc.Driver");
			connection =  DriverManager.getConnection(servidor, usuario, senha);
			statement = connection.createStatement();
			System.out.println("Conexão realizada com sucesso!");
			logger.info("Conexão realizada com sucesso!");
		} catch (Exception e) {
			logger.info("Erro na conexão do banco da dados"
					+ e.toString());
			System.out.println("Erro na conexão do banco da dados"
					+ e.toString());
		}
		
		return connection;
	}
	
	public static void fecharConexao(Connection conexao) throws Exception{
			try {
				if(conexao != null){
					conexao.close();
				}
			} catch (Exception e) {
				//TODO colocar log
				logger.info("Erro ao fechar Conexao:"+ e.getCause());
				System.out.println("Erro ao fechar Conexao:"+ e.getCause());
				throw new Exception(e);
			}
		}
	
	public static void finalizarSql(ResultSet rs, PreparedStatement ps) throws Exception{
		try{
			
			if(rs != null){
				rs.close();
				rs = null;
			}
			if(ps != null){
				ps.close();
				ps = null;
			}
		}catch (Exception e) {
			logger.info("Erro ao fechar Conexao:"+ e.getCause());
			System.out.println("Erro ao finalizar Conexao:"+ e.getCause());
			throw new Exception(e);
		}
		
	}
	
	public static void commitConexao(Connection conexao) throws SQLException{
		try {
			conexao.commit();
		} catch (SQLException e) {
			// inserir Log
			logger.info("Erro ao commitar Conexao:"+ e.getMessage());
			throw new SQLException(e.getMessage());
		}
	}
	
}
