/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface AdminUIMessages extends Messages{

	String chooseCate();
	String addCate();
	String cateName();
	String catePath();
	String confirm();
	String cancel();
	String alert1();
	String alert2();
	String alert3();
	String alert4();
	String alert5();
	String alert6();
	String alert7();
	String delete1();
	String delete2();
	String delete3();
	String delete4();
	String change();

	//program management
	String deleteProg();
	String progDelete1();
	String progDelete2();
	String progSuccess();

	//dataset management
	String deleteData();
	String dataDelete1();
	String dataDelete2();
	String dataSuccess();

	//user management
	String power1();
	String power2();
	String power3();
	String powerSuccess();
	String userDelete1();
	String userDelete2();
	String userSuccess();
	String editPower();
	String deleteUser();

	//category management
	String dataAdd();
	String progAdd();
	String deleteCate();

	//search
	String searchNoInput();
	String searchNoResult();
	String searchCateError();

	//page
	String firstPage();
	String lastPage();
	String prevPage();
	String nextPage();
}
