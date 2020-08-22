package opp.dao;

import java.util.List;

import opp.dto.PontoColetaDTO;

public interface PontoColetaDAO {

	List<PontoColetaDTO> recuperaPontosColetaPorNome(String pontoColeta,String status) throws Exception;
	
	List<PontoColetaDTO> recuperaTodosPontosColeta() throws Exception;
	
	PontoColetaDTO recuperaPontoColetaPorNome(String pontoColeta) throws Exception;
	
	void atualizaPontoColeta(PontoColetaDTO pontoColeta) throws Exception;
	
	void inserePontoColeta(PontoColetaDTO pontoColeta) throws Exception;
	
	
}
