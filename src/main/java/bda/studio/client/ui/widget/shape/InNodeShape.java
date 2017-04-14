package bda.studio.client.ui.widget.shape;

import bda.studio.client.ui.connection.Connection;
import bda.studio.client.ui.widget.BaseWidget;
import com.orange.links.client.shapes.DrawableSet;

/**
 * InNodeShape class
 */
public class InNodeShape extends NodeShape {

  private Connection currentConnection = null;
  public InNodeShape(BaseWidget parent, int portId) {
    super(parent, portId);
  }

  public InNodeShape(BaseWidget parent, int portId, double leftRelative,
      double topRelative) {
    super(parent, portId, leftRelative, topRelative);
  }

  @Override
  public DrawableSet<Connection> getConnections() {
    return conns;
  }

  @Override
  public boolean addConnection(Connection connection) {
    currentConnection = connection;
    return super.addConnection(connection);
  }

  @Override
  public boolean removeConnection(Connection connection) {
    if (connection == currentConnection) {
      currentConnection = null;
    }
    return super.removeConnection(connection);
  }

  @Override
  public void onConnectionStart() {
  }

  @Override
  public void onConnectionEnd(Connection conn) {
    if (this.currentConnection != null) {
      conns.remove(currentConnection);
      NodeShape shape = (NodeShape) currentConnection.getStartShape();
      shape.removeConnection(currentConnection);
      super.deleteConnectionFromController(currentConnection);
      currentConnection = null;
    }
    addConnection(conn);
  }

  public Connection getCurrentConnection() {
    return currentConnection;
  }

  @Override
  public void onConnectionCancel() {

  }


  
  public InNodeShape clone(){
    InNodeShape node  = new InNodeShape(this.getWidget(),this.getPortId());
    return node ;
  }
}
