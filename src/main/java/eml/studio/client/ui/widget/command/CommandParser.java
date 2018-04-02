/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

/**
 * A {@link CommandParser} is used to parse string into {@link Commander}
 */
public class CommandParser {

	/**
	 * @param arr A character array
	 * @param ch The character to find
	 * @param begin The begin offset in arr to find ch
	 * @return The next offset of ch
	 */
	public static int nextChar(char []arr,char ch, int begin){
		int k = begin;
		while( k < arr.length && arr[k] != ch ) k ++;
		if( k == arr.length ) return -1;
		return k;
	}

	/**
	 * @param arr A character array
	 * @param ch The character to find
	 * @param begin The begin offset in arr to find ch
	 * @param end The end offset in arr to find ch
	 * @return The next offset of ch
	 */
	public static int nextChar(char []arr,char ch, int begin, int end){
		int k = begin;
		while( k < end && arr[k] != ch ) k ++;
		if( arr[k] == ch ) return k;
		else return -1;
	}

	/**
	 * To extract next string
	 * @param arr An array of characters
	 * @param begin The begin offset in arr
	 * @param sb A StringBuilder would be update in the method
	 * @return The end offset in arr of current called
	 */
	public static int extractStr(char [] arr, int begin, StringBuilder sb){
		int k = begin + 1;
		while( k < arr.length  ){
			if( arr[k] == '\\' ){
				if( k + 1 < arr.length ){
					if( arr[k+1] == '\\' ){//  Escaped\\
						sb.append('\\');
						k += 2;
					}else if( arr[k+1] == '"' ){// Escaped\"
						sb.append('"');
						k += 2;
					}else{// If only have one \+, other character regardless
						sb.append('\\');
						k ++;
					}
				}else{
					k ++;
				}
			}else if( arr[k] == '"' ){//End character
				k ++;
				break;
			}else{
				sb.append(arr[k]);
				k ++;
			}
		}

		return k;
	}

	/**
	 * To extract file field holder：<br/>
	 * Input file field holder：{in:FileType:"File description"}<br/>
	 * Output file field holder：{out:FileType,StoreType:"File description"}
	 * @param arr A array of characters that represent the command format string
	 * @param offset Index offset in arr
	 * @param holder A {@link FileHolder} to update
	 * @throws CommandParseException
	 */
	public static int extractFileHolder(char[] arr, int offset,FileHolder holder) throws CommandParseException{
		//The next colon offset
		int k = nextChar(arr, ':', offset + 1);
		if( k == -1 ) throw new CommandParseException(
				"Parsing failed, colon is not found in " +String.valueOf(arr, offset + 1, arr.length));

		String word = String.valueOf( arr, offset + 1, k - offset - 1 );
		String cntType = null;
		String storeType = null;

		if( word.equals( "in") ){
			holder.setFileType( FileHolder.FileType.InputFile);
			offset = k;
			k = nextChar(arr, ':', offset + 1 );
			if( k == -1 ) throw new CommandParseException(
					"Parsing failed, colon is not found in " +String.valueOf(arr, offset + 1, arr.length));

			cntType = String.valueOf(arr,offset + 1, k - offset - 1 );
			storeType = FileDescription.StoreType.HFile.name();;
		}else if( word.equals("out") ){
			holder.setFileType( FileHolder.FileType.OutputFile);
			offset = k;
			//The next colon offset
			int end = nextChar(arr,':',offset + 1 );

			if( end == -1 ) throw new CommandParseException(
					"Parsing failed, colon is not found in " +String.valueOf(arr, offset + 1, arr.length - offset - 1));

			k = nextChar(arr, ',', offset + 1, end );
			//There are no colons exists
			if( k == - 1 ){
				cntType = String.valueOf(arr,offset + 1, end - offset - 1 );
				storeType = FileDescription.StoreType.HFile.name();
			}else{
				cntType = String.valueOf(arr,offset + 1, k - offset - 1 );
				storeType = String.valueOf(arr,k + 1, end - k - 1 );
			}
			k = end;
		}else{
			throw new CommandParseException(
					"Parsing failed at string line index: " + offset );
		}

		holder.setContentType(cntType);
		holder.setStoreType( FileDescription.StoreType.get(storeType) );

		k ++;
		if( k < arr.length && arr[k] != '"' ){
			throw new CommandParseException(
					"Parsing failed, file field is not found in " +String.valueOf(arr, offset + 1, k - offset));
		}

		StringBuilder sb = new StringBuilder();

		//extract the file description
		k = extractStr(arr,k,sb);

		holder.setDescription( sb.toString());


		if( k < arr.length && arr[k] != '}' ){
			throw new CommandParseException(
					"Parsing failed, the file field holder is not enclosed by {}");
		}

		k ++;
		if( k < arr.length && !(arr[k] == ' ' || arr[k] == '\t') ){
			throw new CommandParseException(
					"Parsing failed, the fields should be splited by space");
		}
		return k;
	}

