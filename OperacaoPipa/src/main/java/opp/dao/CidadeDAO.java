package opp.dao;

import java.util.List;

import opp.dto.CidadeDTO;

public interface CidadeDAO {

	List<CidadeDTO> recuperaCidadesPorNome(String cidade,String status) throws Exception;
	
	CidadeDTO recuperaCidadePorNome(String cidade) throws Exception;
	
	void atualizaCidade(CidadeDTO cidade) throws Exception;
	
	void insereCidade(CidadeDTO cidade) throws Exception;
	
	List<CidadeDTO> recuperaTodasCidades() throws Exception;
	List<CidadeDTO> recuperaTodasCidadesAtivas() throws Exception;
	CidadeDTO recuperaCidadePorId(int id) throws Exception;
	
}
