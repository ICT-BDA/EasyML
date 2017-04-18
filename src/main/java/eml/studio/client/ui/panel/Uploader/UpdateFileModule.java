package eml.studio.client.ui.panel.Uploader;

import eml.studio.client.ui.binding.DatasetBinder;
import eml.studio.client.ui.binding.ProgramBinder;
import eml.studio.shared.model.Dataset;
import eml.studio.shared.model.Program;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class UpdateFileModule extends FileUploader {

  final Button startbt = new Button("提交");
  protected Program upLoadProgram = new Program();
  protected Dataset upLoadDataset = new Dataset();
  public static ProgramBinder programBinder = GWT.create(ProgramBinder.class);
  public static DatasetBinder datsetBinder = GWT.create(DatasetBinder.class);

  public UpdateFileModule() {
    super();
    startbt.setStyleName("bda-fileupload-btn");
    this.setBorderWidth(1);
    HorizontalPanel horizontalPanel = (HorizontalPanel) this.getWidget(0);
    horizontalPanel.add(startbt);
    this.getElement().getStyle().setBorderColor("#cccccc");
    this.setWidth("100%");
    this.setStyleName("bda-fileupload-bottom-vpanel");
  }

  public Button getStartbt() {
    return startbt;
  }

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

  public static ProgramBinder getProgramBinder() {
    return programBinder;
  }

  public static void setProgramBinder(ProgramBinder programBinder) {
    UpdateFileModule.programBinder = programBinder;
  }

  public static DatasetBinder getDatsetBinder() {
    return datsetBinder;
  }

  public static void setDatsetBinder(DatasetBinder datsetBinder) {
    UpdateFileModule.datsetBinder = datsetBinder;
  }
}
