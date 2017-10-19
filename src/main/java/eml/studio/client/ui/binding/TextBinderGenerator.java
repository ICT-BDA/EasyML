/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.binding;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * The TextBinderGenerator allow the GWT coder to generate Java code at compile time 
 * and have it then be complied along with the rest of the project into JavaScript<br/>
 * 
 * This class auto generate method bind() and sync() for {@link TextBinder}
 */
public class TextBinderGenerator extends Generator {
	private String implPackageName;

	private String implTypeName;
	private JClassType parameterizedType1;
	private JClassType parameterizedType2;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String requestedClass) throws UnableToCompleteException {

		TypeOracle typeOracle = context.getTypeOracle();

		JClassType objectType = typeOracle.findType(requestedClass);
		if (objectType == null) {
			logger.log(TreeLogger.ERROR, "Could not find type: " + requestedClass);
			throw new UnableToCompleteException();
		}

		implTypeName = objectType.getSimpleSourceName() + "Impl";

		implPackageName = objectType.getPackage().getName();

		JClassType[] implementedTypes = objectType.getImplementedInterfaces();

		// Can only implement one interface
		if (implementedTypes == null
				|| implementedTypes.length != 1
				|| !implementedTypes[0].getQualifiedSourceName().equals(
						TextBinder.class.getName())) {
			logger
			.log(
					TreeLogger.ERROR,
					"The type: " + requestedClass
					+ " Must implement only one interface: "
					+ TextBinder.class.getName());
			throw new UnableToCompleteException();
		}

		// Get parameterized type
		JParameterizedType parameterType = implementedTypes[0].isParameterized();
		if (parameterType == null) {
			logger.log(TreeLogger.ERROR, "The type: " + requestedClass
					+ " Must implement only one parameterized interface: "
					+ TextBinder.class.getName());
			throw new UnableToCompleteException();
		}

		if (parameterType.getTypeArgs() == null

				|| parameterType.getTypeArgs().length != 2) {
			logger.log(TreeLogger.ERROR,
					"The type: " + requestedClass
					+ " Must implement two parameterized interface: "
					+ TextBinder.class.getName() + " with two Parameter");
			throw new UnableToCompleteException();

		}

		parameterizedType1 = parameterType.getTypeArgs()[0];
		parameterizedType2 = parameterType.getTypeArgs()[1];
		// logger.log(TreeLogger.INFO ,
		// parameterizedType2.getParameterizedQualifiedSourceName() +"\n"
		// + parameterizedType2.getQualifiedSourceName());

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				implPackageName, implTypeName);

		composerFactory.addImport(Map.class.getCanonicalName());
		composerFactory.addImport(List.class.getCanonicalName());
		// composerFactory.addImport(Field.class.getCanonicalName());
		composerFactory
		.addImplementedInterface(objectType.getQualifiedSourceName());

		PrintWriter printWriter = context.tryCreate(logger, implPackageName,
				implTypeName);
		if (printWriter != null) {

			SourceWriter sourceWriter = composerFactory.createSourceWriter(context,
					printWriter);

			composeBindMethod(logger, sourceWriter);
			composeSyncMethod(logger, sourceWriter);
			sourceWriter.commit(logger);

		}
		return implPackageName + "." + implTypeName;
	}

	/**
	 * Generate method bind
	 */
	private void composeBindMethod(TreeLogger logger, SourceWriter sourceWriter) {

		logger.log(TreeLogger.INFO, "");
		String line = "public void bind("
				+ parameterizedType1.getQualifiedSourceName() + " text, "
				+ parameterizedType2.getQualifiedSourceName() + " obj){";
		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

		line = "  System.out.println(\"Implement it now:)\");";
		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

		ArrayList<JField> fields = new ArrayList<JField>();

		JClassType curtype = parameterizedType2;
		do {

			for (JField filed : curtype.getFields()) {
				fields.add(filed);
			}
			curtype = curtype.getSuperclass();
		} while (!curtype.getName().equals("Object"));

		for (JField field : fields) {
			String name = field.getName();
			String Name = name.substring(0, 1).toUpperCase() + name.substring(1);
			line = " text.setText(\"" + name + "\", obj.get" + Name
					+ "().toString() );";
			sourceWriter.println(line);
			logger.log(TreeLogger.INFO, line);

		}
		line = "}";

		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

	}

	/**
	 * Generate method sync
	 */
	private void composeSyncMethod(TreeLogger logger, SourceWriter sourceWriter) {

		logger.log(TreeLogger.INFO, "");
		String line = "public void sync("
				+ parameterizedType1.getQualifiedSourceName() + " text, "
				+ parameterizedType2.getQualifiedSourceName() + " obj){";
		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

		line = "  System.out.println(\"Implement it now:)\");";
		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

		ArrayList<JField> fields = new ArrayList<JField>();

		JClassType curtype = parameterizedType2;
		do {

			for (JField filed : curtype.getFields()) {
				fields.add(filed);
			}
			curtype = curtype.getSuperclass();
		} while (!curtype.getName().equals("Object"));

		for (JField field : fields) {
			String name = field.getName();
			String Name = name.substring(0, 1).toUpperCase() + name.substring(1);
			String type = field.getType().getQualifiedSourceName();
			String simType = field.getType().getSimpleSourceName();
			if ("java.lang.String".equals(type))
				line = " if( text.getText(\"" + name + "\") != null )obj.set" + Name
				+ "( text.getText(\"" + name + "\") );";
			else
				line = " if( text.getText(\"" + name + "\") != null )obj.set" + Name
				+ "( " + type + ".parse" + simType + "( text.getText(\"" + name
				+ "\")) );";

			sourceWriter.println(line);
			logger.log(TreeLogger.INFO, line);

		}
		line = "}";

		sourceWriter.println(line);
		logger.log(TreeLogger.INFO, line);

	}
}
