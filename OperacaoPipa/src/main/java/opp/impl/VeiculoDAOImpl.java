package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.VeiculoDAO;
import opp.dto.VeiculoDTO;
import opp.util.Constantes;

import org.apache.log4j.Logger;

public class VeiculoDAOImpl implements VeiculoDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5966924990252581215L;

	private static final Logger logger = Logger.getLogger(VeiculoDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public VeiculoDTO recuperaVeiculoPorId(int id) throws Exception {
		logger.info("Iniciando VeiculoDTO recuperaVeiculoPorId(int id)" + "Id: " + id);
		VeiculoDTO veiculo = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryVeiculo = new StringBuffer();
			queryVeiculo.append(
					  "  SELECT *                         "
					+ "  FROM veiculo c			          "
					+ "	 WHERE c.id = '"+id+"'            "
			);

			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				veiculo = new VeiculoDTO();

				veiculo.setId(new Integer(resultSet.getInt("ID")));
				veiculo.setPlaca(resultSet.getString("PLACA"));
				veiculo.setRenavam(resultSet.getString("RENAVAM"));
				veiculo.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				veiculo.setMarcaModelo(resultSet.getString("MARCAMODELO"));
				veiculo.setStatus(resultSet.getString("STATUS"));
			}
			
			return veiculo;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public VeiculoDTO recuperaVeiculoPorPlaca(String placa) throws Exception {
		logger.info("Iniciando VeiculoDTO recuperaVeiculoPorPlaca(String placa)" + "Placa: " + placa);
		VeiculoDTO veiculo = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryVeiculo = new StringBuffer();
			queryVeiculo.append(
					  "  SELECT *                         "
					+ "  FROM veiculo c			          "
					+ "	 WHERE upper(c.placa) = upper('"+placa+"')   "
			);

			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				veiculo = new VeiculoDTO();

				veiculo.setId(new Integer(resultSet.getInt("ID")));
				veiculo.setPlaca(resultSet.getString("PLACA"));
				veiculo.setRenavam(resultSet.getString("RENAVAM"));
				veiculo.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				veiculo.setMarcaModelo(resultSet.getString("MARCAMODELO"));
				veiculo.setStatus(resultSet.getString("STATUS"));
			}
			
			return veiculo;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<VeiculoDTO> recuperaVeiculosPorPlaca(String placa, String status) throws Exception {
		logger.info("Iniciando List<VeiculoDTO> recuperaVeiculosPorPlaca(String placa, String status)" 
					+ "Veiculo: " + placa + "Status: " + status);
		
		List<VeiculoDTO> listaVeiculo = new ArrayList<VeiculoDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryVeiculo = new StringBuffer();
			queryVeiculo.append(
					  "  SELECT *                                 "
					+ "  FROM veiculo c			                  "
					+ "	 WHERE c.placa LIKE '"+placa+"%'         "
			);

			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryVeiculo.append("\n  AND c.status = '"+status+"'              ");
			}
			
			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				VeiculoDTO veiculo = new VeiculoDTO();

				veiculo.setId(new Integer(resultSet.getInt("ID")));
				veiculo.setPlaca(resultSet.getString("PLACA"));
				veiculo.setRenavam(resultSet.getString("RENAVAM"));
				veiculo.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				veiculo.setMarcaModelo(resultSet.getString("MARCAMODELO"));
				veiculo.setStatus(resultSet.getString("STATUS"));

				listaVeiculo.add(veiculo);
			}

			return listaVeiculo;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<VeiculoDTO> recuperaTodosVeiculosAtivos() throws Exception {
		logger.info("Iniciando List<VeiculoDTO> recuperaTodosVeiculos()");
		
		List<VeiculoDTO> listaVeiculo = new ArrayList<VeiculoDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryVeiculo = new StringBuffer();
			queryVeiculo.append(
					  "  SELECT *                                 "
					+ "  FROM veiculo		"
					+ " WHERE STATUS = 'ATIVO'	                  "
			);

			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				VeiculoDTO veiculo = new VeiculoDTO();

				veiculo.setId(new Integer(resultSet.getInt("ID")));
				veiculo.setPlaca(resultSet.getString("PLACA"));
				veiculo.setRenavam(resultSet.getString("RENAVAM"));
				veiculo.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				veiculo.setMarcaModelo(resultSet.getString("MARCAMODELO"));
				veiculo.setStatus(resultSet.getString("STATUS"));

				listaVeiculo.add(veiculo);
			}

			return listaVeiculo;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<VeiculoDTO> recuperaTodosVeiculos() throws Exception {
		logger.info("Iniciando List<VeiculoDTO> recuperaTodosVeiculos()");
		
		List<VeiculoDTO> listaVeiculo = new ArrayList<VeiculoDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryVeiculo = new StringBuffer();
			queryVeiculo.append(
					  "  SELECT *                                 "
					+ "  FROM veiculo			                  "
			);

			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				VeiculoDTO veiculo = new VeiculoDTO();

				veiculo.setId(new Integer(resultSet.getInt("ID")));
				veiculo.setPlaca(resultSet.getString("PLACA"));
				veiculo.setRenavam(resultSet.getString("RENAVAM"));
				veiculo.setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				veiculo.setMarcaModelo(resultSet.getString("MARCAMODELO"));
				veiculo.setStatus(resultSet.getString("STATUS"));

				listaVeiculo.add(veiculo);
			}

			return listaVeiculo;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void insereVeiculo(VeiculoDTO veiculo) throws Exception{
		logger.info("Iniciando void insereVeiculo(String veiculo" + "Veiculo: " + veiculo);
		
		int id = 0;
		
		if(veiculo.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = veiculo.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryVeiculo = new StringBuffer();
			
			queryVeiculo.append(
					" INSERT INTO `veiculo` (                 " +
					" `ID`,                                   " +
					" `PLACA` ,                               " +
					" `RENAVAM` ,                             " +
					" `CAPACIDADEPIPA` ,                      " +
					" `MARCAMODELO`,                          " +							
					" `STATUS`                                " +
					")                                        " +
					" VALUES (                                " +
					" '"+ id                                  +"', " +
					" '"+ veiculo.getPlaca().toUpperCase()                  +"', "+
					" '"+ veiculo.getRenavam()                +"', "+
					" '"+ veiculo.getCapacidadePipa()         +"', "+
					" '"+ veiculo.getMarcaModelo()            +"', "+
					" '"+ veiculo.getStatus()	              +"'  "+	
					")                                        " 
					);
			
			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
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
	public void atualizaVeiculo(VeiculoDTO veiculo) throws Exception{
		logger.info("Iniciando void atualizaVeiculo(String veiculo" + "Veiculo: " + veiculo);
		
		int id = 0;
		
		if(veiculo.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = veiculo.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryVeiculo = new StringBuffer();

			queryVeiculo.append(
					" UPDATE veiculo  SET     " +
					" `PLACA` =               " +  " '"+ veiculo.getPlaca()             +"', "+
					" `RENAVAM` =             " +  " '"+ veiculo.getRenavam()           +"', "+
					" `CAPACIDADEPIPA` =      " +  " '"+ veiculo.getCapacidadePipa()    +"', "+
					" `MARCAMODELO` =         " +  " '"+ veiculo.getMarcaModelo()       +"', "+
					" `STATUS` =              " +  " '"+ veiculo.getStatus()	        +"'  "+
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryVeiculo.toString());
			statement = conecction.prepareStatement(queryVeiculo.toString());
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
			StringBuffer queryVeiculo = new StringBuffer();

			queryVeiculo.append("select max(id) max_id from veiculo");

			statement = conecction.prepareStatement(queryVeiculo.toString());
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