	/**
	 * To extract parameter field holder：<br/>
	 *  [\"Parameter Name\":ParameterType,min,max:default,defaultValue]<br/>
	 *  If the ParameterType is a string, then defaultValue must contained by \"\" 
	 * @param arr An array of characters
	 * @param offset Current offset
	 * @param param A {@link Parameter} to update
	 * @throws CommandParseException
	 */
	public static int extractParameter(char[] arr, int offset, Parameter param) throws CommandParseException{
		int k = offset + 1;
		if( k < arr.length && arr[k] != '"' ) throw new CommandParseException(
				"Parsing failed, caused by the parameter name is not enclosed by '\"'");

		StringBuilder sb = new StringBuilder();
		//To extract parameter name
		k = extractStr(arr, k, sb);
		String name = sb.toString();
		if( k == -1 ){
			throw new CommandParseException(
					"Parsing failed, caused by the parameter name is not enclosed by '\"'");
		}

		/** Get the type and the maximum minimum area**/
		if( arr[k] != ':' ) throw new CommandParseException(
				"Parsing failed, a parameter name should be followed by colon");

		offset = k + 1;
		int end = nextChar(arr, ':', offset );
		if( end == -1 ) throw new CommandParseException(
				"Parsing failed, colon is not found in " +String.valueOf(arr, offset, arr.length - offset));

		//The first comma offset
		k = nextChar(arr, ',', offset, end );
		String type = null;
		String min = null;
		String max = null;
		String dfault = null;
		if( k == -1 ){
			type = String.valueOf(arr, offset, end - offset );
		}else{
			type = String.valueOf(arr, offset, k - offset );
			if( "string".equals( type.toLowerCase())) throw new CommandParseException(
					"Parsing failed，'String' has no min and max");

			//The next comma offset
			offset = k + 1;
			//The second comma offset
			k = nextChar(arr, ',', offset, end);
			if( k == -1 ){
				min = String.valueOf(arr, offset, end - offset );
			}else{
				min = String.valueOf(arr, offset, k - offset );
				//The character at offset k should be comma, so skipped
				max = String.valueOf(arr, k + 1, end - k - 1 );
			}
		}

		offset = end + 1;
		//find the comma before defaultValue
		k = nextChar(arr,',', offset );

		if( k == -1 ) throw new CommandParseException(
				"Parsing failed, comma is not found in " +String.valueOf(arr, offset, arr.length - offset));

		offset = k + 1;
		//[...:default,..]
		if( offset < arr.length && arr[offset] != ']' ){

			if( "string".equals(type.toLowerCase()) ){
				if( arr[offset] != '"' ) throw new CommandParseException(
						"Parsing failed, the default value of string should be enclosed by '\"'");

				StringBuilder descSb = new StringBuilder();
				k = extractStr(arr, offset, descSb);
				if( k > arr.length || arr[k] != ']' )  throw new CommandParseException(
						"Parsing failed, the parameter field is not enclosed by []");

				dfault = descSb.toString();

			}else{
				k = nextChar( arr, ']', offset );
				if( k == -1 ){
					throw new CommandParseException("Parsing failed, the parameter field is not enclosed by []");
				}

				dfault = String.valueOf(arr, offset, k - offset );
			}
		}

		param.setParamName( name );
		param.setParamType( type );

		if( min != null && !"".equals( min ) ){
			Object obj = ValueCheck.validate(type, min);
			if(obj == null ) throw new CommandParseException("Value '"+min+"' can't be parsed to' type:"+type);
			else param.setMinValue(min);
		}
		if( max != null && !"".equals(max)){
			Object obj = ValueCheck.validate(type, max);
			if(obj == null ) throw new CommandParseException("Value '"+max+"' can't be parsed to' type:"+type);
			else param.setMaxValue(max);
		}

		if( dfault != null && !"".equals(dfault)){
			Object obj = ValueCheck.validate(type, dfault);
			if(obj == null ) throw new CommandParseException("Value '"+dfault+"' can't be parsed to' type:"+type);
			else param.setParamValue(dfault);
		}

		if( k < arr.length && arr[k] != ']' ){
			throw new CommandParseException(
					"Parsing failed, the parameter field is not enclosed by []");
		}

		k ++;
		if( k < arr.length && !(arr[k] == ' ' || arr[k] == '\t') ){
			throw new CommandParseException(
					"Parsing failed, the fields should be splited by space");
		}
		return k;
	}

	public static int extractScriptHolder(char []arr, int offset, ScriptHolder holder) throws CommandParseException{
		int k = offset + 1;
		if( k < arr.length && arr[k] != '"' ) throw new CommandParseException(
				"Parsing failed, the script should be enclosed by '\"'");

		StringBuilder sb = new StringBuilder();
		k = extractStr(arr, k, sb);

		if( k == -1 )throw new CommandParseException(
				"Parsing failed, the script should be enclosed by '\"'");

		holder.setName( sb.toString() );

		if( k < arr.length && arr[k] != ')' ){
			throw new CommandParseException(
					"Parsing failed, the script field should be enclosed by ()");
		}

		k ++;
		if( k < arr.length && !(arr[k] == ' ' || arr[k] == '\t') ){
			throw new CommandParseException(
					"Parsing failed, the fields should be splited by space");

		}

		return k;
	}

