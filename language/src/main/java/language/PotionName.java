package language;

import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.Map;
import org.bukkit.potion.PotionType;

public enum PotionName {

  AWKWARD("AWKWARD", "awkward"),
  FIRE_RESISTANCE("FIRE_RESISTANCE", "fire_resistance"),
  HARM("INSTANT_DAMAGE", "harming"),
  HEAL("INSTANT_HEAL", "healing"),
  INCREASE_DAMAGE("STRENGTH", "strength"),
  INVISIBILITY("INVISIBILITY", "invisibility"),
  JUMP("JUMP", "leaping"),
  LUCK("LUCK", "luck"),
  MUNDANE("MUNDANE", "mundane"),
  NIGHT_VISION("NIGHT_VISION", "night_vision"),
  POISON("POISON", "poison"),
  REGENERATION("REGEN", "regeneration"),
  SLOW("SLOWNESS", "slowness"),
  SLOW_FALLING("SLOW_FALLING", "slow_falling"),
  SPEED("SPEED", "swiftness"),
  THICK("THICK", "thick"),
  TURTLE_MASTER("TURTLE_MASTER", "turtle_master"),
  UNCRAFTABLE("UNCRAFTABLE", "empty"),
  WATER("WATER", "water"),
  WATER_BREATHING("WATER_BREATHING", "water_breathing"),
  WEAKNESS("WEAKNESS", "weakness");

  private static final Map<String, PotionName> lookup = Maps.newHashMap();

  static {
    for (final PotionName effect : EnumSet.allOf(PotionName.class)) {
      lookup.put(effect.getPotionEffect(), effect);
    }
  }

  private final String potionEffect;
  private final String languageEntry;

  private PotionName(final String potionEffect, final String path) {
    this.potionEffect = potionEffect;
    languageEntry = path;
  }

  /**
   * @param effectType The effect type.
   * @return The index of a potion based on effect.
   */
  public static PotionName get(final PotionType effectType) {
    return lookup.get(effectType.toString());
  }

  public String getPotionEffect() {
    return potionEffect;
  }

  public String getLocalePath() {
    return languageEntry;
  }


}
