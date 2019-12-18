package net.crytec.libs.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * This class offers methods for inventories and/or itemstacks.
 *
 * @author crysis992
 */
public class UtilInv {

  /**
   * Insert an ItemStack to the players inventory. Also plays a sound for pickup.
   *
   * @param player
   * @param stack
   */
  public static void insert(final Player player, final ItemStack stack) {
    insert(player, stack, false);
  }

  /**
   * Insert an ItemStack to the players inventory. Also plays a sound for pickup.
   *
   * @param player - The Player
   * @param stack  - The Itemstack to insert
   * @param drop   - Drop the item on the ground if the inventory is full.
   */
  public static void insert(final Player player, final ItemStack stack, final boolean drop) {
    insert(player, stack, drop, true);
  }

  /**
   * Insert an ItemStack to the players inventory. Also plays a sound for pickup.
   *
   * @param player    - The Player
   * @param stack     - The Itemstack to insert
   * @param playsound - play pickup sound
   * @param drop      - Drop the item on the ground if the inventory is full.
   */
  public static void insert(final Player player, final ItemStack stack, final boolean drop, final boolean playsound) {
    final HashMap<Integer, ItemStack> items = player.getInventory().addItem(stack);

    if (items.size() == 0 && playsound) {
      UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP);
    } else {
      items.values().forEach(itemLeft -> player.getWorld().dropItemNaturally(player.getLocation(), itemLeft));
      if (playsound) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.8F, 1);
      }
    }
  }

  /**
   * Insert an ItemStack to the players inventory. Also plays a sound for pickup.
   *
   * @param stack - The Itemstack to insert
   * @param drop  - Drop the item on the ground if the inventory is full.
   */
  public static void insert(final Inventory inventory, final ItemStack stack, final boolean drop, final Location dropLocation) {
    inventory.addItem(stack).values().forEach(itemLeft -> {
      if (drop) {
        dropLocation.getWorld().dropItemNaturally(dropLocation, itemLeft);
      }
    });
  }


  public static void insertAmount(final Inventory inventory, final ItemStack item, final int amount, final boolean dropExcess, final Location dropLocation) {

    final int stackAmount = item.getAmount();
    final int stackSize = item.getMaxStackSize();
    final int stacks = (int) ((double) (amount * stackAmount) / (double) stackSize);
    final int left = (int) ((double) (amount * stackAmount) % (double) stackSize);

    final ItemStack singleItem = item.clone();
    singleItem.setAmount(1);

    if (stacks != 0) {
      singleItem.setAmount(stackSize);
      IntStream.range(0, stacks).forEach(i -> {
        UtilInv.insert(inventory, singleItem.clone(), dropExcess, dropLocation);
      });
    }

    if (left != 0) {
      singleItem.setAmount(left);
      UtilInv.insert(inventory, singleItem.clone(), dropExcess, dropLocation);
    }
  }


  /**
   * Check if the inventory is empty
   *
   * @param inv The Inventory to check
   * @return -
   */
  public static boolean isEmpty(final Inventory inv) {
    return Arrays.stream(inv.getContents()).anyMatch(item -> item != null);
  }

  /**
   * Check if the players Inventory contains at least a required amount of a specific material
   *
   * @param player
   * @param item     - The Material type
   * @param required - The required amount
   * @return -
   */
  @Deprecated
  public static boolean contains(final Player player, final Material item, final int required) {
    return player.getInventory().contains(item, required);
  }

  public static boolean has(final Inventory inv, final ItemStack item, final int amount) {
    final int fullAmount = amount * item.getAmount();

    return fullAmount <= IntStream.range(0, 36).mapToObj(i -> inv.getItem(i)).filter(i -> i != null && i.isSimilar(item)).mapToInt(i -> i.getAmount()).sum();
  }


  public static int remove(final Inventory inv, final ItemStack item, final int amount) {

    final int fullAmount = amount * item.getAmount();

    ItemStack current;
    int currentAmount, left = fullAmount;

    for (int i = 0; i < inv.getSize(); i++) {
      current = inv.getItem(i);
      if (current == null || !current.isSimilar(item)) {
        continue;
      }
      currentAmount = current.getAmount();
      if (currentAmount < left) {
        left -= currentAmount;
        inv.clear(i);
        continue;
      } else {
        current.setAmount(current.getAmount() - left);
        return 0;
      }
    }

    return left;
  }


  /**
   * Drop the inventory content of a Inventory onto the ground
   *
   * @param dropLocation
   * @param content
   */
  public static void drop(final Location dropLocation, final ItemStack[] content) {
    drop(dropLocation, content, 0);
  }

  /**
   * Drop the inventory content of a Inventory onto the ground
   *
   * @param dropLocation
   * @param content
   */
  public static void drop(final Location dropLocation, final ItemStack[] content, final int pickupDelay) {

    final World world = dropLocation.getWorld();

    for (final ItemStack item : content) {
      if (item == null || item.getType() == Material.AIR) {
        continue;
      }
      final Item drop = world.dropItemNaturally(dropLocation, item);
      if (pickupDelay > 0) {
        drop.setPickupDelay(pickupDelay);
      }
    }
  }


  /**
   * Removes all Items with the given Material Type
   *
   * @param player
   * @param type
   * @return -
   */
  public static int removeAll(final Player player, final Material type) {
    final HashSet<ItemStack> remove = new HashSet<>();
    int count = 0;

    for (final ItemStack item : player.getInventory().getContents()) {
      if ((item != null) && (item.getType() == type)) {
        count += item.getAmount();
        remove.add(item);
      }
    }
    for (final ItemStack item : remove) {
      player.getInventory().remove(item);
    }
    return count;
  }

  /**
   * Clear the players inventory. Optional with armor slots
   *
   * @param player
   * @param clearArmor
   */
  public static void clear(final Player player, final boolean clearArmor) {
    for (int j = 0; j < player.getInventory().getSize(); j++) {
      player.getInventory().clear(j);
    }

    if (clearArmor) {
      player.getInventory().setArmorContents(new ItemStack[0]);
    }
    player.updateInventory();
  }

  public static boolean hasSpot(final Player player) {
    final int items = player.getInventory().firstEmpty();
    if (items == -1) {
      return false;
    }
    return true;
  }

  public static boolean hasSpot(final Inventory inventory) {
    final int items = inventory.firstEmpty();
    if (items == -1) {
      return false;
    }
    return true;
  }

  /**
   * Checks if the inventory contains a specific amount of the given ItemStack.
   *
   * @param item
   * @param amount
   * @return
   */
  public static boolean hasItems(final Inventory inventory, final ItemStack item, final int amount) {

    final int fullAmount = amount * item.getAmount();

    return fullAmount <= IntStream.range(0, inventory.getSize())
        .mapToObj(i -> inventory.getItem(i))
        .filter(i -> i != null && i.isSimilar(item))
        .mapToInt(i -> i.getAmount())
        .sum();

  }

  /**
   * Remove a specific amount of items of the given type from an Inventory
   *
   * @param inventory
   * @param mat
   * @param quantity
   * @return -
   */
  public static int removeItem(final Inventory inventory, final Material mat, final int quantity) {
    ItemStack current;
    int currentAmount, left = quantity;

    for (int i = 0; i < inventory.getSize(); i++) {
      current = inventory.getItem(i);
      if (current == null || !current.getType().equals(mat)) {
        continue;
      }
      currentAmount = current.getAmount();
      if (currentAmount < left) {
        left -= currentAmount;
        inventory.clear(i);
        continue;
      } else {
        current.setAmount(current.getAmount() - left);
        return 0;
      }
    }

    return left;
  }

  public static int removeItem(final Inventory inventory, final ItemStack item, final int amount) {

    final int fullAmount = amount * item.getAmount();

    ItemStack current;
    int currentAmount, left = fullAmount;

    for (int i = 0; i < inventory.getSize(); i++) {
      current = inventory.getItem(i);
      if (current == null || !current.isSimilar(item)) {
        continue;
      }
      currentAmount = current.getAmount();
      if (currentAmount < left) {
        left -= currentAmount;
        inventory.clear(i);
        continue;
      } else {
        current.setAmount(current.getAmount() - left);
        return 0;
      }
    }

    return left;
  }

  /**
   * Checks if the given item stack is null or its type is {@link Material#AIR}.
   *
   * @param item an item stack
   * @return {@code true} if it is "null". Otherwise is {@code false}.
   */
  public static boolean isNull(@Nullable final ItemStack item) {
    return item == null || item.getType() == Material.AIR || item.getType().toString().endsWith("_AIR");
  }

  /**
   * Checks if the given material is null or is air.
   *
   * @param material material
   * @return {@code true} if it is "null". Otherwise is {@code false}.
   */
  public static boolean isNull(@Nullable final Material material) {
    return material == null || material == Material.AIR || material.toString().endsWith("_AIR");
  }

  /**
   * Compares two given item stacks.
   *
   * @param a the first item stack
   * @param b the second item stack
   * @return {@code true} if they are equal. Otherwise is {@code false}.
   */
  public static boolean compare(@Nullable final ItemStack a, @Nullable final ItemStack b) {
    if (isNull(a)) {
      return isNull(b);
    } else if (isNull(b)) {
      return isNull(a);
    }
    return a.equals(b);
  }

  /**
   * Compares two given array of item stacks.
   *
   * @param a the first array
   * @param b the second array
   * @return {@code true} if they are equal. Otherwise is {@code false}.
   */
  public static boolean compare(@Nullable final ItemStack[] a, @Nullable final ItemStack[] b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    if (a.length != b.length) {
      return false;
    }
    int i = 0;
    while (i < a.length) {
      if (!compare(a[i], b[i])) {
        return false;
      }
      i++;
    }
    return true;
  }

  /**
   * Serializes an ItemStack array to a Base64 encoded String
   *
   * @param items
   * @return -
   */
  public static String itemStackArrayToBase64(final ItemStack[] items) {
    try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);) {

      // Write the size of the inventory
      dataOutput.writeInt(items.length);

      // Save every element in the list
      for (int i = 0; i < items.length; i++) {
        dataOutput.writeObject(items[i]);
      }
      // Serialize that array
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (final IOException e) {
      Bukkit.getLogger().severe("Failed to serialize ItemStack array to Base64");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Deserializes an Base64 String to an ItemStack array.
   *
   * @param data
   * @return -
   */
  public static ItemStack[] itemStackArrayFromBase64(final String data) {
    try (final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(data)));) {

      final ItemStack[] items = new ItemStack[dataInput.readInt()];

      // Read the serialized inventory
      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) dataInput.readObject();
      }
      return items;
    } catch (final IOException | ClassNotFoundException ex) {
      Bukkit.getLogger().severe("Failed to deserialize Base64");
      ex.printStackTrace();
      return null;
    }
  }
}