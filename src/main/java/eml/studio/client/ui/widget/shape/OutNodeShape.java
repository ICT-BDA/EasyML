package eml.studio.client.ui.widget.shape;

import eml.studio.client.ui.connection.Connection;
import eml.studio.client.ui.widget.BaseWidget;
import eml.studio.client.ui.widget.dataset.DatasetWidget;
import eml.studio.client.ui.widget.program.ProgramWidget;

import com.orange.links.client.shapes.DrawableSet;

/**
 * Out node shape in widget
 */
public class OutNodeShape extends NodeShape {

  private String fileId;
  public OutNodeShape(BaseWidget parent, int portId) {
    super(parent, portId);
  }

  public OutNodeShape(BaseWidget parent, int portId, double leftRelative,
      double topRelative) {
    super(parent, portId, leftRelative, topRelative);
  }


  @Override
  public DrawableSet<Connection> getConnections() {
    return conns;
  }

  @Override
  public void onConnectionStart() {

  }

  @Override
  public void onConnectionEnd(Connection conn) {
    addConnection(conn);
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileid) {
    this.fileId = fileid;
  }

  @Override
  public void onConnectionCancel() {
    // TODO Auto-generated method stub

  }

  /**
   * get file path for workflow<br/>
   * will be called when generating oozie workflow
   */
  public String getWorkflowPath(){
    if( this.getWidget() instanceof DatasetWidget ){
      DatasetWidget dw = (DatasetWidget) this.getWidget();
      return dw.getDataset().getPath();
    }else{
      ProgramWidget pw = (ProgramWidget) this.getWidget();
      return pw.getWorkPath() + fileId;
    }
    
  }
  
  /**
   * get absolute file path on hdfs
   */
  public String getAbsolutePath(){
    if( this.getWidget() instanceof DatasetWidget ){
      DatasetWidget dw = (DatasetWidget) this.getWidget();
      return dw.getDataset().getPath();
    }else{
      ProgramWidget pw = (ProgramWidget) this.getWidget();
      return pw.getAction().getAppPath() + "/" + pw.getAction().getName() + "/" + fileId;
    }
  }

  public OutNodeShape clone(){
    OutNodeShape node = new OutNodeShape(this.getWidget(),this.getPortId());
    return node;
  }
}
