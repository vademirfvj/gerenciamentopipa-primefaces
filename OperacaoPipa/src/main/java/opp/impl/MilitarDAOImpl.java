package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.MilitarDAO;
import opp.dto.MilitarDTO;
import opp.util.Constantes;

import org.apache.log4j.Logger;

public class MilitarDAOImpl implements MilitarDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6690474060773250582L;

	private static final Logger logger = Logger.getLogger(MilitarDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public MilitarDTO recuperaMilitarPorId(int id) throws Exception {
		logger.info("Iniciando MilitarDTO recuperaMilitarPorId(int id)" + "Id: " + id);
		MilitarDTO militar = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryMilitar = new StringBuffer();
			queryMilitar.append(
					  "  SELECT *                         "
					+ "  FROM militar c			          "
					+ "	 WHERE c.id = '"+id+"'            "
			);

			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				militar = new MilitarDTO();

				militar.setId(new Integer(resultSet.getInt("ID")));
				militar.setNome(resultSet.getString("NOME"));
				militar.setPostGrad(resultSet.getInt("POSTOGRAD"));
				militar.setCpf(resultSet.getString("CPF"));
				militar.setBanco(resultSet.getString("BANCO"));
				militar.setNumBanco(resultSet.getString("NUMBANCO"));
				militar.setAgencia(resultSet.getString("AGENCIA"));
				militar.setConta(resultSet.getString("CONTA"));
				militar.setTelefone(resultSet.getString("TELEFONE"));
				militar.setCelular(resultSet.getString("CELULAR"));
				militar.setEmail(resultSet.getString("EMAIL"));
				militar.setStatus(resultSet.getString("STATUS"));
			}
			
			return militar;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public MilitarDTO recuperaMilitarPorCpf(String cpf) throws Exception {
		logger.info("Iniciando MilitarDTO recuperaMilitarPorCpf(String cpf)" + "Militar: " + cpf);
		MilitarDTO militar = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryMilitar = new StringBuffer();
			queryMilitar.append(
					  "  SELECT *                         "
					+ "  FROM militar c			          "
					+ "	 WHERE c.cpf = '"+cpf+"'   "
			);

			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				militar = new MilitarDTO();

				militar.setId(new Integer(resultSet.getInt("ID")));
				militar.setNome(resultSet.getString("NOME"));
				militar.setPostGrad(resultSet.getInt("POSTOGRAD"));
				militar.setCpf(resultSet.getString("CPF"));
				militar.setBanco(resultSet.getString("BANCO"));
				militar.setNumBanco(resultSet.getString("NUMBANCO"));
				militar.setAgencia(resultSet.getString("AGENCIA"));
				militar.setConta(resultSet.getString("CONTA"));
				militar.setTelefone(resultSet.getString("TELEFONE"));
				militar.setCelular(resultSet.getString("CELULAR"));
				militar.setEmail(resultSet.getString("EMAIL"));
				militar.setStatus(resultSet.getString("STATUS"));
			}
			
			return militar;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public MilitarDTO recuperaMilitarPorNome(String militarStr) throws Exception {
		logger.info("Iniciando MilitarDTO recuperaMilitarPorNome(String militar)" + "Militar: " + militarStr);
		MilitarDTO militar = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryMilitar = new StringBuffer();
			queryMilitar.append(
					  "  SELECT *                         "
					+ "  FROM militar c			          "
					+ "	 WHERE c.nome = '"+militarStr+"'   "
			);

			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				militar = new MilitarDTO();

				militar.setId(new Integer(resultSet.getInt("ID")));
				militar.setNome(resultSet.getString("NOME"));
				militar.setPostGrad(resultSet.getInt("POSTOGRAD"));
				militar.setCpf(resultSet.getString("CPF"));
				militar.setBanco(resultSet.getString("BANCO"));
				militar.setNumBanco(resultSet.getString("NUMBANCO"));
				militar.setAgencia(resultSet.getString("AGENCIA"));
				militar.setConta(resultSet.getString("CONTA"));
				militar.setTelefone(resultSet.getString("TELEFONE"));
				militar.setCelular(resultSet.getString("CELULAR"));
				militar.setEmail(resultSet.getString("EMAIL"));
				militar.setStatus(resultSet.getString("STATUS"));
			}
			
			return militar;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<MilitarDTO> recuperaMilitarsPorNome(String nome, String status) throws Exception {
		logger.info("Iniciando List<MilitarDTO> recuperaMilitarsPorNome(String nome, String status)" 
					+ "Militar: " + nome + "Status: " + status);
		
		List<MilitarDTO> listaMilitar = new ArrayList<MilitarDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryMilitar = new StringBuffer();
			queryMilitar.append(
					  "  SELECT *                                 "
					+ "  FROM militar c			                  "
					+ "	 WHERE c.nome LIKE '"+nome+"%'         "
			);
			
			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryMilitar.append("\n  AND c.status = '"+status+"'              ");
			}

			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				MilitarDTO militar = new MilitarDTO();

				militar.setId(new Integer(resultSet.getInt("ID")));
				militar.setNome(resultSet.getString("NOME"));
				militar.setPostGrad(resultSet.getInt("POSTOGRAD"));
				militar.setCpf(resultSet.getString("CPF"));
				militar.setBanco(resultSet.getString("BANCO"));
				militar.setNumBanco(resultSet.getString("NUMBANCO"));
				militar.setAgencia(resultSet.getString("AGENCIA"));
				militar.setConta(resultSet.getString("CONTA"));
				militar.setTelefone(resultSet.getString("TELEFONE"));
				militar.setCelular(resultSet.getString("CELULAR"));
				militar.setEmail(resultSet.getString("EMAIL"));
				militar.setStatus(resultSet.getString("STATUS"));

				listaMilitar.add(militar);
			}

			return listaMilitar;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<MilitarDTO> recuperaTodosMilitares() throws Exception {
		logger.info("Iniciando List<MilitarDTO> recuperaTodosMilitars()");
		
		List<MilitarDTO> listaMilitar = new ArrayList<MilitarDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryMilitar = new StringBuffer();
			queryMilitar.append(
					  "  SELECT *                                 "
					+ "  FROM militar			                  "
			);

			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				MilitarDTO militar = new MilitarDTO();

				militar.setId(new Integer(resultSet.getInt("ID")));
				militar.setNome(resultSet.getString("NOME"));
				militar.setPostGrad(resultSet.getInt("POSTOGRAD"));
				militar.setCpf(resultSet.getString("CPF"));
				militar.setBanco(resultSet.getString("BANCO"));
				militar.setNumBanco(resultSet.getString("NUMBANCO"));
				militar.setAgencia(resultSet.getString("AGENCIA"));
				militar.setConta(resultSet.getString("CONTA"));
				militar.setTelefone(resultSet.getString("TELEFONE"));
				militar.setCelular(resultSet.getString("CELULAR"));
				militar.setEmail(resultSet.getString("EMAIL"));
				militar.setStatus(resultSet.getString("STATUS"));

				listaMilitar.add(militar);
			}

			return listaMilitar;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void insereMilitar(MilitarDTO militar) throws Exception{
		logger.info("Iniciando void insereMilitar(String militar" + "Militar: " + militar);
		
		int id = 0;
		
		if(militar.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = militar.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryMilitar = new StringBuffer();
			
			queryMilitar.append(
					" INSERT INTO `militar` (                 " +
					" `ID`,                                   " +
					" `NOME`,                                 " +
					" `POSTOGRAD`,                            " +
					" `CPF`,                                  " +
					" `BANCO`,                                " +
					" `NUMBANCO`,                             " +
					" `AGENCIA`,                              " +
					" `CONTA`,                                " +
					" `TELEFONE`,                             " +
					" `CELULAR`,                              " +
					" `EMAIL`,                                " +
					" `STATUS`                                " +
					")                                        " +
					" VALUES (                                " +
					" '"+ id                             +"', " +
					" '"+ militar.getNome().toUpperCase()              +"', "+
					" '"+ militar.getPostGrad()          +"', "+
					" '"+ militar.getCpf()               +"', "+
					" '"+ militar.getBanco()             +"', "+
					" '"+ militar.getNumBanco()          +"', "+
					" '"+ militar.getAgencia()           +"', "+
					" '"+ militar.getConta()             +"', "+
					" '"+ militar.getTelefone()          +"', "+
					" '"+ militar.getCelular()           +"', "+
					" '"+ militar.getEmail()             +"', "+
					" '"+ militar.getStatus()	         +"'  "+	
					")                                        " 
					);
			
			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			statement.executeUpdate();
			
		}catch(Exception ex){
			throw new Exception(ex);
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public void atualizaMilitar(MilitarDTO militar) throws Exception{
		logger.info("Iniciando void atualizaMilitar(String militar" + "Militar: " + militar);
		
		int id = 0;
		
		if(militar.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = militar.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryMilitar = new StringBuffer();

			queryMilitar.append(
					" UPDATE militar  SET     " +
					" `NOME` =                " +  " '"+ militar.getNome()              +"', "+
					" `POSTOGRAD` =           " +  " '"+ militar.getPostGrad()          +"', "+
					" `CPF` =                 " +  " '"+ militar.getCpf()               +"', "+
					" `BANCO` =               " +  " '"+ militar.getBanco()             +"', "+
					" `NUMBANCO` =            " +  " '"+ militar.getNumBanco()          +"', "+
					" `AGENCIA` =             " +  " '"+ militar.getAgencia()           +"', "+
					" `CONTA` =               " +  " '"+ militar.getConta()             +"', "+
					" `TELEFONE` =            " +  " '"+ militar.getTelefone()          +"', "+
					" `CELULAR` =             " +  " '"+ militar.getCelular()           +"', "+
					" `EMAIL` =               " +  " '"+ militar.getEmail()             +"', "+
					" `STATUS` =              " +  " '"+ militar.getStatus()	        +"'  "+
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryMilitar.toString());
			statement = conecction.prepareStatement(queryMilitar.toString());
			statement.executeUpdate();
			
		}catch(Exception ex){
			throw new Exception(ex);
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

	private int recuperaProximoId() throws Exception{
		
		int retorno=0;
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryMilitar = new StringBuffer();

			queryMilitar.append("select max(id) max_id from militar");

			statement = conecction.prepareStatement(queryMilitar.toString());
			resultSet = statement.executeQuery();
			
			while ( resultSet.next() ){
				if(null != resultSet){
					String valor = resultSet.getString("max_id");
	
					if(null == valor){
						retorno = 1;
					}else{
						retorno = Integer.valueOf(valor)+1;
					}
				}
			}
			
		}catch(Exception ex){
		
			throw new Exception(ex);
				
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		return retorno;
	}
	
	
}
