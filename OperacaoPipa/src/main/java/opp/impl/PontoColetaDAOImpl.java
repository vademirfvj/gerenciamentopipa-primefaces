package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.PontoColetaDAO;
import opp.dto.PontoColetaDTO;
import opp.util.Constantes;

import org.apache.log4j.Logger;

public class PontoColetaDAOImpl implements PontoColetaDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5431765097209744304L;
	private static final Logger logger = Logger.getLogger(PontoColetaDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	@Override
	public PontoColetaDTO recuperaPontoColetaPorNome(String pontoColeta) throws Exception {
		logger.info("Iniciando PontoColetaDTO recuperaPontoColetaPorNome(String pontoColeta)" + "PontoColeta: " + pontoColeta);
		PontoColetaDTO pc = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPC = new StringBuffer();
			queryPC.append(
					  "  SELECT *                                    "
					+ "  FROM PONTOCOLETA pc			             "
					+ "	 WHERE upper(pc.pontocoleta) = upper('"+pontoColeta+"')    "
			);

			logger.info("Query: " + queryPC.toString());
			statement = conecction.prepareStatement(queryPC.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				pc = new PontoColetaDTO();

				pc.setId(new Integer(resultSet.getInt("ID")));
				pc.setPontoColeta(resultSet.getString("PONTOCOLETA"));
				pc.setGeoLocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				pc.setStatus(resultSet.getString("STATUS"));
				pc.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));

			}
			
			return pc;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public List<PontoColetaDTO> recuperaTodosPontosColeta() throws Exception {
		logger.info("Iniciando List<PontoColetaDTO> recuperaTodosPontosColeta()");
		
		List<PontoColetaDTO> listaPC = new ArrayList<PontoColetaDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPC = new StringBuffer();
			queryPC.append(
					  "  SELECT *                   "
					+ "  FROM PONTOCOLETA pc	    "
					+ "  ORDER BY PONTOCOLETA       "
			);

			logger.info("Query: "+queryPC.toString());
			
			statement = conecction.prepareStatement(queryPC.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PontoColetaDTO pcDTO = new PontoColetaDTO();

				pcDTO.setId(new Integer(resultSet.getInt("ID")));
				pcDTO.setPontoColeta(resultSet.getString("PONTOCOLETA"));
				pcDTO.setGeoLocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				pcDTO.setStatus(resultSet.getString("STATUS"));
				pcDTO.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));

				listaPC.add(pcDTO);
			}

			return listaPC;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public List<PontoColetaDTO> recuperaPontosColetaPorNome(String pontoColeta, String status) throws Exception {
		logger.info("Iniciando List<PontoColetaDTO> recuperaPontosColetaPorNome(String pontoColeta, String status)" 
					+ "PontoColeta: " + pontoColeta + "Status: " + status);
		
		List<PontoColetaDTO> listaPC = new ArrayList<PontoColetaDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPC = new StringBuffer();
			queryPC.append(
					  "  SELECT *                                       "
					+ "  FROM PONTOCOLETA pc			                "
					+ "	 WHERE pc.pontocoleta LIKE '"+pontoColeta+"%'  "
			);

			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryPC.append("\n  AND pc.status = '"+status+"'              ");
			}
			
			logger.info("Query: " + queryPC.toString());
			statement = conecction.prepareStatement(queryPC.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PontoColetaDTO pcDTO = new PontoColetaDTO();

				pcDTO.setId(new Integer(resultSet.getInt("ID")));
				pcDTO.setPontoColeta(resultSet.getString("PONTOCOLETA"));
				pcDTO.setGeoLocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				pcDTO.setStatus(resultSet.getString("STATUS"));
				pcDTO.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));

				listaPC.add(pcDTO);
			}

			return listaPC;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void inserePontoColeta(PontoColetaDTO pontoColeta) throws Exception{
		logger.info("Iniciando void inserePontoColeta(String pontoColeta" + "PontoColeta: " + pontoColeta);
		
		long id = 0;
		
		if(pontoColeta.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pontoColeta.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoColeta = new StringBuffer();
			
			queryPontoColeta.append(
					" INSERT INTO pontocoleta                              " +
				    "     (                                                " +
				    " 		`ID`,                                          " +
				    " 		`PONTOCOLETA`,                                 " +
				    " 		`GEOLOCALIZACAO`,                              " +
				    " 		`STATUS`,                                      " +
				    " 		`CODGCDA`                                      " +
				    " 	)VALUES(                                           " +
				    " 		'"+id                              +"',        " +  
				    " 		'"+pontoColeta.getPontoColeta().toUpperCase()    +"',        " +  
				    " 		'"+pontoColeta.getGeoLocalizacao() +"',        " +
				    " 		'"+pontoColeta.getStatus()         +"',        " +
				    " 		'"+pontoColeta.getCodGcda()        +"'         " +
				    " 	)                                                  " 
			 );
			
			logger.info("Query: " + queryPontoColeta.toString());
			statement = conecction.prepareStatement(queryPontoColeta.toString());
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
	public void atualizaPontoColeta(PontoColetaDTO pontoColeta) throws Exception{
		logger.info("Iniciando void atualizaPontoColeta(String pontoColeta" + "PontoColeta: " + pontoColeta);
		
		int id = 0;
		
		if(pontoColeta.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pontoColeta.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoColeta = new StringBuffer();

			queryPontoColeta.append(
					" UPDATE pontocoleta  SET                                         " +
				    " 		 `PONTOCOLETA` = '"+ pontoColeta.getPontoColeta()    +"', " +
				    " 	  `GEOLOCALIZACAO` = '"+ pontoColeta.getGeoLocalizacao() +"', " +
				    " 		      `STATUS` = '"+ pontoColeta.getStatus()         +"', " +
				    " 		     `CODGCDA` = '"+ pontoColeta.getCodGcda()        +"'  " +
				    " WHERE ID =             '"+ id                              +"'  "
			 );
			
			
			
			logger.info("Query: " + queryPontoColeta.toString());
			statement = conecction.prepareStatement(queryPontoColeta.toString());
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
			StringBuffer queryPC = new StringBuffer();

			queryPC.append("select max(id) max_id from pontocoleta");

			statement = conecction.prepareStatement(queryPC.toString());
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
