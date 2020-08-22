package opp.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.UsuarioService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
@ViewScoped
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 873142096184188266L;

	private static final Logger logger = Logger.getLogger(LoginBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos */
	private UsuarioDTO usuarioDTO;
	private String usuario;
	private String senha;
	private String status;
	
	private int diasExpirarLicenca;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@PostConstruct
	public void loadPage() {
		
		usuarioService = new UsuarioService();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg').show();");		
	}
	
	public void login(){
		
		try {
			
			
			if(Funcoes.isNuloOuVazio(usuario)){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "O campo usuário está vazio."));
	    		return;
			}
			
			if(Funcoes.isNuloOuVazio(senha)){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "O campo senha está vazio."));
	    		return;
			}
			
			usuarioDTO = usuarioService.recuperaUsuarioPorLogin(usuario);
			
			if(Funcoes.isNuloOuVazio(usuarioDTO)){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Usuário Inexistente."));
	    		return;
			}
			
			if(usuarioDTO.getStatus().equalsIgnoreCase("INATIVO")){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Usuário Inativo."));
	    		return;
			}
			
			if(!usuarioDTO.getSenha().equals(senha)){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Senha incorreta."));
	    		return;
			}
			
			String dataLicenca = rpcProperties.buscaPropriedade("data.licenca.expirada");
			
			diasExpirarLicenca = Funcoes.recuperaDiasLicenca(dataLicenca);
			
			if(diasExpirarLicenca <= 0){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "A licença expirou, entre em contato com a NEWTEC."));
	    		return;
			}
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
			
			Funcoes.setAttributeSession(Constantes.USUARIO_LOGADO, usuarioDTO);

		} catch (Exception e) {
			System.out.println("Erro ao recuperar usuário: "+e.toString());
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao recuperar usuário. ["
    				+e.toString()+ " ]"));
		}
		
	}

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public int getDiasExpirarLicenca() {
		return diasExpirarLicenca;
	}

	public void setDiasExpirarLicenca(int diasExpirarLicenca) {
		this.diasExpirarLicenca = diasExpirarLicenca;
	}
	
}
