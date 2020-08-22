package opp.sevice;

import java.io.Serializable;
import java.util.List;

import opp.dao.CidadeDAO;
import opp.dto.CidadeDTO;
import opp.impl.CidadeDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("pagamentoService")
public class PagamentoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7402998549710190395L;
	
	@Autowired
	private CidadeDAO cidadeDAO = new CidadeDAOImpl();
	

}
