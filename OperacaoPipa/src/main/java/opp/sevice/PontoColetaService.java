package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.PontoColetaDAO;
import opp.dto.PontoColetaDTO;
import opp.impl.PontoColetaDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pontoColetaService")
public class PontoColetaService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7402998549710190395L;
	
	@Autowired
	private PontoColetaDAO pontoColetaDAO = new PontoColetaDAOImpl();
	
	public void atualizaPontoColeta(PontoColetaDTO pontoColeta) throws Exception{
		pontoColetaDAO.atualizaPontoColeta(pontoColeta);
	}
	
	public void inserePontoColeta(PontoColetaDTO pontoColeta) throws Exception{
		pontoColetaDAO.inserePontoColeta(pontoColeta);
	}
 
	public PontoColetaDTO recuperaPontoColetaPorNome(String nome) throws Exception{
		return pontoColetaDAO.recuperaPontoColetaPorNome(nome);
	}

	public List<PontoColetaDTO> recuperaTodosPontosColeta() throws Exception{
		return pontoColetaDAO.recuperaTodosPontosColeta();
	}
	
	public List<PontoColetaDTO> recuperaPontosColetaPorNome(String nome, String status) throws Exception{
		return pontoColetaDAO.recuperaPontosColetaPorNome(nome,status);
	}
}
