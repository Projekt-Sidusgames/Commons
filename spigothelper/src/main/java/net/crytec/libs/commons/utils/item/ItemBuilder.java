package net.crytec.libs.commons.utils.item;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import net.crytec.libs.commons.utils.lang.Validate;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

  private final ItemStack item;
  private final ItemMeta meta;

  public ItemBuilder(final Material material) {
    item = new ItemStack(material);
    meta = item.getItemMeta();
  }

  public ItemBuilder(final ItemStack item) {
    this.item = item;
    meta = item.getItemMeta();
  }


  public ItemStack build() {
    item.setItemMeta(meta);
    return item;
  }


  public ItemBuilder amount(final int amount) {
    item.setAmount(amount);
    return this;
  }


  public ItemBuilder name(final String name) {
    meta.setDisplayName(name);
    return this;
  }


  public ItemBuilder lore(final String string) {
    final List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    lore.add(string);
    meta.setLore(lore);
    return this;
  }


  public ItemBuilder lore(final String string, final int index) {
    final List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    lore.set(index, string);
    meta.setLore(lore);
    return this;
  }


  public ItemBuilder lore(final List<String> lore) {
    final List<String> newLore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    newLore.addAll(lore);
    meta.setLore(newLore);
    return this;
  }


  public ItemBuilder setUnbreakable(final boolean unbreakable) {
    meta.setUnbreakable(unbreakable);
    return this;
  }


  public ItemBuilder setDurability(final int durability) {
    ((Damageable) meta).setDamage(durability);
    return this;
  }


  public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
    if (level <= 0) {
      meta.removeEnchant(enchantment);
    } else {
      meta.addEnchant(enchantment, level, true);
    }
    return this;
  }


  public ItemBuilder enchantment(final Enchantment enchantment) {
    meta.addEnchant(enchantment, 1, true);
    return this;
  }


  public ItemBuilder type(final Material material) {
    item.setType(material);
    return this;
  }


  public ItemBuilder clearLore() {
    meta.setLore(Lists.newArrayList());
    return this;
  }


  public ItemBuilder clearEnchantment() {
    for (final Enchantment e : item.getEnchantments().keySet()) {
      item.removeEnchantment(e);
    }
    return this;
  }


  public ItemBuilder setSkullOwner(final OfflinePlayer player) {
    Validate.isTrue(item.getType() == Material.PLAYER_HEAD, "skullOwner() only applicable for skulls!");

    final SkullMeta meta = (SkullMeta) this.meta;
    meta.setOwningPlayer(player);
    item.setItemMeta(meta);
    return this;
  }


  public ItemBuilder setItemFlag(final ItemFlag flag) {
    meta.addItemFlags(flag);
    return this;
  }

  public ItemBuilder setAttribute(final Attribute attribute, final double amount, final EquipmentSlot slot) {
    final AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "itembuilder", amount, Operation.ADD_NUMBER, slot);
    meta.addAttributeModifier(attribute, modifier);
    return this;
  }


  public ItemBuilder removeAttribute(final Attribute attribute) {
    meta.removeAttributeModifier(attribute);
    return this;
  }


  public ItemBuilder setModelData(final int data) {
    meta.setCustomModelData(data);
    return this;
  }

}
