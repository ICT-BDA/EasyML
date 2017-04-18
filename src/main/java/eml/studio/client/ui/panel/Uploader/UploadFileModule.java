package eml.studio.client.ui.panel.Uploader;


import eml.studio.client.util.Constants;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;



import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class UploadFileModule extends FileUploader {
  protected Program upLoadProgram = new Program();
  protected Dataset upLoadDataset = new Dataset();

  final Button startbt = new Button(Constants.studioUIMsg.submit());

  public UploadFileModule() {
    super();
    startbt.setStyleName("bda-fileupload-btn");
    this.setBorderWidth(1);
    HorizontalPanel horizontalPanel = (HorizontalPanel) this.getWidget(0);
    horizontalPanel.add(startbt);
    this.getElement().getStyle().setBorderColor("#cccccc");
    this.setWidth("100%");
    this.setStyleName("bda-fileupload-bottom-vpanel");

  }

  //get and set method
  public Program getUpLoadProgram() {
    return upLoadProgram;
  }

  public void setUpLoadProgram(Program upLoadProgram) {
    this.upLoadProgram = upLoadProgram;
  }

  public Dataset getUpLoadDataset() {
    return upLoadDataset;
  }

  public void setUpLoadDataset(Dataset upLoadDataset) {
    this.upLoadDataset = upLoadDataset;
  }

  public Button getStartbt() {
    return startbt;
  }
}
