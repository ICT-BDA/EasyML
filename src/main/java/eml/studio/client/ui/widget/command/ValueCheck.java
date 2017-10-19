/**
 * Copyright 2017 Institute of Computing Technology, Chinese Academy of Sciences.
 * Licensed under the terms of the Apache 2.0 license.
 * Please see LICENSE file in the project root for terms
 */
package eml.studio.client.ui.widget.command;

import java.util.logging.Logger;

public class ValueCheck {
  private static Logger logger = Logger.getLogger(ValueCheck.class.getName());

  /**
   * Validate the value
   * @param type The value type
   * @param value The string value
   * @return The real object of the value
   */
  public static Object validate(String type, String value) {

    Object res = null;
    try {
      if ("int".equals(type.toLowerCase())) {
        res = Integer.parseInt(value.trim());
      } else if ("double".equals(type.toLowerCase()) || "float".equals(type.toLowerCase())) {
        res = Double.parseDouble(value.trim());
      } else if ("bool".equals(type.toLowerCase())
          || "boolean".equals(type.toLowerCase())) {
        value = value.trim();

        if (!("false".equals(value) || "False".equals(value)
            || "True".equals(value) || "true".equals(value)))
          return null;

        res = Boolean.parseBoolean(value);
      } else if( "string".equals(type.toLowerCase())){
        res = value;
      }
    } catch (Exception ex) {
      return null;
    }
    return res;

  }

  public static void validate(Parameter param, String value)
      throws ValueInvalidException {
    Object res = validate(param.getParamType(), value);
    if (res == null) {
      throw new ValueInvalidException("Parameter [" + param.getParaName() + "] value \""
          + value + "\" is invalide");
    }

    if ("int".equals(param.getParamType().toLowerCase())
        || "double".equals(param.getParamType().toLowerCase())) {
      double val;
      if ("int".equals(param.getParamType().toLowerCase()))
        val = (Integer) res;
      else
        val = (Double) res;

      String minStr = param.getMinValue();
      String maxStr = param.getMaxValue();

      if (minStr != null && !minStr.equals("")) {

        Double min = Double.parseDouble(minStr);

        if (min > val) {
          throw new ValueInvalidException("Parameter [" + param.getParaName()
              + "] should not small than " + minStr);
        }
      }

      if (maxStr != null && !maxStr.equals("")) {
        Double max = Double.parseDouble(maxStr);
        if (max < val) {
          throw new ValueInvalidException("Parameter [" + param.getParaName()
              + "] should not bigger than " + maxStr);
        }
      }

    }
  }
}
