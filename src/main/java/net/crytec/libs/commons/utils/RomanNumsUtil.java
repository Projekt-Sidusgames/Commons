package net.crytec.libs.commons.utils;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * An API for converting latin numbers to roman and vice versa
 */
public class RomanNumsUtil {

  private static final NavigableMap<Integer, String> reversed;
  private static final TreeMap<Integer, String> romanNums = new TreeMap<>();

  static {
    romanNums.put(1, "I");
    romanNums.put(4, "IV");
    romanNums.put(5, "V");
    romanNums.put(9, "IX");
    romanNums.put(10, "X");
    romanNums.put(40, "XL");
    romanNums.put(50, "L");
    romanNums.put(90, "XC");
    romanNums.put(100, "C");
    romanNums.put(400, "CD");
    romanNums.put(500, "D");
    romanNums.put(900, "CM");
    romanNums.put(1000, "M");
    reversed = romanNums.descendingMap();
  }

  /**
   * Convert the given roman number to latin
   *
   * @param roman - The roman number
   * @return The latin number (conversion result)
   */
  public static int fromRoman(String roman) {
    roman = roman.toUpperCase();
    int out = 0;
    for (final Map.Entry<Integer, String> e : reversed.entrySet()) {
      final String st = e.getValue();
      final int am = e.getKey();
      while (roman.startsWith(st)) {
        out += am;
        roman = roman.substring(st.length());
      }
    }
    return out;
  }

  /**
   * Convert the given latin number to roman
   *
   * @param num - The latin number
   * @return The roman number (conversion result)
   */
  public static String toRoman(int num) {
    final StringBuilder out = new StringBuilder();
    while (num > 0) {
      final Map.Entry<Integer, String> e = romanNums.floorEntry(num);
      num -= e.getKey();
      out.append(e.getValue());
    }
    return out.toString();
  }
}
