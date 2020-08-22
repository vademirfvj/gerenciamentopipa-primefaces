package opp.dao;

import java.util.List;

import opp.dto.PipeiroDTO;

public interface PipeiroDAO {

	PipeiroDTO recuperaPipeiroPorNome(String nome) throws Exception;
	
	List<PipeiroDTO> recuperaPipeirosPorNome(String nome,String status, int codcidadePesquisa) throws Exception;
	
	void atualizaPipeiro(PipeiroDTO pipeiro) throws Exception;
	
	void inserePipeiro(PipeiroDTO pipeiro) throws Exception;
	
	List<PipeiroDTO> recuperaTodosPipeiros() throws Exception;
	
	PipeiroDTO recuperaPipeiroPorId(int id) throws Exception;
	
}
