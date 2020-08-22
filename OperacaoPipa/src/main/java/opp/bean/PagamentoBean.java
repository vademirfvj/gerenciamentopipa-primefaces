package opp.bean;
 
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import opp.sevice.PagamentoService;
 
@ViewScoped
@ManagedBean(name = "pagamentoBean", eager = true)
@SessionScoped
public class PagamentoBean implements Serializable {
	
	private String remessa;
 	
    
    @ManagedProperty("#{pagamentoService}")
    private PagamentoService service;
    
    
    @PostConstruct
    public void init() {
    	
    }


	public String getRemessa() {
		return remessa;
	}


	public void setRemessa(String remessa) {
		this.remessa = remessa;
	}


	public PagamentoService getService() {
		return service;
	}


	public void setService(PagamentoService service) {
		this.service = service;
	}
    
}