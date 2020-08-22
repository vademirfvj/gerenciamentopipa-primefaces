package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.PipeiroDAO;
import opp.dto.PipeiroDTO;
import opp.impl.PipeiroDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pipeiroService")
public class PipeiroService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6023403366935407143L;
	
	@Autowired
	private PipeiroDAO pipeiroDAO = new PipeiroDAOImpl();
	
	public void atualizaPipeiro(PipeiroDTO pipeiro) throws Exception{
		pipeiroDAO.atualizaPipeiro(pipeiro);
	}
	
	public void inserePipeiro(PipeiroDTO pipeiro) throws Exception{
		pipeiroDAO.inserePipeiro(pipeiro);
	}
 
	public PipeiroDTO recuperaPipeiroPorNome(String nome) throws Exception{
		return pipeiroDAO.recuperaPipeiroPorNome(nome);
	}
	
	public List<PipeiroDTO> recuperaTodosPipeiros() throws Exception{
		return pipeiroDAO.recuperaTodosPipeiros();
	}
	
	public List<PipeiroDTO> recuperaPipeirosPorNome(String nome, String status, int codcidadePesquisa) throws Exception{
		return pipeiroDAO.recuperaPipeirosPorNome(nome,status,codcidadePesquisa);
	}
	
	public PipeiroDTO recuperaPipeiroPorId(int id) throws Exception{
		return pipeiroDAO.recuperaPipeiroPorId(id);
		
	}
	
}
