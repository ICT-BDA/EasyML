package eml.studio.client.ui.widget.shape;


/**
 * MouseShape at Point
 */
public class MouseShape extends Point {

  private int height = 1;
  private int width = 1;
  Point mousePoint;

  public MouseShape(Point mousePoint) {
    super(mousePoint.getLeft(), mousePoint.getTop());
    this.mousePoint = mousePoint;
  }

  @Override
  public int getLeft() {
    return mousePoint.getLeft();
  }

  @Override
  public int getTop() {
    return mousePoint.getTop();
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

}