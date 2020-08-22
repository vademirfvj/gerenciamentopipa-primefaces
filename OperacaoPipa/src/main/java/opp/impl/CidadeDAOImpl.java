package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.CidadeDAO;
import opp.dto.CidadeDTO;
import opp.util.Constantes;

import org.apache.log4j.Logger;

public class CidadeDAOImpl implements CidadeDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6376054978523110503L;
	private static final Logger logger = Logger.getLogger(CidadeDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public CidadeDTO recuperaCidadePorId(int id) throws Exception {
		logger.info("Iniciando CidadeDTO recuperaCidadePorId(int id)" + "Id: " + id);
		CidadeDTO cidade = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "\n  SELECT *                         "
					+ "\n  FROM cidade c, pontoColeta pc    "
					+ "\n	 WHERE c.id = '"+id+"'            "
					+ "\n  AND c.CODPONTOCOLETA = pc.ID     "
			);

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				cidade = new CidadeDTO();

				cidade.setId(new Integer(resultSet.getInt("ID")));
				cidade.setCidade(resultSet.getString("NOME"));
				cidade.setUf(resultSet.getString("UF"));
				cidade.setCodPontoColeta(new Integer(resultSet.getInt("CODPONTOCOLETA")));
				cidade.setGeolocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				cidade.setNumHabitantes(new Integer(resultSet.getInt("NRHABITANTES")));
				cidade.setEmail(resultSet.getString("EMAIL"));
				cidade.setCarr_max(new Integer(resultSet.getInt("CARRMAX")));
				cidade.setCoordSopMunicipal(resultSet.getString("COORDSOPMUNICIPAL"));
				cidade.setFoneSopMunicipal(resultSet.getString("FONESOPMUNICIPAL"));
				cidade.setNumPipeiros(new Integer(resultSet.getInt("NUMPIPEIROS")));
				cidade.setStatus(resultSet.getString("STATUS"));
				cidade.getPontoColetaDTO().setPontoColeta(resultSet.getString("PONTOCOLETA"));
				cidade.getPontoColetaDTO().setCodGcda(resultSet.getInt("CODGCDA"));
			}
			
			return cidade;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public CidadeDTO recuperaCidadePorNome(String cidadeStr) throws Exception {
		logger.info("Iniciando CidadeDTO recuperaCidadePorNome(String cidade)" + "Cidade: " + cidadeStr);
		CidadeDTO cidade = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "\n  SELECT *                         "
					+ "\n  FROM cidade c			          "
					+ "\n	 WHERE upper(c.nome) = upper('"+cidadeStr+"')   "
			);

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				cidade = new CidadeDTO();

				cidade.setId(new Integer(resultSet.getInt("ID")));
				cidade.setCidade(resultSet.getString("NOME"));
				cidade.setUf(resultSet.getString("UF"));
				cidade.setCodPontoColeta(new Integer(resultSet.getInt("CODPONTOCOLETA")));
				cidade.setGeolocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				cidade.setNumHabitantes(new Integer(resultSet.getInt("NRHABITANTES")));
				cidade.setEmail(resultSet.getString("EMAIL"));
				cidade.setCarr_max(new Integer(resultSet.getInt("CARRMAX")));
				cidade.setCoordSopMunicipal(resultSet.getString("COORDSOPMUNICIPAL"));
				cidade.setFoneSopMunicipal(resultSet.getString("FONESOPMUNICIPAL"));
				cidade.setNumPipeiros(new Integer(resultSet.getInt("NUMPIPEIROS")));
				cidade.setStatus(resultSet.getString("STATUS"));
			}
			
			return cidade;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<CidadeDTO> recuperaCidadesPorNome(String cidade, String status) throws Exception {
		logger.info("Iniciando List<CidadeDTO> recuperaCidadesPorNome(String cidade, String status)" 
					+ "Cidade: " + cidade + "Status: " + status);
		
		List<CidadeDTO> listaCidade = new ArrayList<CidadeDTO>();
		
		try {
			

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "\n  SELECT *                                 "
					+ "\n FROM cidade c			                  "
					+ "\n	 WHERE c.nome LIKE '"+cidade+"%'         ");
			
			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryCidade.append("\n  AND c.status = '"+status+"'              ");
			}

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				CidadeDTO cidadeDTO = new CidadeDTO();

				cidadeDTO.setId(new Integer(resultSet.getInt("ID")));
				cidadeDTO.setCidade(resultSet.getString("NOME"));
				cidadeDTO.setUf(resultSet.getString("UF"));
				cidadeDTO.setCodPontoColeta(new Integer(resultSet.getInt("CODPONTOCOLETA")));
				cidadeDTO.setGeolocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				cidadeDTO.setEmail(resultSet.getString("EMAIL"));
				cidadeDTO.setCarr_max(new Integer(resultSet.getInt("CARRMAX")));
				cidadeDTO.setCoordSopMunicipal(resultSet.getString("COORDSOPMUNICIPAL"));
				cidadeDTO.setFoneSopMunicipal(resultSet.getString("FONESOPMUNICIPAL"));
				cidadeDTO.setStatus(resultSet.getString("STATUS"));

				listaCidade.add(cidadeDTO);
			}
			
			CidadeDTO c1;
			for (int i = 0; i < listaCidade.size(); i++) {
				
				c1 = listaCidade.get(i);
				
				c1.setNumHabitantes(recuperaQuantHabitantesPorCidade(c1.getId()));
				
				c1.setNumPipeiros(recuperaQuantPipeirosPorCidade(c1.getId()));

			}

			return listaCidade;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	private int recuperaQuantPipeirosPorCidade(int id) throws Exception {
		logger.info("Iniciando recuperaQuantPipeirosPorCidade " + "Id: " + id);
		
		int retorno = 0;
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "  SELECT count(*) as soma FROM pipeiro where CodCidade = ?");

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				retorno = resultSet.getInt("soma");
				
				return retorno;
				
			}
			
			return retorno;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}


	private int recuperaQuantHabitantesPorCidade(int id) throws Exception {
		logger.info("Iniciando recuperaQuantHabitantesPorCidade " + "Id: " + id);
		
		int retorno = 0;
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "  SELECT SUM(Populacao) as soma FROM pontoabastecimento where CODCIDADE = ?");

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				retorno = resultSet.getInt("soma");
				
				return retorno;
				
			}
			
			return retorno;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}


	@Override
	public List<CidadeDTO> recuperaTodasCidadesAtivas() throws Exception {
		logger.info("Iniciando List<CidadeDTO> recuperaTodasCidades()");
		
		List<CidadeDTO> listaCidade = new ArrayList<CidadeDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "\n  SELECT c.*,p.ID as ID_PONTO_C,p.PONTOCOLETA as NOME_PONTO_C FROM cidade c, pontocoleta p                                "
					+ "\n  where c.CODPONTOCOLETA = p.ID			     "
					+ "\n AND c.STATUS = 'ATIVO'            "
			);

			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				CidadeDTO cidadeDTO = new CidadeDTO();

				cidadeDTO.setId(new Integer(resultSet.getInt("ID")));
				cidadeDTO.setCidade(resultSet.getString("NOME"));
				cidadeDTO.setUf(resultSet.getString("UF"));
				cidadeDTO.setCodPontoColeta(new Integer(resultSet.getInt("CODPONTOCOLETA")));
				cidadeDTO.setGeolocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				cidadeDTO.setNumHabitantes(new Integer(resultSet.getInt("NRHABITANTES")));
				cidadeDTO.setEmail(resultSet.getString("EMAIL"));
				cidadeDTO.setCarr_max(new Integer(resultSet.getInt("CARRMAX")));
				cidadeDTO.setCoordSopMunicipal(resultSet.getString("COORDSOPMUNICIPAL"));
				cidadeDTO.setFoneSopMunicipal(resultSet.getString("FONESOPMUNICIPAL"));
				cidadeDTO.setNumPipeiros(new Integer(resultSet.getInt("NUMPIPEIROS")));
				cidadeDTO.setStatus(resultSet.getString("STATUS"));

                                cidadeDTO.getPontoColetaDTO().setId(resultSet.getInt("ID_PONTO_C"));
				cidadeDTO.getPontoColetaDTO().setPontoColeta(resultSet.getString("NOME_PONTO_C"));
				listaCidade.add(cidadeDTO);
			}

			return listaCidade;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	
	@Override
	public List<CidadeDTO> recuperaTodasCidades() throws Exception {
		logger.info("Iniciando List<CidadeDTO> recuperaTodasCidades()");
		
		List<CidadeDTO> listaCidade = new ArrayList<CidadeDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryCidade = new StringBuffer();
			queryCidade.append(
					  "  SELECT *                                 "
					+ "  FROM cidade			                  "
			);

			statement = conecction.prepareStatement(queryCidade.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				CidadeDTO cidadeDTO = new CidadeDTO();

				cidadeDTO.setId(new Integer(resultSet.getInt("ID")));
				cidadeDTO.setCidade(resultSet.getString("NOME"));
				cidadeDTO.setUf(resultSet.getString("UF"));
				cidadeDTO.setCodPontoColeta(new Integer(resultSet.getInt("CODPONTOCOLETA")));
				cidadeDTO.setGeolocalizacao(resultSet.getString("GEOLOCALIZACAO"));
				cidadeDTO.setEmail(resultSet.getString("EMAIL"));
				cidadeDTO.setCarr_max(new Integer(resultSet.getInt("CARRMAX")));
				cidadeDTO.setCoordSopMunicipal(resultSet.getString("COORDSOPMUNICIPAL"));
				cidadeDTO.setFoneSopMunicipal(resultSet.getString("FONESOPMUNICIPAL"));
				cidadeDTO.setStatus(resultSet.getString("STATUS"));

				listaCidade.add(cidadeDTO);
			}
			
			CidadeDTO c1;
			for (int i = 0; i < listaCidade.size(); i++) {
				
				c1 = listaCidade.get(i);
				
				c1.setNumHabitantes(recuperaQuantHabitantesPorCidade(c1.getId()));
				
				c1.setNumPipeiros(recuperaQuantPipeirosPorCidade(c1.getId()));

			}


			return listaCidade;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void insereCidade(CidadeDTO cidade) throws Exception{
		logger.info("Iniciando void insereCidade(String cidade" + "Cidade: " + cidade);
		
		int id = 0;
		
		if(cidade.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = cidade.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryCidade = new StringBuffer();
			
			queryCidade.append(
					" INSERT INTO `cidade` (                  " +
					" `ID`,                                   " +
					" `NOME`,                                 " +
					" `UF`,                                   " +
					" `CODPONTOCOLETA`,                       " +
					" `GEOLOCALIZACAO`,                       " +
					" `NRHABITANTES`,                         " +
					" `EMAIL`,                                " +
					" `CARRMAX`,                              " +
					" `COORDSOPMUNICIPAL`,                    " +
					" `FONESOPMUNICIPAL`,                     " +
					" `NUMPIPEIROS`,                          " +
					" `STATUS`)                               " +
					" VALUES (                                " +
					" '"+ id                             +"', " +
					" '"+ cidade.getCidade().toUpperCase()   +"', " +
					" '"+ cidade.getUf()                 +"', " +
					" '"+ cidade.getCodPontoColeta()     +"', " +
					" '"+ cidade.getGeolocalizacao()     +"', " +
					" '"+ cidade.getNumHabitantes()      +"', " +
					" '"+ cidade.getEmail()              +"', " +
					" '"+ cidade.getCarr_max()           +"', " +
					" '"+ cidade.getCoordSopMunicipal()  +"', " +
					" '"+ cidade.getFoneSopMunicipal()   +"', " +
					" '"+ cidade.getNumPipeiros()        +"', " +
					" '"+ cidade.getStatus()             +"' )" 
					);
			
			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
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
	public void atualizaCidade(CidadeDTO cidade) throws Exception{
		logger.info("Iniciando void atualizaCidade(String cidade" + "Cidade: " + cidade);
		
		int id = 0;
		
		if(cidade.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = cidade.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryCidade = new StringBuffer();

			queryCidade.append(
					" UPDATE cidade  SET      " +
					" `NOME` =                " +  " '"+ cidade.getCidade()             +"', "  +
					" `UF` =                  " +  " '"+ cidade.getUf()                 +"', "  +
					" `CODPONTOCOLETA` =      " +  " '"+ cidade.getCodPontoColeta()     +"', "  +
					" `GEOLOCALIZACAO` =      " +  " '"+ cidade.getGeolocalizacao()     +"', "  +
					" `NRHABITANTES` =        " +  " '"+ cidade.getNumHabitantes()      +"', "  +
					" `EMAIL` =               " +  " '"+ cidade.getEmail()              +"', "  +
					" `CARRMAX` =             " +  " '"+ cidade.getCarr_max()           +"', "  +
					" `COORDSOPMUNICIPAL` =   " +  " '"+ cidade.getCoordSopMunicipal()  +"', "  +
					" `FONESOPMUNICIPAL` =    " +  " '"+ cidade.getFoneSopMunicipal()   +"', "  +
					" `NUMPIPEIROS` =         " +  " '"+ cidade.getNumPipeiros()        +"', "  +
					" `STATUS` =              " +  " '"+ cidade.getStatus()             +"'  "  +
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryCidade.toString());
			
			statement = conecction.prepareStatement(queryCidade.toString());
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
		logger.info("Iniciando void recuperaProximoId()");
		int retorno=0;
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryCidade = new StringBuffer();

			queryCidade.append("select max(id) max_id from cidade");

			statement = conecction.prepareStatement(queryCidade.toString());
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
