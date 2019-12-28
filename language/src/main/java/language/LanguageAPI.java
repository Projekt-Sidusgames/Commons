package language;

import net.crytec.libs.commons.utils.MaterialSet;
import net.crytec.libs.commons.utils.RomanNumsUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

public class LanguageAPI {

  private Language language;
  private final JavaPlugin host;

  private static MaterialSet potions;
  private static LanguageAssetLoader assets;

  public LanguageAPI(final JavaPlugin host, final Language language) {
    this.host = host;
    this.language = language;

    setLanguage(Language.EN_US);

    LanguageAPI.assets = new LanguageAssetLoader(this);

    LanguageAPI.potions = new MaterialSet(new NamespacedKey(host, "lang-potion"),
        Material.LINGERING_POTION,
        Material.SPLASH_POTION,
        Material.TIPPED_ARROW,
        Material.POTION
    );
  }

  public void setLanguage(final Language language) {
    this.language = language;
  }

  public Language getLanguage() {
    return language;
  }


  public static String getEnchantment(final Enchantment enchantment) {
    return getEnchantment(enchantment, 0);
  }


  public static String getEnchantment(final Enchantment enchantment, final int level) {
    final String path = "enchantment." + enchantment.getKey().getNamespace() + '.' + enchantment.getKey().getKey();

    return assets.translate(path) + " " + RomanNumsUtil.toRoman(level);
  }


  /**
   * Translates the name of the entity to a readable name that is also used clientside. In case the entity has a custom displayname, this will be returned instead
   *
   * @param entity The Entity to translate
   * @return returns the translated entity name
   */
  public static String getEntityName(final Entity entity) {
    return entity.getCustomName() != null ? entity.getCustomName() : getEntityName(entity.getType());
  }


  /**
   * Returns the translated name of the given EntityType
   *
   * @param type The EntityType to translate
   * @return the translated name
   */
  public static String getEntityName(final EntityType type) {
    final String path = "entity." + type.getKey().getNamespace() + '.' + type.getKey().getKey();
    return assets.translate(path);
  }

  public static String translate(final String path) {
    return assets.translate(path);
  }


  public static String getItemName(final ItemStack item) {
    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
      return item.getItemMeta().getDisplayName();
    }

    final Material type = item.getType();

    String path = "unknown";

    if (item.getType().isBlock()) {
      path = "block." + type.getKey().getNamespace() + '.' + type.getKey().getKey();
    } else if (potions.isTagged(item)) {

      final StringBuilder pathBuilder = new StringBuilder()
          .append("item.")
          .append(type.getKey().getNamespace())
          .append('.')
          .append(type.getKey().getKey())
          .append('.')
          .append("effect")
          .append(".");

      final PotionMeta meta = (PotionMeta) item.getItemMeta();
      final PotionType potiontype = meta.getBasePotionData().getType();

      pathBuilder.append(PotionName.get(potiontype).getLocalePath());

      path = pathBuilder.toString();

    } else if (item.getType().isItem()) {
      path = "item." + type.getKey().getNamespace() + '.' + type.getKey().getKey();
    }
    return assets.translate(path);
  }


  protected JavaPlugin getHost() {
    return host;
  }


}
