/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.util.Constants;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Add directory panel
 */
public class CateAddPanel extends PopupPanel {
	private String type;
	private Label title = new Label("Hint");
	private Label close = new Label();
	private Label nameLabel = new Label(Constants.adminUIMsg.cateName());
	private TextBox nameBox = new TextBox();
	private Label nameErrorLabel = new Label("");
	private Label fathLabel = new Label(Constants.adminUIMsg.catePath());
	private TextBox fathBox = new TextBox();
	private Label fathErrorLabel = new Label("");
	private Button confirmBtn = new Button(Constants.adminUIMsg.confirm());
	private Button cancelBtn = new Button(Constants.adminUIMsg.cancel());
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel mask = new HorizontalPanel();
	private VerticalPanel root = new VerticalPanel();

	public CateAddPanel() {
		bind();

		HorizontalPanel alertHeader = new HorizontalPanel();
		HorizontalPanel alertButton = new HorizontalPanel();
		VerticalPanel alertContent = new VerticalPanel();
		title.setStyleName("cateAddBoxTitle");
		close.setStyleName("alertClose");
		alertHeader.add(title);
		alertHeader.add(close);
		alertHeader.addStyleName("alertTitleBG");

		HorizontalPanel namePanel = new HorizontalPanel();
		HorizontalPanel fathPanel = new HorizontalPanel();
		nameLabel.getElement().getStyle().setMargin(9, Unit.PX);
		nameBox.removeStyleName("gwt-TextBox");
		nameBox.addStyleName("admin-cate-add-input");
		nameErrorLabel.addStyleName("account-error");
		namePanel.add(nameLabel);
		namePanel.add(nameBox);
		namePanel.add(nameErrorLabel);
		fathLabel.getElement().getStyle().setMargin(12, Unit.PX);
		fathBox.removeStyleName("gwt-TextBox");
		fathBox.addStyleName("admin-cate-add-input");
		fathErrorLabel.addStyleName("account-error");
		fathPanel.add(fathLabel);
		fathPanel.add(fathBox);
		fathPanel.add(fathErrorLabel);

		namePanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		namePanel.getElement().getStyle().setMarginBottom(20, Unit.PX);
		fathPanel.getElement().getStyle().setMarginTop(20, Unit.PX);
		fathPanel.getElement().getStyle().setMarginBottom(20, Unit.PX);
		alertContent.add(namePanel);
		alertContent.add(fathPanel);
		alertContent.addStyleName("margin_left_17");

		confirmBtn.removeStyleName("gwt-Button");
		cancelBtn.removeStyleName("gwt-Button");
		cancelBtn.getElement().getStyle().setMarginLeft(105, Unit.PX);

		alertButton.add(confirmBtn);
		alertButton.add(cancelBtn);
		alertButton.addStyleName("alertButton2");

		vPanel.add(alertHeader);
		vPanel.add(alertContent);
		vPanel.add(alertButton);

		vPanel.addStyleName("alertBox");
		mask.addStyleName("alertBack");

		root.add(mask);
		root.add(vPanel);
		this.add(root);
		this.removeStyleName("gwt-PopupPanel");
	}

	/**
	 * Event binding
	 */
	public void bind(){
		//close
		close.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CateAddPanel.this.nameBox.setValue("");
				CateAddPanel.this.fathBox.setValue("");
				CateAddPanel.this.nameErrorLabel.setText("");
				CateAddPanel.this.fathErrorLabel.setText("");
				CateAddPanel.this.hide();
			}
		});

		//cancel
		cancelBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				CateAddPanel.this.nameBox.setValue("");
				CateAddPanel.this.fathBox.setValue("");
				CateAddPanel.this.nameErrorLabel.setText("");
				CateAddPanel.this.fathErrorLabel.setText("");
				CateAddPanel.this.hide();
			}

		});
	}

	@Override
	public void setTitle(String msg){
		title.setText(msg);
	}

	public String getType(){
		return type;
	}

	public VerticalPanel getPanel(){
		return vPanel;
	}

	public TextBox getNameBox(){
		return nameBox;
	}

	public Label getNameErrorLabel(){
		return nameErrorLabel;
	}

	public TextBox getFatherBox(){
		return fathBox;
	}

	public Label getFathErrorLabel(){
		return fathErrorLabel;
	}

	public Button getCancelBtn(){
		return cancelBtn;
	}

	public Button getConfirmBtn(){
		return confirmBtn;
	}

	public Label getClose(){
		return close;
	}
}
