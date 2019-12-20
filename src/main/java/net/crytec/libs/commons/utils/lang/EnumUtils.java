package net.crytec.libs.commons.utils.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Utility library to provide helper methods for Java enums.</p>
 */
public class EnumUtils {

  private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
  private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
  private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
  private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

  /**
   * <p>Gets the enum for the class, returning {@code null} if not found.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that it does not throw an exception
   * for an invalid enum name.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @param enumName  the enum name, null returns null
   * @return the enum, null if not found
   */
  public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
    return getEnum(enumClass, enumName, null);
  }

  /**
   * <p>Gets the enum for the class, returning {@code defaultEnum} if not found.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that it does not throw an exception
   * for an invalid enum name.</p>
   *
   * @param <E>         the type of the enumeration
   * @param enumClass   the class of the enum to query, not null
   * @param enumName    the enum name, null returns default enum
   * @param defaultEnum the default enum
   * @return the enum, default enum if not found
   * @since 3.10
   */
  public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName, final E defaultEnum) {
    if (enumName == null) {
      return defaultEnum;
    }
    try {
      return Enum.valueOf(enumClass, enumName);
    } catch (final IllegalArgumentException ex) {
      return defaultEnum;
    }
  }

  /**
   * <p>Gets the enum for the class, returning {@code null} if not found.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that it does not throw an exception
   * for an invalid enum name and performs case insensitive matching of the name.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @param enumName  the enum name, null returns null
   * @return the enum, null if not found
   * @since 3.8
   */
  public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass, final String enumName) {
    return getEnumIgnoreCase(enumClass, enumName, null);
  }

  /**
   * <p>Gets the enum for the class, returning {@code defaultEnum} if not found.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that it does not throw an exception
   * for an invalid enum name and performs case insensitive matching of the name.</p>
   *
   * @param <E>         the type of the enumeration
   * @param enumClass   the class of the enum to query, not null
   * @param enumName    the enum name, null returns default enum
   * @param defaultEnum the default enum
   * @return the enum, default enum if not found
   * @since 3.10
   */
  public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass, final String enumName, final E defaultEnum) {
    if (enumName == null || !enumClass.isEnum()) {
      return defaultEnum;
    }
    for (final E each : enumClass.getEnumConstants()) {
      if (each.name().equalsIgnoreCase(enumName)) {
        return each;
      }
    }
    return defaultEnum;
  }

  /**
   * <p>Gets the {@code List} of enums.</p>
   *
   * <p>This method is useful when you need a list of enums rather than an array.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @return the modifiable list of enums, never null
   */
  public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass) {
    return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
  }

  /**
   * <p>Gets the {@code Map} of enums by name.</p>
   *
   * <p>This method is useful when you need a map of enums by name.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @return the modifiable map of enum names to enums, never null
   */
  public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
    final Map<String, E> map = new LinkedHashMap<>();
    for (final E e : enumClass.getEnumConstants()) {
      map.put(e.name(), e);
    }
    return map;
  }

  /**
   * <p>Checks if the specified name is a valid enum for the class.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that checks if the name is
   * a valid enum without needing to catch the exception.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @param enumName  the enum name, null returns false
   * @return true if the enum name is valid, otherwise false
   */
  public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
    return getEnum(enumClass, enumName) != null;
  }

  /**
   * <p>Checks if the specified name is a valid enum for the class.</p>
   *
   * <p>This method differs from {@link Enum#valueOf} in that checks if the name is
   * a valid enum without needing to catch the exception and performs case insensitive matching of the name.</p>
   *
   * @param <E>       the type of the enumeration
   * @param enumClass the class of the enum to query, not null
   * @param enumName  the enum name, null returns false
   * @return true if the enum name is valid, otherwise false
   * @since 3.8
   */
  public static <E extends Enum<E>> boolean isValidEnumIgnoreCase(final Class<E> enumClass, final String enumName) {
    return getEnumIgnoreCase(enumClass, enumName) != null;
  }

  /**
   * This constructor is public to permit tools that require a JavaBean instance to operate.
   */
  public EnumUtils() {
  }
}