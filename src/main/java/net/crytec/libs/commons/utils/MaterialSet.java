package net.crytec.libs.commons.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class MaterialSet implements Tag<Material> {

  private final NamespacedKey key;
  private final Set<Material> materials;

  public MaterialSet(final NamespacedKey key, final Predicate<Material> filter) {
    this(key, Stream.of(Material.values()).filter(filter).collect(Collectors.toList()));
  }

  public MaterialSet(final NamespacedKey key, final Material... materials) {
    this(key, Lists.newArrayList(materials));
  }

  public MaterialSet(final NamespacedKey key, final Collection<Material> materials) {
    this.key = key;
    this.materials = Sets.newEnumSet(materials, Material.class);
  }

  @Override
  public NamespacedKey getKey() {
    return key;
  }

  public MaterialSet add(final Tag<Material>... tags) {
    for (final Tag<Material> tag : tags) {
      add(tag.getValues());
    }
    return this;
  }

  public MaterialSet add(final MaterialSet... tags) {
    for (final Tag<Material> tag : tags) {
      add(tag.getValues());
    }
    return this;
  }

  public MaterialSet add(final Material... material) {
    materials.addAll(Lists.newArrayList(material));
    return this;
  }

  public MaterialSet add(final Collection<Material> materials) {
    this.materials.addAll(materials);
    return this;
  }

  public MaterialSet contains(final String with) {
    return add(mat -> mat.name().contains(with));
  }

  public MaterialSet endsWith(final String with) {
    return add(mat -> mat.name().endsWith(with));
  }

  public MaterialSet startsWith(final String with) {
    return add(mat -> mat.name().startsWith(with));
  }

  public MaterialSet add(final Predicate<Material> filter) {
    add(Stream.of(Material.values()).filter(((Predicate<Material>) Material::isLegacy).negate()).filter(filter).collect(Collectors.toList()));
    return this;
  }

  public MaterialSet not(final MaterialSet tags) {
    not(tags.getValues());
    return this;
  }

  public MaterialSet not(final Material... material) {
    materials.removeAll(Lists.newArrayList(material));
    return this;
  }

  public MaterialSet not(final Collection<Material> materials) {
    this.materials.removeAll(materials);
    return this;
  }

  public MaterialSet not(final Predicate<Material> filter) {
    not(Stream.of(Material.values()).filter(((Predicate<Material>) Material::isLegacy).negate()).filter(filter).collect(Collectors.toList()));
    return this;
  }

  public MaterialSet notEndsWith(final String with) {
    return not(mat -> mat.name().endsWith(with));
  }

  public MaterialSet notStartsWith(final String with) {
    return not(mat -> mat.name().startsWith(with));
  }

  @Override
  public Set<Material> getValues() {
    return materials;
  }

  public boolean isTagged(final BlockData block) {
    return isTagged(block.getMaterial());
  }

  public boolean isTagged(final BlockState block) {
    return isTagged(block.getType());
  }

  public boolean isTagged(final Block block) {
    return isTagged(block.getType());
  }

  public boolean isTagged(final ItemStack item) {
    return isTagged(item.getType());
  }

  @Override
  public boolean isTagged(final Material material) {
    return materials.contains(material);
  }

}