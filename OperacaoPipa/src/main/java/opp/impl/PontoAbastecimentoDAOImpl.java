package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.PontoAbastecimentoDAO;
import opp.dto.IndiceDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.util.Constantes;
import opp.util.Funcoes;

import org.apache.log4j.Logger;

public class PontoAbastecimentoDAOImpl implements PontoAbastecimentoDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7019029172253214370L;

	private static final Logger logger = Logger.getLogger(PontoAbastecimentoDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public PontoAbastecimentoDTO recuperaPontoAbastecimentoPorId(int id) throws Exception {
		logger.info("Iniciando PontoAbastecimentoDTO recuperaPontoAbastecimentoPorId(int id)" + "Id: " + id);
		PontoAbastecimentoDTO pontoAbastecimento = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPontoAbastecimento = new StringBuffer();
			queryPontoAbastecimento.append(
					  "  SELECT p.*, i.INDICE as INDICE, i.DSC_INDICE as MOMENTOTRANSPORTE      "
					+ "  FROM pontoabastecimento p, indice i		          "
					+ "	 WHERE p.id = '"+id+"'            "
					+ "  AND p.COD_INDICE = i.id "
			);

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				pontoAbastecimento = new PontoAbastecimentoDTO();

				pontoAbastecimento.setId(new Integer(resultSet.getInt("ID")));
				pontoAbastecimento.setComunidade(resultSet.getString("COMUNIDADE"));
				pontoAbastecimento.setPopulacao(new Integer(resultSet.getInt("POPULACAO")));
				pontoAbastecimento.setDistancia(new Double(resultSet.getDouble("DISTANCIA")));
				pontoAbastecimento.setMomento(resultSet.getString("MOMENTOTRANSPORTE"));
				pontoAbastecimento.setCodcidade(new Integer(resultSet.getInt("CODCIDADE")));
				pontoAbastecimento.setGeoreferenciamento(resultSet.getString("GEOREFERENCIAMENTO"));
				pontoAbastecimento.setVolume(resultSet.getDouble("VOLUME"));
				pontoAbastecimento.setAsfalto(resultSet.getDouble("ASFALTO"));
				pontoAbastecimento.setTerra(resultSet.getDouble("TERRA"));
				pontoAbastecimento.setIndice(new Double(resultSet.getDouble("INDICE")));
				pontoAbastecimento.setApontador(resultSet.getString("APONTADOR"));
				pontoAbastecimento.setSubstituto(resultSet.getString("SUBSTITUTO"));
				pontoAbastecimento.setStatus(resultSet.getString("STATUS"));
				pontoAbastecimento.setPopulacao5L(new Integer(resultSet.getInt("POPULACAO_5L")));
				pontoAbastecimento.setPopulacaoDefault(new Integer(resultSet.getInt("POPULACAO_DEFAULT")));
				pontoAbastecimento.setLitrosDiario(new Integer(resultSet.getInt("LITROS_DIARIO")));
				pontoAbastecimento.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));
			}
			
			return pontoAbastecimento;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public PontoAbastecimentoDTO recuperaPontoAbastecimentoPorComunidade(String comunidade, int codCidade) throws Exception {
		logger.info("Iniciando PontoAbastecimentoDTO recuperaPontoAbastecimentoPorComunidade(String comunidade)" + "Comunidade: " + comunidade);
		PontoAbastecimentoDTO pontoAbastecimento = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPontoAbastecimento = new StringBuffer();
			queryPontoAbastecimento.append(
					  "  SELECT p.*, i.INDICE as INDICE, i.DSC_INDICE as MOMENTOTRANSPORTE                       "
					+ "  FROM pontoabastecimento p, indice i			          "
					+ "	 WHERE upper(p.comunidade) = upper('"+comunidade+"')   "
					+ "  AND p.COD_INDICE = i.id "
					+ "  AND p.codcidade = "+codCidade
			);

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				pontoAbastecimento = new PontoAbastecimentoDTO();

				pontoAbastecimento.setId(new Integer(resultSet.getInt("ID")));
				pontoAbastecimento.setComunidade(resultSet.getString("COMUNIDADE"));
				pontoAbastecimento.setPopulacao(new Integer(resultSet.getInt("POPULACAO")));
				pontoAbastecimento.setDistancia(new Double(resultSet.getDouble("DISTANCIA")));
				pontoAbastecimento.setMomento(resultSet.getString("MOMENTOTRANSPORTE"));
				pontoAbastecimento.setCodcidade(new Integer(resultSet.getInt("CODCIDADE")));
				pontoAbastecimento.setGeoreferenciamento(resultSet.getString("GEOREFERENCIAMENTO"));
				pontoAbastecimento.setVolume(resultSet.getDouble("VOLUME"));
				pontoAbastecimento.setAsfalto(resultSet.getDouble("ASFALTO"));
				pontoAbastecimento.setTerra(resultSet.getDouble("TERRA"));
				pontoAbastecimento.setIndice(new Double(resultSet.getDouble("INDICE")));
				pontoAbastecimento.setApontador(resultSet.getString("APONTADOR"));
				pontoAbastecimento.setSubstituto(resultSet.getString("SUBSTITUTO"));
				pontoAbastecimento.setStatus(resultSet.getString("STATUS"));
				pontoAbastecimento.setPopulacao5L(new Integer(resultSet.getInt("POPULACAO_5L")));
				pontoAbastecimento.setPopulacaoDefault(new Integer(resultSet.getInt("POPULACAO_DEFAULT")));
				pontoAbastecimento.setLitrosDiario(new Integer(resultSet.getInt("LITROS_DIARIO")));
				pontoAbastecimento.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));
			}
			
			return pontoAbastecimento;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<PontoAbastecimentoDTO> recuperaPontoAbastecimentosPorComunidade(String comunidade, String status, int codcidadePesquisa) throws Exception {
		logger.info("Iniciando List<PontoAbastecimentoDTO> recuperaPontoAbastecimentosPorComunidade(String comunidade, String status)" 
					+ "PontoAbastecimento: " + comunidade + "Status: " + status);
		
		List<PontoAbastecimentoDTO> listaPontoAbastecimento = new ArrayList<PontoAbastecimentoDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPontoAbastecimento = new StringBuffer();
			queryPontoAbastecimento.append(
					  "  SELECT p.*, i.INDICE as INDICE, i.DSC_INDICE as MOMENTOTRANSPORTE, i.ID as CODINDICE, c.NOME as NOMECIDADE "
					+ "  FROM pontoabastecimento p, indice i, cidade c			                  "
					+ "	 WHERE p.comunidade LIKE '"+comunidade+"%'         "
					+ "  AND p.codcidade = c.id "
			);

			if(!Funcoes.isNuloOuVazioZero(codcidadePesquisa)){
				queryPontoAbastecimento.append(" AND p.CODCIDADE = "+codcidadePesquisa+" ");
			}
			
			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryPontoAbastecimento.append("\n  AND p.status = '"+status+"'              ");
			}
			
			queryPontoAbastecimento.append("  AND p.COD_INDICE = i.id "
										 + "  order by p.COMUNIDADE");
			
			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PontoAbastecimentoDTO pontoAbastecimento = new PontoAbastecimentoDTO();

				pontoAbastecimento.setId(new Integer(resultSet.getInt("ID")));
				pontoAbastecimento.setComunidade(resultSet.getString("COMUNIDADE"));
				pontoAbastecimento.setPopulacao(new Integer(resultSet.getInt("POPULACAO")));
				pontoAbastecimento.setDistancia(new Double(resultSet.getDouble("DISTANCIA")));
				pontoAbastecimento.setMomento(resultSet.getString("MOMENTOTRANSPORTE"));
				pontoAbastecimento.setCodcidade(new Integer(resultSet.getInt("CODCIDADE")));
				pontoAbastecimento.setNomeCidade(resultSet.getString("NOMECIDADE"));
				pontoAbastecimento.setGeoreferenciamento(resultSet.getString("GEOREFERENCIAMENTO"));
				pontoAbastecimento.setVolume(resultSet.getDouble("VOLUME"));
				pontoAbastecimento.setAsfalto(resultSet.getDouble("ASFALTO"));
				pontoAbastecimento.setTerra(resultSet.getDouble("TERRA"));
				pontoAbastecimento.setIndice(new Double(resultSet.getDouble("INDICE")));
				pontoAbastecimento.setApontador(resultSet.getString("APONTADOR"));
				pontoAbastecimento.setSubstituto(resultSet.getString("SUBSTITUTO"));
				pontoAbastecimento.setStatus(resultSet.getString("STATUS"));
				pontoAbastecimento.setPopulacao5L(new Integer(resultSet.getInt("POPULACAO_5L")));
				pontoAbastecimento.setPopulacaoDefault(new Integer(resultSet.getInt("POPULACAO_DEFAULT")));
				pontoAbastecimento.setLitrosDiario(new Integer(resultSet.getInt("LITROS_DIARIO")));
				pontoAbastecimento.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));
				pontoAbastecimento.setCodIndice(new Integer(resultSet.getInt("CODINDICE")));
				
				listaPontoAbastecimento.add(pontoAbastecimento);
			}

			return listaPontoAbastecimento;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<PontoAbastecimentoDTO> recuperaTodosPontoAbastecimentos() throws Exception {
		logger.info("Iniciando List<PontoAbastecimentoDTO> recuperaTodosPontoAbastecimentos()");
		
		List<PontoAbastecimentoDTO> listaPontoAbastecimento = new ArrayList<PontoAbastecimentoDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPontoAbastecimento = new StringBuffer();
			queryPontoAbastecimento.append(
					  "  SELECT p.*, i.INDICE as INDICE, i.DSC_INDICE as MOMENTOTRANSPORTE        "
					+ "  FROM pontoabastecimento p, indice i where p.COD_INDICE = i.id        "
			);

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PontoAbastecimentoDTO pontoAbastecimento = new PontoAbastecimentoDTO();

				pontoAbastecimento.setId(new Integer(resultSet.getInt("ID")));
				pontoAbastecimento.setComunidade(resultSet.getString("COMUNIDADE"));
				pontoAbastecimento.setPopulacao(new Integer(resultSet.getInt("POPULACAO")));
				pontoAbastecimento.setDistancia(new Double(resultSet.getDouble("DISTANCIA")));
				pontoAbastecimento.setMomento(resultSet.getString("MOMENTOTRANSPORTE"));
				pontoAbastecimento.setCodcidade(new Integer(resultSet.getInt("CODCIDADE")));
				pontoAbastecimento.setGeoreferenciamento(resultSet.getString("GEOREFERENCIAMENTO"));
				pontoAbastecimento.setVolume(resultSet.getDouble("VOLUME"));
				pontoAbastecimento.setAsfalto(resultSet.getDouble("ASFALTO"));
				pontoAbastecimento.setTerra(resultSet.getDouble("TERRA"));
				pontoAbastecimento.setIndice(new Double(resultSet.getDouble("INDICE")));
				pontoAbastecimento.setApontador(resultSet.getString("APONTADOR"));
				pontoAbastecimento.setSubstituto(resultSet.getString("SUBSTITUTO"));
				pontoAbastecimento.setStatus(resultSet.getString("STATUS"));
				pontoAbastecimento.setPopulacao5L(new Integer(resultSet.getInt("POPULACAO_5L")));
				pontoAbastecimento.setPopulacaoDefault(new Integer(resultSet.getInt("POPULACAO_DEFAULT")));
				pontoAbastecimento.setLitrosDiario(new Integer(resultSet.getInt("LITROS_DIARIO")));
				pontoAbastecimento.setCodGcda(new Integer(resultSet.getInt("CODGCDA")));

				listaPontoAbastecimento.add(pontoAbastecimento);
			}

			return listaPontoAbastecimento;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void inserePontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception{
		logger.info("Iniciando void inserePontoAbastecimento(String pontoAbastecimento" + "PontoAbastecimento: " + pontoAbastecimento);
		
		int id = 0;
		
		if(pontoAbastecimento.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pontoAbastecimento.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoAbastecimento = new StringBuffer();
			
			queryPontoAbastecimento.append(
					" INSERT INTO `pontoAbastecimento` (                 " +
							" `ID`,                " +
							" `COMUNIDADE`,		   " + 	
							" `POPULACAO`,         " +
							" `DISTANCIA`,         " +
							" `CODCIDADE`,         " +
							" `GEOREFERENCIAMENTO`," +
							" `VOLUME`,            " +
							" `ASFALTO`,           " +
							" `TERRA`,             " +
							" `APONTADOR`,         " +
							" `SUBSTITUTO`,        " +
							" `STATUS`,            " +
							" `POPULACAO_5L`,      " +
							" `POPULACAO_DEFAULT`, " +
							" `CODGCDA`,           " +
							" `LITROS_DIARIO`,     " +
							" `COD_INDICE`         " +
					")                                        " +
					" VALUES (                                " +
					" '" + id                                          +"', "+
					" '" + pontoAbastecimento.getComunidade().toUpperCase()          +"', "+
					" '" + pontoAbastecimento.getPopulacao()           +"', "+
					" '" + pontoAbastecimento.getDistancia()           +"', "+
					" '" + pontoAbastecimento.getCodcidade()           +"', "+
					" '" + pontoAbastecimento.getGeoreferenciamento()  +"', "+
					" '" + pontoAbastecimento.getVolume()              +"', "+
					" '" + pontoAbastecimento.getAsfalto()             +"', "+
					" '" + pontoAbastecimento.getTerra()               +"', "+
					" '" + pontoAbastecimento.getApontador().toUpperCase()            +"', "+
					" '" + pontoAbastecimento.getSubstituto().toUpperCase()           +"', "+
					" '" + pontoAbastecimento.getStatus()              +"', "+
					" '" + pontoAbastecimento.getPopulacao5L()         +"', "+
					" '" + pontoAbastecimento.getPopulacaoDefault()    +"', "+
					" '" + pontoAbastecimento.getCodGcda()             +"', "+
					" '" + pontoAbastecimento.getLitrosDiario()        +"', "+
					" '" + pontoAbastecimento.getCodIndice()           +"'  "+
					")                                        " 
					);
			
			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
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
	public void atualizaPontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) throws Exception{
		logger.info("Iniciando void atualizaPontoAbastecimento(String pontoAbastecimento" + "PontoAbastecimento: " + pontoAbastecimento);
		
		int id = 0;
		
		if(pontoAbastecimento.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pontoAbastecimento.getId();
		}
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoAbastecimento = new StringBuffer();

			queryPontoAbastecimento.append(
					" UPDATE pontoAbastecimento  SET     " +
					" `COMUNIDADE` =        " +  " '" + pontoAbastecimento.getComunidade().toUpperCase()           +"', "+
					" `POPULACAO` =         " +  " '" + pontoAbastecimento.getPopulacao()           +"', "+
					" `DISTANCIA` =         " +  " '" + pontoAbastecimento.getDistancia()           +"', "+
					" `CODCIDADE` =         " +  " '" + pontoAbastecimento.getCodcidade()           +"', "+
					" `GEOREFERENCIAMENTO` =" +  " '" + pontoAbastecimento.getGeoreferenciamento()  +"', "+
					" `VOLUME` =            " +  " '" + pontoAbastecimento.getVolume()              +"', "+
					" `ASFALTO` =           " +  " '" + pontoAbastecimento.getAsfalto()             +"', "+
					" `TERRA` =             " +  " '" + pontoAbastecimento.getTerra()               +"', "+
					" `APONTADOR` =         " +  " '" + pontoAbastecimento.getApontador().toUpperCase()            +"', "+
					" `SUBSTITUTO` =        " +  " '" + pontoAbastecimento.getSubstituto().toUpperCase()           +"', "+
					" `STATUS` =            " +  " '" + pontoAbastecimento.getStatus()              +"', "+
					" `POPULACAO_5L` =      " +  " '" + pontoAbastecimento.getPopulacao5L()         +"', "+
					" `POPULACAO_DEFAULT` = " +  " '" + pontoAbastecimento.getPopulacaoDefault()    +"', "+
					" `COD_INDICE`        = " +  " '" + pontoAbastecimento.getCodIndice()           +"', "+
					" `LITROS_DIARIO`     = " +  " '" + pontoAbastecimento.getCodIndice()           +"', "+
					" `CODGCDA` =           " +  " '" + pontoAbastecimento.getCodGcda()             +"'  "+
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
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
			StringBuffer queryPontoAbastecimento = new StringBuffer();

			queryPontoAbastecimento.append("select max(id) max_id from pontoAbastecimento");

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
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

	@Override
	public IndiceDTO recuperaIndicePorDescricao(String maisAsfalto)throws Exception {
		
		IndiceDTO indice = new IndiceDTO();
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoAbastecimento = new StringBuffer();

			queryPontoAbastecimento.append("SELECT * FROM indice where dsc_indice = '"+maisAsfalto+"'");

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();
			
			while ( resultSet.next() ){
				indice.setId(resultSet.getInt("ID"));
				indice.setDscIndice(resultSet.getString("DSC_INDICE"));
				indice.setIndice(resultSet.getDouble("INDICE"));
			}
			
		}catch(Exception ex){
		
			throw new Exception(ex);
				
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		return indice;
	}

	@Override
	public List<IndiceDTO> recuperaIndiceTodos() throws Exception {
			
		IndiceDTO indice = null;
		List<IndiceDTO> indices = new ArrayList();
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoAbastecimento = new StringBuffer();

			queryPontoAbastecimento.append("SELECT * FROM indice ");

			logger.info("Query: " + queryPontoAbastecimento.toString());
			statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
			resultSet = statement.executeQuery();
			
			while ( resultSet.next() ){
				
				indice = new IndiceDTO();
				
				indice.setId(resultSet.getInt("ID"));
				indice.setDscIndice(resultSet.getString("DSC_INDICE"));
				indice.setIndice(resultSet.getDouble("INDICE"));
				
				indices.add(indice);
			}
			
		}catch(Exception ex){
		
			throw new Exception(ex);
				
		}finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		return indices;
	}

	@Override
	public void atualizarIndices(IndiceDTO indice)throws Exception {
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPontoAbastecimento = new StringBuffer();

			queryPontoAbastecimento.append(
					" UPDATE indice  SET     " +
					" `INDICE` =        " +  " '" + indice.getIndice()         +"' "+
					" WHERE DSC_INDICE = '"+ indice.getDscIndice() +"'   "
					);
			
				logger.info("Query: " + queryPontoAbastecimento.toString());
				statement = conecction.prepareStatement(queryPontoAbastecimento.toString());
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
	
	
}
