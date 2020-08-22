package opp.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name="MenuBean")
@ViewScoped
public class MenuBean implements Serializable{

private static final long serialVersionUID = 1L;
private MenuModel menuModel;

  @PostConstruct
public void init()
{
      menuModel = new DefaultMenuModel();


      DefaultSubMenu submenu = new DefaultSubMenu("Cadastro"); // Cria o submenu

      submenu.setStyle("width:250px");
      
      DefaultMenuItem item = new DefaultMenuItem(); //Cria o menuitem
         //Pipeiro
         item.setValue("Pipeira");
         item.setIcon("ui-icon-document");
         item.setCommand("#{MyMBean.metodo}");
         submenu.addElement(item); //adiciona o menuitem ao submenu
      
         //Ponto de Abastecimento
         item = new DefaultMenuItem();//novo menuitem
         item.setValue("Ponto de Abastecimento");
         item.setIcon("ui-icon-document");
         item.setCommand("#{MyMBean.metodo}");
         submenu.addElement(item);//adiciona o menuitem ao submenu


      menuModel.addElement(submenu); //adiciona o submenu ao menu
      
      DefaultSubMenu secondSubmenu = new DefaultSubMenu("Distribuição");
      secondSubmenu.setStyle("width:250px");
      item = new DefaultMenuItem("Inserir");
      item.setIcon("ui-icon-extlink");
      item.setCommand("TELA_DISTRIBUICAO");
//      item.setUpdate("messages");
      secondSubmenu.addElement(item);
      
      menuModel.addElement(secondSubmenu); //adiciona o submenu ao menu
      
}

  public MenuModel getMenuModel()
   {
      return menuModel;
   }

}