	public static Commander parse(String cmdline) throws CommandParseException{
		char []arr = cmdline.trim().toCharArray();
		int i,j,k;
		i = 0;
		j = 0;
		Commander cmd = new Commander();
		while( i < arr.length ){
			if( arr[i] == ' ' || arr[i] == '\t'){
				k = i + 1;
				//to skip all tab and space
				while( k < arr.length && (arr[k] == ' ' || arr[k] == '\t') ) k ++;

				if( i - j > 0 ){ // if true, there are contents
					cmd.getSplitList().add( String.valueOf(arr, j, i - j ));
				}
				i = j = k;
			}
			else if( arr[i] == '{' ){
				FileHolder holder = new FileHolder();
				holder.setIndex( cmd.getSplitList().size());
				i = extractFileHolder(arr,i,holder);
				switch(holder.getFileType())
				{
				case InputFile:
					cmd.getInFileHolders().add( holder);
					break;
				case OutputFile:
					cmd.getOutFileHolders().add( holder);
					break;
				default :
				}
			}else if( arr[i] == '['){
				Parameter param = new Parameter();
				param.setIndex( cmd.getSplitList().size() );
				i = extractParameter(arr, i, param);
				cmd.getParameters().add(param);
			}else if( arr[i] == '(' ){
				if( cmd.getScriptHolder() != null ){
					throw new CommandParseException("Only one script field is allowed");
				}
				ScriptHolder holder = new ScriptHolder();
				holder.setIndex( cmd.getSplitList().size() );
				i = extractScriptHolder(arr, i, holder );

				cmd.setScriptHolder( holder );

			}else i ++;
		}

		if( i - j > 0 ){
			cmd.getSplitList().add( String.valueOf(arr, j, i - j ));
		}
		return cmd;
	}
	public static void main(String args[]) throws CommandParseException{
		/*String line = "python bda_xgb_classification.py "
        + "{in:General:\"mylibsvm  训练文件\"} "
        + "{in:General:\"mylibsvm预测文件\"} "
        + "{out:General:\"预测概率输出文件\"} "
        + "[\"n_round\":Int:default,60] "
        + "[\"max_depth\":Int:default,8] "
        + "[\"eta\":Double:default,0.2] "
        + "[\"min_child_weight\":Int:default,500] "
        + "[\"learning_rate\":Double:default,0.01] "
        + "[\"nthread\":Int:default,10] "
        + "[\"gamma\":Int:default,0] "
        + "[\"max_delta_step\":String:default,\"wodsfhoadsfas 0fasdf \\\"woaizhognguo\\\"\\sdf\"] "
        + "[\"subsample\":Double:default,0.8] "
        + "[\"colsample_bytree\":Double:default,0.85] "
        + "[\"lambda\":Int:default,1]";
		 */
		String line = "spark-submit --class bda.spark.streaming.crf.CRFStreamDecoder crfstreaming.jar --stream_host [\"IP地址\":String:default,\"10.60.0.57\"] --stream_port [\"端口号\":String:default,\"19999\"] --output_pt {out:LabeledPoint:\"输出文件\"} --duration [\"批量间隔\":String:default,\"10\"] --metadata_f {in:LabeledPoint:\"模型文件\"} --feature_f {in:LabeledPoint:\"特征文件\"} --weight_f {in:LabeledPoint:\"权重文件\"}  --test {din:LabelPoint:\"流输入文件\"}";
		System.out.println( line );
		Commander cmd = parse(line);
		System.out.println(cmd.toCommandLine(false));
	}

	public static String reConstruct(Commander cmd){

		String line = null;
		String arr[] = new String[cmd.getSplitList().size()];
		cmd.getSplitList().toArray(arr);
		for( FileHolder f: cmd.getInFileHolders())
		{
			line = "{in:"+f.getContentType() + ":\"" + f.getDescription()+"\"}";
			arr[f.getIndex()] = line;
			System.out.println( line );
		}

		for( FileHolder f: cmd.getOutFileHolders())
		{
			line = "{out:"+f.getContentType() +"," + f.getStoreType() + ":\"" + f.getDescription()+"\"}";
			arr[f.getIndex()] = line;
		}

		for( Parameter param: cmd.getParameters()){
			line = "[\"" + param.getParaName() +"\":" + param.getParamType();
			if( param.getMinValue() != null ){
				line += "," + param.getMinValue();
			}
			if( param.getMaxValue() != null ){
				if( param.getMinValue() == null )
					line += ",";
				line +="," + param.getMaxValue();
			}

			line += ":default,";
			if( param.getParamValue() != null ){
				if("string".equals( param.getParamType().toLowerCase()) ){
					line += "\""+param.getParamValue().replace("\\", "\\\\").replace("\"", "\\\"") +"\"";
				}else
					line += param.getParamValue();
			}

			line += "]";
			arr[param.getIndex()] = line;
		}
		line = "";
		for( String str: arr){
			line += str + " ";
		}

		return line;
	}
}
