package opp.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import opp.connection.ConexaoMySQL;
import opp.dao.PipeiroDAO;
import opp.dto.PipeiroDTO;
import opp.util.Constantes;
import opp.util.Funcoes;

import org.apache.log4j.Logger;

public class PipeiroDAOImpl implements PipeiroDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3697777575781622467L;

	private static final Logger logger = Logger.getLogger(PipeiroDAOImpl.class);
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();

	
	@Override
	public PipeiroDTO recuperaPipeiroPorId(int id) throws Exception {
		logger.info("Iniciando PipeiroDTO recuperaPipeiroPorId(int id)" + "Id: " + id);
		PipeiroDTO pipeiro = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPipeiro = new StringBuffer();
			queryPipeiro.append(
					  "  SELECT *                         "
					+ "  FROM pipeiro c, veiculo v        "
					+ "	 WHERE c.id = '"+id+"'            "
					+ "  AND c.CODVEICULO = v.ID          "
			);

			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				pipeiro = new PipeiroDTO();

				pipeiro.setId(new Integer(resultSet.getInt("ID")));
				pipeiro.setNome(resultSet.getString("NOME"));
				pipeiro.setCodCidade(new Integer(resultSet.getInt("CODCIDADE")));    
				pipeiro.setCnh(new Integer(resultSet.getInt("CNH")));          
				pipeiro.setValidadeCnh(new Date(resultSet.getDate("VALIDADECNH").getTime()));  
				pipeiro.setIdentidade(new Integer(resultSet.getInt("IDENTIDADE")));   
				pipeiro.setOrgaoEmissor(resultSet.getString("ORGAOEMISSORUF")); 
				pipeiro.setNit(resultSet.getString("NIT"));          
				pipeiro.setCpf(resultSet.getString("CPF"));          
				pipeiro.setCodBanco(resultSet.getString("CODBANCO"));     
				pipeiro.setBanco(resultSet.getString("BANCO"));        
				pipeiro.setAgencia(resultSet.getString("AGENCIA"));      
				pipeiro.setConta(resultSet.getString("CONTA"));        
				pipeiro.setCodVeiculo(new Integer(resultSet.getInt("CODVEICULO")));   
				pipeiro.setSaldoSiaf(new Float(resultSet.getInt("SALDOSIAF")));    
				pipeiro.setRecursoAno(new Float(resultSet.getInt("RECURSOANO")));   
				pipeiro.setRestoPagar(new Float(resultSet.getInt("RESTOPAGAR")));   
				pipeiro.setStatus(resultSet.getString("STATUS"));
				
				pipeiro.getVeiculoDTO().setPlaca(resultSet.getString("PLACA"));
				pipeiro.getVeiculoDTO().setCapacidadePipa(new Float(resultSet.getInt("CAPACIDADEPIPA")));
				
				
			}
			
			return pipeiro;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public PipeiroDTO recuperaPipeiroPorNome(String nome) throws Exception {
		logger.info("Iniciando PipeiroDTO recuperaPipeiroPorNome(String nome)" + "Nome: " + nome);
		PipeiroDTO pipeiro = null;
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPipeiro = new StringBuffer();
			queryPipeiro.append(
					  "  SELECT *                         "
					+ "  FROM pipeiro c			          "
					+ "	 WHERE upper(c.nome) = upper('"+nome+"')   "
			);

			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				pipeiro = new PipeiroDTO();

				pipeiro.setId(new Integer(resultSet.getInt("ID")));
				pipeiro.setNome(resultSet.getString("NOME"));
				pipeiro.setCodCidade(new Integer(resultSet.getInt("CODCIDADE")));    
				pipeiro.setCnh(new Integer(resultSet.getInt("CNH")));          
				pipeiro.setValidadeCnh(new Date(resultSet.getDate("VALIDADECNH").getTime()));  
				pipeiro.setIdentidade(new Integer(resultSet.getInt("IDENTIDADE")));   
				pipeiro.setOrgaoEmissor(resultSet.getString("ORGAOEMISSORUF")); 
				pipeiro.setNit(resultSet.getString("NIT"));         
				pipeiro.setCpf(resultSet.getString("CPF"));          
				pipeiro.setCodBanco(resultSet.getString("CODBANCO"));     
				pipeiro.setBanco(resultSet.getString("BANCO"));        
				pipeiro.setAgencia(resultSet.getString("AGENCIA"));      
				pipeiro.setConta(resultSet.getString("CONTA"));        
				pipeiro.setCodVeiculo(new Integer(resultSet.getInt("CODVEICULO")));   
				pipeiro.setSaldoSiaf(new Float(resultSet.getInt("SALDOSIAF")));    
				pipeiro.setRecursoAno(new Float(resultSet.getInt("RECURSOANO")));   
				pipeiro.setRestoPagar(new Float(resultSet.getInt("RESTOPAGAR"))); 
				pipeiro.setStatus(resultSet.getString("STATUS"));
			}
			
			return pipeiro;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<PipeiroDTO> recuperaPipeirosPorNome(String nome, String status, int codcidadePesquisa) throws Exception {
		logger.info("Iniciando List<PipeiroDTO> recuperaPipeirosPorNome(String nome, String status)" 
					+ "Pipeiro: " + nome + "Status: " + status);
		
		List<PipeiroDTO> listaPipeiro = new ArrayList<PipeiroDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPipeiro = new StringBuffer();
			queryPipeiro.append(
					  "  SELECT c.*, cid.nome as NOMECIDADE                                 "
					+ "  FROM pipeiro c, cidade cid			                  "
					+ "	 WHERE c.nome LIKE '"+nome+"%'         "
					+ "  AND c.CODCIDADE = cid.ID             "
			);
			
			if(!Funcoes.isNuloOuVazioZero(codcidadePesquisa)){
				queryPipeiro.append(" AND c.CODCIDADE = "+codcidadePesquisa+" ");
			}
			

			if(!status.equalsIgnoreCase(Constantes.TODOS)){
				queryPipeiro.append("\n  AND c.status = '"+status+"'              ");
			}
			
			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PipeiroDTO pipeiro = new PipeiroDTO();

				pipeiro.setId(new Integer(resultSet.getInt("ID")));
				pipeiro.setNome(resultSet.getString("NOME"));
				pipeiro.setCodCidade(new Integer(resultSet.getInt("CODCIDADE")));   
				pipeiro.setNomeCidade(resultSet.getString("NOMECIDADE"));
				pipeiro.setCnh(new Integer(resultSet.getInt("CNH")));
				
				if(resultSet.getDate("VALIDADECNH") != null)
				pipeiro.setValidadeCnh(new Date(resultSet.getDate("VALIDADECNH").getTime()));
				
				pipeiro.setIdentidade(new Integer(resultSet.getInt("IDENTIDADE")));   
				pipeiro.setOrgaoEmissor(resultSet.getString("ORGAOEMISSORUF")); 
				pipeiro.setNit(resultSet.getString("NIT"));          
				pipeiro.setCpf(resultSet.getString("CPF"));          
				pipeiro.setCodBanco(resultSet.getString("CODBANCO"));     
				pipeiro.setBanco(resultSet.getString("BANCO"));        
				pipeiro.setAgencia(resultSet.getString("AGENCIA"));      
				pipeiro.setConta(resultSet.getString("CONTA"));        
				pipeiro.setCodVeiculo(new Integer(resultSet.getInt("CODVEICULO")));   
				pipeiro.setSaldoSiaf(new Float(resultSet.getFloat("SALDOSIAF")));    
				pipeiro.setRecursoAno(new Float(resultSet.getFloat("RECURSOANO")));   
				pipeiro.setRestoPagar(new Float(resultSet.getFloat("RESTOPAGAR"))); 
				pipeiro.setStatus(resultSet.getString("STATUS"));

				listaPipeiro.add(pipeiro);
			}

			return listaPipeiro;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public List<PipeiroDTO> recuperaTodosPipeiros() throws Exception {
		logger.info("Iniciando List<PipeiroDTO> recuperaTodosPipeiros()");
		
		List<PipeiroDTO> listaPipeiro = new ArrayList<PipeiroDTO>();
		
		try {

			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPipeiro = new StringBuffer();
			queryPipeiro.append(
					  "  SELECT *                                 "
					+ "  FROM pipeiro			                  "
			);

			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				PipeiroDTO pipeiro = new PipeiroDTO();

				pipeiro.setId(new Integer(resultSet.getInt("ID")));
				pipeiro.setNome(resultSet.getString("NOME"));
				pipeiro.setCodCidade(new Integer(resultSet.getInt("CODCIDADE")));    
				pipeiro.setCnh(new Integer(resultSet.getInt("CNH")));          
				pipeiro.setValidadeCnh(new Date(resultSet.getDate("VALIDADECNH").getTime()));  
				pipeiro.setIdentidade(new Integer(resultSet.getInt("IDENTIDADE")));   
				pipeiro.setOrgaoEmissor(resultSet.getString("ORGAOEMISSORUF")); 
				pipeiro.setNit(resultSet.getString("NIT"));          
				pipeiro.setCpf(resultSet.getString("CPF"));          
				pipeiro.setCodBanco(resultSet.getString("CODBANCO"));     
				pipeiro.setBanco(resultSet.getString("BANCO"));        
				pipeiro.setAgencia(resultSet.getString("AGENCIA"));      
				pipeiro.setConta(resultSet.getString("CONTA"));        
				pipeiro.setCodVeiculo(new Integer(resultSet.getInt("CODVEICULO")));   
				pipeiro.setSaldoSiaf(new Float(resultSet.getInt("SALDOSIAF")));    
				pipeiro.setRecursoAno(new Float(resultSet.getInt("RECURSOANO")));   
				pipeiro.setRestoPagar(new Float(resultSet.getInt("RESTOPAGAR"))); 
				pipeiro.setStatus(resultSet.getString("STATUS"));

				listaPipeiro.add(pipeiro);
			}

			return listaPipeiro;

		} finally {
			// Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			// Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
	
	@Override
	public void inserePipeiro(PipeiroDTO pipeiro) throws Exception{
		logger.info("Iniciando void inserePipeiro(String pipeiro" + "Pipeiro: " + pipeiro);
		
		int id = 0;
		
		if(pipeiro.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pipeiro.getId();
		}
		
		java.sql.Date data = null;
		
		if(!Funcoes.isNuloOuVazio(pipeiro.getValidadeCnh()))
		data = new java.sql.Date(pipeiro.getValidadeCnh().getTime());
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPipeiro = new StringBuffer();
			
			queryPipeiro.append(
					" INSERT INTO `pipeiro` (                 " +
					" `ID`,              " + 
					" `NOME`,            " + 
					" `CODCIDADE`,       " + 
					" `CNH`,             " + 
					" `VALIDADECNH`,     " + 
					" `IDENTIDADE`,      " + 
					" `ORGAOEMISSORUF`,  " + 
					" `NIT`,             " + 
					" `CPF`,             " + 
					" `CODBANCO`,        " + 
					" `BANCO`,           " + 
					" `AGENCIA`,         " + 
					" `CONTA`,           " + 
					" `CODVEICULO`,      " + 
					" `SALDOSIAF`,       " + 
					" `RECURSOANO`,      " + 
					" `RESTOPAGAR`,      " + 
					" `STATUS`           " + 
					")                                     "+
					" VALUES (                             "+
					" '" + id                         +"', "+
					" '" + pipeiro.getNome().toUpperCase()   +"', "+
					" '" + pipeiro.getCodCidade()     +"', "+
					" '" + pipeiro.getCnh()           +"', "+
					" " + data						  +", "+
					" '" + pipeiro.getIdentidade()    +"', "+
					" '" + pipeiro.getOrgaoEmissor()  +"', "+
					" '" + pipeiro.getNit()           +"', "+
					" '" + pipeiro.getCpf()           +"', "+
					" '" + pipeiro.getCodBanco()      +"', "+
					" '" + pipeiro.getBanco()         +"', "+
					" '" + pipeiro.getAgencia()       +"', "+
					" '" + pipeiro.getConta()         +"', "+
					" '" + pipeiro.getCodVeiculo()    +"', "+
					" '" + pipeiro.getSaldoSiaf()     +"', "+
					" '" + pipeiro.getRecursoAno()    +"', "+
					" '" + pipeiro.getRestoPagar()    +"', "+
					" '" + pipeiro.getStatus()        +"'  "+
					")                                 " 
					);
			
			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
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
	public void atualizaPipeiro(PipeiroDTO pipeiro) throws Exception{
		logger.info("Iniciando void atualizaPipeiro(String pipeiro" + "Pipeiro: " + pipeiro);
		
		int id = 0;
		
		if(pipeiro.getId() == 0){
			id = recuperaProximoId();
			
		}else{
			id = pipeiro.getId();
		}
		
		java.sql.Date data = new java.sql.Date(pipeiro.getValidadeCnh().getTime());
		
		try {
			conecction = ConexaoMySQL.getConnection();
			StringBuffer queryPipeiro = new StringBuffer();

			queryPipeiro.append(
					" UPDATE pipeiro  SET   " +
					" `NOME` =              " +  " '" + pipeiro.getNome()          +"', "+
					" `CODCIDADE` =         " +  " '" + pipeiro.getCodCidade()     +"', "+
					" `CNH` =               " +  " '" + pipeiro.getCnh()           +"', "+
					" `VALIDADECNH` =       " +  " '" + data                       +"', "+
					" `IDENTIDADE` =        " +  " '" + pipeiro.getIdentidade()    +"', "+
					" `ORGAOEMISSORUF` =    " +  " '" + pipeiro.getOrgaoEmissor()  +"', "+
					" `NIT` =               " +  " '" + pipeiro.getNit()           +"', "+
					" `CPF` =               " +  " '" + pipeiro.getCpf()           +"', "+
					" `CODBANCO` =          " +  " '" + pipeiro.getCodBanco()      +"', "+
					" `BANCO` =             " +  " '" + pipeiro.getBanco()         +"', "+
					" `AGENCIA` =           " +  " '" + pipeiro.getAgencia()       +"', "+
                    " `CONTA` =             " +  " '" + pipeiro.getConta()         +"', "+
                    " `CODVEICULO` =        " +  " '" + pipeiro.getCodVeiculo()    +"', "+
					" `SALDOSIAF` =         " +  " '" + pipeiro.getSaldoSiaf()     +"', "+
					" `RECURSOANO` =        " +  " '" + pipeiro.getRecursoAno()    +"', "+
					" `RESTOPAGAR` =        " +  " '" + pipeiro.getRestoPagar()    +"', "+
					" `STATUS` =            " +  " '" + pipeiro.getStatus()        +"'  "+
					" WHERE ID = '"+ id +"'   "
					);
			
			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
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
			StringBuffer queryPipeiro = new StringBuffer();

			queryPipeiro.append("select max(id) max_id from pipeiro");

			statement = conecction.prepareStatement(queryPipeiro.toString());
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