package eml.studio.client.mvp.view;

import eml.studio.client.controller.DiagramController;
import eml.studio.client.ui.property.PropertyTable;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generator is the base class for all custom pages
 * In the onModuleLoad custom page,
 * contains some of the basic functions of the page
 * TODO: 把非UI相关的代码移到present里去
 */
public abstract class Generator {

  protected Panel propPanel = new AbsolutePanel();

  /** Menu panel show in the head */
  protected HTMLPanel headerHtmlPanel = null;

  private DiagramController controller;


  public DiagramController getController() {
    return controller;
  }

  public void setController(DiagramController controller) {
    this.controller = controller;
  }

  public abstract Widget createMainLayout();

  /**
   * Tet the program attributes information panel
   *
   * @param pt The attribute table corresponding to the algorithm module
   */
  public void setPropTable(PropertyTable pt) {
    propPanel.clear();
    propPanel.add(pt);
  }
  
  public void clearPropTable(){
    propPanel.clear();
  }
}
