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
 * Delete confirmation prompt box
 */
public class DeletePanel extends PopupPanel {
	private Label title = new Label("Hint");
	private Label close = new Label();
	private Label content = new Label();
	private Button confirmBtn = new Button(Constants.adminUIMsg.confirm());
	private Button cancelBtn = new Button(Constants.adminUIMsg.cancel());
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel alertHeader = new HorizontalPanel();
	private HorizontalPanel alertButton = new HorizontalPanel();
	private VerticalPanel alertContent = new VerticalPanel();
	private VerticalPanel mask = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();

	public DeletePanel() {
		title.setStyleName("alertTitle");
		close.setStyleName("alertClose");
		alertHeader.add(title);
		alertHeader.add(close);
		alertHeader.addStyleName("alertTitleBG");

		content.removeStyleName("gwt-Label");
		content.addStyleName("alertBody");
		alertContent.add(content);
		alertContent.addStyleName("alertBodyBG");

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

		bind();
		root.add(mask);
		root.add(vPanel);
		this.add(root);
		this.removeStyleName("gwt-PopupPanel");
	}

	/**
	 * Event binding
	 */
	private void bind(){

		close.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DeletePanel.this.hide();
			}
		});

		cancelBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DeletePanel.this.hide();
			}

		});
	}

	@Override
	public void setTitle(String msg){
		title.setText(msg);
	}

	public void setContent(String msg){
		content.setText(msg);
	}

	public VerticalPanel getPanel(){
		return vPanel;
	}

	public Label getContent(){
		return content;
	}

	public Button getConfirmBtn(){
		return confirmBtn;
	}
}
