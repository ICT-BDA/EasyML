/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel.component;

import eml.studio.client.util.Constants;
import eml.studio.shared.util.DatasetType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

import java.util.ArrayList;

/**
 * Generate command line panel
 */
public class DescCmdPanel extends VerticalPanel implements DescWidget {
	ArrayList<ListBox> parameterListType = new ArrayList<ListBox>();
	ArrayList<ListBox> parameterTypeDetail = new ArrayList<ListBox>();
	ArrayList<TextBox> parameterName = new ArrayList<TextBox>();
	ArrayList<TextBox> parameterValue = new ArrayList<TextBox>();
	ArrayList<TextBox> parameterDetail = new ArrayList<TextBox>();
	ArrayList<CheckBox> parameterCheckDelete = new ArrayList<CheckBox>();
	String str = "";
	FlexTable grid = new FlexTable();
	FlexCellFormatter cellFormatter = grid.getFlexCellFormatter();
	Label lb_filename = new Label(Constants.studioUIMsg.commandLine());

	public DescCmdPanel(TextArea cmd) {
		init(cmd);
		this.setStyleName("bda-desccmdpanel");
	}

	public void init(final TextArea cmd) {
		str += cmd.getText().toString();
		lb_filename.setStyleName("desccmdpanel-Label");
		final TextBox tb_filename = new TextBox();
		tb_filename.setStyleName("desccmdpanel-cmd-textbox");
		VerticalPanel dialogpanel = new VerticalPanel();
		Button add = new Button(Constants.studioUIMsg.add());
		Button delete = new Button(Constants.studioUIMsg.remove());
		Button create = new Button(Constants.studioUIMsg.generate());
		add.setStyleName("desccmdpanel-add-delete-Button");
		delete.setStyleName("desccmdpanel-add-delete-Button");
		create.setStyleName("desccmdpanel-create-Button");
		add.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final int count = grid.getRowCount();
				grid.insertRow(count - 2);
				parameterName.add(new TextBox());// Parameter name
				parameterName.get(count - 4).setStyleName("desccmdpanel-other-textbox");

				parameterTypeDetail.add(new ListBox());//参数类型
				parameterTypeDetail.get(count - 4).setStyleName("desccmdpanel-ListBox");
				parameterTypeDetail.get(count - 4).addItem(" ");
				parameterTypeDetail.get(count - 4).addItem(DatasetType.GENERAL.getDesc());
				parameterTypeDetail.get(count - 4).addItem(DatasetType.CSV.getDesc());
				parameterTypeDetail.get(count - 4).addItem(DatasetType.TSV.getDesc());
				parameterTypeDetail.get(count - 4).addItem(DatasetType.JSON.getDesc());
				parameterTypeDetail.get(count - 4).addItem(DatasetType.DIRECTORY.getDesc());

				parameterListType.add(new ListBox());// Type description
				parameterListType.get(count - 4).setStyleName("desccmdpanel-ListBox");
				parameterListType.add(new ListBox());
				parameterListType.get(count - 4).addItem("in");
				parameterListType.get(count - 4).addItem("out");
				parameterListType.get(count - 4).addItem("int");
				parameterListType.get(count - 4).addItem("double");
				parameterListType.get(count - 4).addItem("string");
				parameterListType.get(count - 4).addItem("bool");
				parameterListType.get(count - 4).addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						String type = parameterListType.get(count - 4).getValue(
								parameterListType.get(count - 4).getSelectedIndex());
						// String Type=type.substring(0, 1).toUpperCase()+type.substring(1);
						if (type.equals("int") || type.equals("double")
								|| type.equals("string") || type.equals("bool")) {
							parameterTypeDetail.get(count - 4).setItemSelected(0, true);
							parameterTypeDetail.get(count - 4).setEnabled(false);
							parameterValue.get(count - 4).setEnabled(true);
						} else {
							parameterValue.get(count - 4).setEnabled(false);
							parameterTypeDetail.get(count - 4).setEnabled(true);
							parameterTypeDetail.get(count - 4).setItemSelected(1, true);
						}
					}
				});

				parameterValue.add(new TextBox());
				parameterValue.get(count - 4)
				.setStyleName("desccmdpanel-other-textbox");
				parameterValue.get(count - 4).setEnabled(false);

				parameterDetail.add(new TextBox());// Parameter Description
				parameterDetail.get(count - 4).setStyleName(
						"desccmdpanel-detail-textbox");
				parameterCheckDelete.add(new CheckBox());// CheckDelete

				grid.setWidget(count - 2, 0, parameterListType.get(count - 4));
				grid.setWidget(count - 2, 1, parameterTypeDetail.get(count - 4));
				grid.setWidget(count - 2, 2, parameterName.get(count - 4));
				grid.setWidget(count - 2, 3, parameterValue.get(count - 4));
				grid.setWidget(count - 2, 4, parameterDetail.get(count - 4));
				grid.setWidget(count - 2, 5, parameterCheckDelete.get(count - 4));
			}
		});
		delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int i = 0;
				for (i = 0; i < parameterCheckDelete.size();) {
					if (parameterCheckDelete.get(i).getValue().toString().equals("true")) {
						grid.removeRow(i + 2);
						parameterName.remove(i);
						parameterListType.remove(i);
						parameterTypeDetail.remove(i);
						parameterValue.remove(i);
						parameterDetail.remove(i);
						parameterCheckDelete.remove(i);
					} else {
						i++;
					}
				}
			}

		});

		create.addClickHandler(new ClickHandler() {// TODO:
			@Override
			public void onClick(ClickEvent event) {
				Boolean iscreate = true;
				str = "";
				str += tb_filename.getText() + " ";
				String alertstr = "";
				for (int i = 0; i < parameterValue.size(); i++) {
					String type = parameterListType.get(i).getValue(
							parameterListType.get(i).getSelectedIndex());

					if (parameterValue.get(i).getText().equals("")) {
						if (type.equals("int") || type.equals("double")
								|| type.equals("bool")) {
							iscreate = false;
							alertstr += "Please enter the default value for normal parameters" + "\n";
							// i=parameterValue.size();
						}

					}
					if (type.equals("in") || type.equals("out")) {
						if (parameterDetail.get(i).getText().equals("")) {
							alertstr += "Please enter the description of the file parameters" + "\n";
						}
					} else {
						if (parameterDetail.get(i).getText().equals(""))
							alertstr += "Please enter the description of the file parameters" + "\n";
						else if (parameterDetail.get(i).getText().contains(":"))
							alertstr += "'The characters can not be used in the description of normal parameters:'" + "\n";

					}

					if (parameterDetail.get(i).getText().equals(""))
						alertstr += "Please enter the description of the normal parameters" + "\n";
					else if (parameterDetail.get(i).getText().contains(":"))
						alertstr += "The characters can not be used in the description of normal parameters':'" + "\n";
				}


				if (tb_filename.getText().toString().equals("")) {
					alertstr += "Please enter the command" + "\n";
					iscreate = false;
				}
				if (!alertstr.equals("")) {
					Window.alert(alertstr);
				}

				if (iscreate) {
					for (int i = 0; i < parameterName.size(); i++) {
						String type = parameterListType.get(i).getValue(
								parameterListType.get(i).getSelectedIndex());
						String typedetail = parameterTypeDetail.get(i).getValue(
								parameterTypeDetail.get(i).getSelectedIndex());
						String name = parameterName.get(i).getText();

						// Value and detail may have spaces,
						// and characters such as \ t need to be escaped
						String value = parameterValue.get(i).getText();
						String detail = parameterDetail.get(i).getText();
						if(!name.equals(""))
							str += "--" + name +" ";
						if (type.equals("in") || type.equals("out")) {
							str += "{" + type + ":" + typedetail
									+ ":\"" + detail + "\"}" + " ";
						}else if( "string".equals(typedetail.toLowerCase())){
							str +=  "[\"" + detail + "\":" + type
									+ ":" + "default" + ",\"" + value + "\"]" + " ";
						}else{
							str +=  "[\"" + detail + "\":" + type
									+ ":" + "default" + "," + value + "]" + " ";
						}
					}
					setValue(str);
					cmd.setVisible(true);
					cmd.setText(getValue());
					cmd.setEnabled(false);

				}
			}

		});

		cellFormatter.setColSpan(0, 1, 4);
		grid.setWidget(0, 0, lb_filename);
		grid.setWidget(0, 1, tb_filename);
		grid.setWidget(0, 2, add);
		Label[] LableInfo = new Label[] { new Label(Constants.studioUIMsg.parameter()), new Label(Constants.studioUIMsg.type()),
				new Label(Constants.studioUIMsg.description()), new Label(Constants.studioUIMsg.value()), new Label(Constants.studioUIMsg.defaultValue()), new Label(Constants.studioUIMsg.remove()) };
		for (int i = 0; i < 6; i++) {
			LableInfo[i].setStyleName("desccmdpanel-Label");
		}
		grid.setWidget(1, 0, LableInfo[1]);
		grid.setWidget(1, 1, LableInfo[3]);
		grid.setWidget(1, 2, LableInfo[0]);
		grid.setWidget(1, 3, LableInfo[4]);
		grid.setWidget(1, 4, LableInfo[2]);
		grid.setWidget(1, 5, delete);
		cellFormatter.setColSpan(2, 0, 2);
		grid.setWidget(2, 0, create);
		cellFormatter.setColSpan(3, 1, 4);
		if (cmd.getText() != "") {
			cmd.setEnabled(true);
		} else {
			cmd.setVisible(false);
		}
		if (cmd.getText().equals("")) {
			cmd.setStyleName("desccmdpanel-cmd-areabox");
		}
		grid.setWidget(3, 1, cmd);
		dialogpanel.add(grid);
		DescCmdPanel.this.setSize("260px", "200px");
		DescCmdPanel.this.add(dialogpanel);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return this.str;
	}

	@Override
	public void setValue(String val) {
		str = val;
	}

}
