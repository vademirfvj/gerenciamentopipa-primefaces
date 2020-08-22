package opp.dao;

import java.util.List;

import opp.dto.MilitarDTO;

public interface MilitarDAO {
	
	List<MilitarDTO> recuperaMilitarsPorNome(String militar,String status) throws Exception;
	
	
	
	MilitarDTO recuperaMilitarPorNome(String militar) throws Exception;
	
	MilitarDTO recuperaMilitarPorCpf(String cpf) throws Exception;
	
	
	void atualizaMilitar(MilitarDTO militar) throws Exception;
	
	void insereMilitar(MilitarDTO militar) throws Exception;
	
	List<MilitarDTO> recuperaTodosMilitares() throws Exception;
	
	MilitarDTO recuperaMilitarPorId(int id) throws Exception;
}
