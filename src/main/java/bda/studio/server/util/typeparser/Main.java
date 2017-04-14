/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package bda.studio.server.util.typeparser;


public class Main {

	public static void main(String args[]){
		/**
		 * You can customize the type resolver and register the parsing type with the registerTypeParser of StringToTypeParserBuilder
		StringToTypeParser parser = StringToTypeParser.newBuilder()
				.registerTypeParser(Car.class, new CarTypeParser())
				.registerTypeParser(int.class, new MySpecialIntTypeParser())
				.build();

		Car volvo = parser.parseType("volvo", Car.class);
		*/
		StringToTypeParser parser = StringToTypeParser.newBuilder().build();
//		String  = parser.parse("", String.class);
		System.out.println(  );
	}
}
