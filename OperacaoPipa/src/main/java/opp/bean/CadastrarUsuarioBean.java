package opp.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import opp.dto.AssinaturasDTO;
import opp.dto.PostoGraduDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.UsuarioService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
@ViewScoped
@ManagedBean(name = "cadastrarUsuarioBean")
@SessionScoped
public class CadastrarUsuarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 873142096184188266L;

	private static final Logger logger = Logger.getLogger(CadastrarUsuarioBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos de Cadastro */
	private UsuarioDTO usuario;
	private int id;
	private String nome;
	private int postGrad;
	private String email;
	private String login;
	private String senha;
	private String status;

	private List<UsuarioDTO> listaUsuario;
	
	private List<PostoGraduDTO> listaPosto;
	
	/* Atributos de Pesquisa */
	private String pesquisaUsuario;
	private String pesquisaStatusUsuario;
	private List<UsuarioDTO> listaPesquisa;
	
	/* Atributos de Edição */
	private UsuarioDTO usuarioEditavel;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Usuario.");
		
		usuarioService = new UsuarioService();
		usuarioEditavel = new UsuarioDTO();
		service = new PagamentoPipeiroSevice();

		
		try {
			
			listaPosto = usuarioService.recuperaTodasPostos();
			
			UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
			
	         if(usuarioNaSessao.getPostGrad() != Integer.valueOf(rpcProperties.buscaPropriedade("id.administrador"))){

				List<PostoGraduDTO> listaPostoTeste = new ArrayList();
				listaPostoTeste.addAll(listaPosto);
				
				PostoGraduDTO pg;
				for (int i = 0; i < listaPostoTeste.size(); i++) {
					pg = listaPostoTeste.get(i);
					
					if(pg.getId() == 5){
						listaPosto.remove(pg);
					}
				}
			
	         }
			
		} catch (Exception ex) {
			logger.error(ex);
			FacesContext.getCurrentInstance().addMessage(    
	                "msgCadastrarPontoAbastecimento",    
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
	                        "Erro ao recuperar Post./Grad.", null));
		}
		
	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		
		logger.info("Inicio do método salvar()");
		
		if(campoEhValido(id,nome,postGrad,email,login,senha,status)){
			
			setStatus("ATIVO");
			
			//Monta objeto
			usuario = new UsuarioDTO(
					nome,postGrad,email,login,senha,status
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(usuario.getNome().trim());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarUsuario",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Usuario
				boolean erro = false;
				
				try{
					usuarioService.insereUsuario(usuario);
					
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_USUARIO,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarUsuario",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarUsuario",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarUsuario",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(nome)){
				msgErro.append("- Nome ");
			}
			if(Funcoes.isNuloOuVazioZero(postGrad)){
				msgErro.append("- Post/Grad ");
			}
			if(Funcoes.isNuloOuVazio(login)){
				msgErro.append("- Login ");
			}
			if(Funcoes.isNuloOuVazio(senha)){
				msgErro.append("- Senha ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}
		
		logger.info("Fim do método salvar()");	
	}

	public boolean verificaSeExiste(String usuario) throws Exception{
		boolean existe = false;
		
		UsuarioDTO usuarioProcurada = usuarioService.recuperaUsuarioPorNome(usuario);
		if(null != usuarioProcurada && usuarioProcurada.getNome().equalsIgnoreCase(usuario)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		usuarioEditavel = new UsuarioDTO();
	}
	
	public void editar(){
		
		logger.info("Inicio do método editar()");
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido( usuarioEditavel.getId(),usuarioEditavel.getNome(),usuarioEditavel.getPostGrad(),usuarioEditavel.getEmail(),
				usuarioEditavel.getLogin(),usuarioEditavel.getSenha(),usuarioEditavel.getStatus())){
			try{
				usuarioService.atualizaUsuario(usuarioEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_USUARIO,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
				
			}catch(Exception ex){
				erro = true;
				msg = "Não foi possível editar o registro.";
				logger.error(ex);
			}
		}
	
		if(!erro){
			limpaEditavel();
			pesquisar();
		}else{
			logger.error("Não foi possível editar o registro.");
			FacesContext.getCurrentInstance().addMessage("msgCadastrarUsuario",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método editar()");	
	}
	
	public void enviaEditavel(UsuarioDTO usuario){
		usuarioEditavel = new UsuarioDTO(
				usuario.getId(),usuario.getNome(),usuario.getPostGrad(),usuario.getEmail(),
				usuario.getLogin(),usuario.getSenha(),usuario.getStatus()
				);
	}
	
	public void pesquisar(){
		
		logger.info("Inicio do método pesquisar()");
		
		String msg = null;
		//Pesquisa Usuario
		try{
			listaPesquisa = usuarioService.recuperaUsuariosPorNome(pesquisaUsuario,pesquisaStatusUsuario);
		
			if(null == listaPesquisa){
				msg = "Não foi possível pesquisar o registro.";
	
			}else if(listaPesquisa.isEmpty()){
				msg = "Nenhum registro foi encontrado.";
			}
		
		}catch(Exception ex){
			logger.error(ex);
			msg = "Não foi possível pesquisar o registro.";
		}
		
		if(null != msg){
			logger.error(msg);
			FacesContext.getCurrentInstance().addMessage("msgCadastrarUsuario",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		logger.info("Fim do método pesquisar()");	
	}
	
	private boolean campoEhValido(int id,String nome,int postGrad,
				String email,String login,String senha,String status){
		
		if(Funcoes.isNuloOuVazio(nome) || Funcoes.isNuloOuVazioZero(postGrad) ||
				Funcoes.isNuloOuVazio(login) || Funcoes.isNuloOuVazio(senha)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){

		id = 0;
		nome="";
		postGrad=0;
		email="";
		login="";
		senha="";
		status = "";
		
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		logger.info("Inicio do método imprimirRelatorio()");
		
		boolean erro = false;
		String msg = "";
		
		try {
			
			
			List<UsuarioDTO> lista = new ArrayList<UsuarioDTO>();
			lista = usuarioService.recuperaTodosUsuarios();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_usuario.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioUsuarios_"+timeStamp+".pdf";
			// exportacao do relatorio para outro formato, no caso PDF
			JasperExportManager.exportReportToPdfFile(print,
					caminhoImpressao+arquivo);

		} catch (Exception e) {
			erro = true;
			logger.error(e);
		}
		
		if(erro){
			msg = "Erro ao gerar relatório.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}else{
			msg = "Relatório gerado com sucesso.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método imprimirRelatorio()");
	}
	
	public void onRowEdit(RowEditEvent event) {
		logger.info("Inicio do método onRowEdit()");
		 
		 try {
			
			 PostoGraduDTO posto = (PostoGraduDTO) event.getObject();
			 
			 usuarioService.atualizarAssinaturaDistrPieiro(posto);
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao atualizar Posto/Grad. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao atualizar Posto/Grad. ["
	    				+e.toString()+ " ]");
			}
		 logger.info("Fim do método onRowEdit()");	 
	 }
	
	/**
	 * Getters and Setters
	 */
	public Status[] getStatusEnum() {
		return Status.values();
	}

	public List<UsuarioDTO> getListaUsuario() {
		if(null == listaUsuario){
			listaUsuario = new ArrayList<UsuarioDTO>();
		}
		
		return listaUsuario;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPostGrad() {
		return postGrad;
	}

	public void setPostGrad(int postGrad) {
		this.postGrad = postGrad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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

	public String getPesquisaUsuario() {
		return pesquisaUsuario;
	}

	public void setPesquisaUsuario(String pesquisaUsuario) {
		this.pesquisaUsuario = pesquisaUsuario;
	}

	public String getPesquisaStatusUsuario() {
		return pesquisaStatusUsuario;
	}

	public void setPesquisaStatusUsuario(String pesquisaStatusUsuario) {
		this.pesquisaStatusUsuario = pesquisaStatusUsuario;
	}

	public List<UsuarioDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<UsuarioDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public UsuarioDTO getUsuarioEditavel() {
		return usuarioEditavel;
	}

	public void setUsuarioEditavel(UsuarioDTO usuarioEditavel) {
		this.usuarioEditavel = usuarioEditavel;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setListaUsuario(List<UsuarioDTO> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public List<PostoGraduDTO> getListaPosto() {
		return listaPosto;
	}

	public void setListaPosto(List<PostoGraduDTO> listaPosto) {
		this.listaPosto = listaPosto;
	}

	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}
}
