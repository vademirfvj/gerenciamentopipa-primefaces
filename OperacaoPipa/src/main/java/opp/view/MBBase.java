package opp.view;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import opp.bean.CadastrarPontoAbastecimentoBean;
import opp.dto.UsuarioDTO;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;


@ViewScoped
@ManagedBean(name = "mBBase")
@SessionScoped
public class MBBase implements Serializable {
	
	/**
	 * Atributos
	 */
	private static final long serialVersionUID = -6854387810533894261L;
	
	private static final Logger logger = Logger.getLogger(CadastrarPontoAbastecimentoBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	private int diasExpirarLicenca;
	
	private String nomeUsuario;
	
	
	private MenuModel menuModel;
	
	public void montaMenus() {

	      menuModel = new DefaultMenuModel();

	      UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);


	      DefaultSubMenu submenu = new DefaultSubMenu("Cadastro"); // Cria o submenu

//	      submenu.setStyle("width:250px");
	      
	      DefaultMenuItem item = new DefaultMenuItem(); //Cria o menuitem
	         //Cidade
	         item.setValue("Cidade");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_CIDADE");
//	         item.setAjax(false);
	         submenu.addElement(item); //adiciona o menuitem ao submenu
	      
	         //Usuário
	         if(usuarioNaSessao.getPostGrad() == Integer.valueOf(rpcProperties.buscaPropriedade("id.administrador"))){
	        	 
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Usuário");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_USUARIO");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu

	         }
	         
	         //Militar
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Militar");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_MILITAR");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu
	         
	         //Ponto de Abastecimento
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Ponto de Abastecimento");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_PA");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu
	         
	         //Ponto de Coleta de Água
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Ponto de Coleta de Água");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_PC");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu
	         
	         //Ponto de Coleta de Água
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Pipeiro");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_PIPEIRO");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu
	         
	         //Veículo
	         item = new DefaultMenuItem();//novo menuitem
	         item.setValue("Veículo");
	         item.setIcon("ui-icon-document");
	         item.setCommand("TELA_VEICULO");
//	         item.setAjax(false);
	         submenu.addElement(item);//adiciona o menuitem ao submenu


	      menuModel.addElement(submenu); //adiciona o submenu ao menu
	      
	      DefaultSubMenu secondSubmenu = new DefaultSubMenu("Pagamentos e Empenhos");
//	      secondSubmenu.setStyle("width:250px");
	      item = new DefaultMenuItem("Distribuição de Pontos de Abastecimento");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_DISTRIBUICAO");
//	      item.setAjax(false);
//	      item.setUpdate("messages");
	      secondSubmenu.addElement(item);
	      
	      item = new DefaultMenuItem();//novo menuitem
	      item.setValue("Proposta Passagens e Diárias");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_TESTE");
//	      item.setAjax(false);
	      secondSubmenu.addElement(item);//adiciona o menuitem ao submenu

	      item = new DefaultMenuItem();//novo menuitem
	      item.setValue("Pagamento de Autônomo");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_PAGAMENTO");
//	      item.setAjax(false);
	      secondSubmenu.addElement(item);//adiciona o menuitem ao submenu
	      
	      item = new DefaultMenuItem();//novo menuitem
	      item.setValue("Cadastro de Recurso");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_RECURSO");
//	      item.setAjax(false);
	      secondSubmenu.addElement(item);//adiciona o menuitem ao submenu
	      
	      item = new DefaultMenuItem();//novo menuitem
	      item.setValue("Cadastro de Empenho");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_EMPENHO");
//	      item.setAjax(false);
	      secondSubmenu.addElement(item);//adiciona o menuitem ao submenu
	      
	      item = new DefaultMenuItem();//novo menuitem
	      item.setValue("Cancelamento de Empenho");
	      item.setIcon("ui-icon-extlink");
	      item.setCommand("TELA_TESTE");
//	      item.setAjax(false);
	      secondSubmenu.addElement(item);//adiciona o menuitem ao submenu
	      menuModel.addElement(secondSubmenu); //adiciona o submenu ao menu
	      
	      DefaultSubMenu terceiroSubmenu = new DefaultSubMenu("Relatórios");
//	      secondSubmenu.setStyle("width:250px");
	      item = new DefaultMenuItem("Gerar");
	      item.setIcon("ui-icon-arrowthick-1-s");
	      item.setCommand("TELA_RELATORIOS");
//	      item.setAjax(false);
//	      item.setUpdate("messages");
	      terceiroSubmenu.addElement(item);
	      menuModel.addElement(terceiroSubmenu); //adiciona o submenu ao menu
	      
	      try {
				
				String dataLicenca = rpcProperties.buscaPropriedade("data.licenca.expirada");
				
				diasExpirarLicenca = Funcoes.recuperaDiasLicenca(dataLicenca);
				
				nomeUsuario = usuarioNaSessao.getNome();
				
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPontoAbastecimento",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Erro ao recuperar Cidadades.", null));
			}
	}
	
	public void load(){
		
			FacesContext fc = FacesContext.getCurrentInstance();
			
			NavigationHandler navHandler = fc.getApplication().getNavigationHandler();
			
			navHandler.handleNavigation(fc, null, Constantes.HOME);
		
	}
	
	public String home() {
		return Constantes.HOME;
	}
	
	public String logout(){
		
//		Funcoes.removeAttributeSession(Constantes.USUARIO_LOGADO);
		
		return Constantes.LOGOUT;
	}
	
	public MenuModel getMenuModel()
	   {
	      return menuModel;
	   }

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public int getDiasExpirarLicenca() {
		return diasExpirarLicenca;
	}

	public void setDiasExpirarLicenca(int diasExpirarLicenca) {
		this.diasExpirarLicenca = diasExpirarLicenca;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
}

