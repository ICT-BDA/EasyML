/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.panel;

import eml.studio.client.util.Constants;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Prompt box
 * 
 */
public class AlertPanel extends PopupPanel {
	private Label title = new Label("Hint");
	private Label close = new Label();
	private Label content = new Label();
	private Button confirmBtn = new Button(Constants.adminUIMsg.confirm());
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel alertHeader = new HorizontalPanel();
	private HorizontalPanel alertButton = new HorizontalPanel();
	private VerticalPanel alertContent = new VerticalPanel();
	private VerticalPanel mask = new VerticalPanel();
	private VerticalPanel root = new VerticalPanel();

	public AlertPanel() {
		bind();

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
		alertButton.add(confirmBtn);
		alertButton.addStyleName("alertButton1");

		vPanel.add(alertHeader);
		vPanel.add(alertContent);
		vPanel.add(alertButton);

		vPanel.addStyleName("alertBox");
		mask.addStyleName("alertBack");

		mask.addStyleName("alertBack");
		mask.getElement().getStyle().setDisplay(Display.NONE);
		vPanel.add(mask);

		root.add(mask);
		root.add(vPanel);
		this.add(root);
		this.removeStyleName("gwt-PopupPanel");
		this.getElement().getStyle().setPosition(Position.FIXED);
		this.getElement().getStyle().setZIndex(999999999);
	}

	/**
	 * Event binding
	 */
	private void bind(){
		//close
		close.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AlertPanel.this.hide();
			}
		});

		//confirm
		confirmBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				AlertPanel.this.hide();
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

	public VerticalPanel getMask(){
		return mask;
	}

	public Label getContent(){
		return content;
	}

	public Button getConfirmBtn(){
		return confirmBtn;
	}

	public Label getClose(){
		return close;
	}
}
