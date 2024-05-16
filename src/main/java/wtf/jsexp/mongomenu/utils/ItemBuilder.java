package wtf.jsexp.mongomenu.utils;

import org.bson.Document;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import wtf.jsexp.mongomenu.utils.compatibility.material.CompatibleMaterial;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemBuilder(String material) {
        this.itemStack = new ItemStack(Material.matchMaterial(material), 1);
    }

    public ItemBuilder(Material material, int data) {
        this.itemStack = new ItemStack(material, 1, (short)data);
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (short)data);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public Document toDocument() {
        Document document = new Document();
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            document.append("name", meta.getDisplayName());
        }

        document.append("amount", this.itemStack.getAmount());
        document.append("material", this.itemStack.getType().name());

        if (meta != null && meta.hasLore()) {
            document.append("lore", meta.getLore());
        }

        if (this.itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) this.itemStack.getItemMeta();

            Document enchantmentsDocument = new Document();
            for (Enchantment enchantment : enchMeta.getStoredEnchants().keySet()) {
                int level = enchMeta.getStoredEnchantLevel(enchantment);
                enchantmentsDocument.append(enchantment.getName(), level);
            }

            document.append("bookEnchantments", enchantmentsDocument);
        }

        Map<Enchantment, Integer> enchantments = this.itemStack.getEnchantments();
        if (!enchantments.isEmpty()) {
            Document enchantmentsDoc = new Document();
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                enchantmentsDoc.append(entry.getKey().getName(), entry.getValue());
            }
            document.append("enchantments", enchantmentsDoc);
        }

        document.append("durability", this.itemStack.getDurability());

        if (meta != null && meta.spigot().isUnbreakable()) {
            document.append("unbreakable", true);
        }

        if (meta != null && meta.hasEnchants() && meta.getEnchants().containsKey(Enchantment.ARROW_INFINITE)) {
            document.append("glow", true);
        }

        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            Color color = leatherArmorMeta.getColor();
            document.append("armor_color", String.format("%d,%d,%d", color.getRed(), color.getGreen(), color.getBlue()));
        }

        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            String owner = skullMeta.getOwner();
            if (owner != null) {
                document.append("owner", owner);
            }
        }

        return document;
    }

    public static ItemStack fromDocument(Document document) {
        ItemBuilder item = new ItemBuilder(Material.getMaterial(document.getString("material")));
        for (String property : document.keySet()) {
            Object value = document.get(property);

            if (Objects.equals(property, "name")) {
                item.name((String) value);
            }

            if (Objects.equals(property, "amount")) {
                item.amount((int) value);
            }

            if (Objects.equals(property, "lore")) {
                item.lore((List<String>) value);
            }

            if (Objects.equals(property, "bookEnchantments")) {
                Document enchants = (Document) document.get(property);
                for (String enchName : enchants.keySet()) {
                    Enchantment enchantment = Enchantment.getByName(enchName);
                    if (enchantment != null) {
                        int level = enchants.getInteger(enchName);
                        item.addBookEnchant(enchantment, level);
                    }
                }
            }

            if (Objects.equals(property, "enchantments")) {
                Document enchants = ((Document) document.get(property));
                for (String enchName : enchants.keySet()) {
                    item.enchant(
                            true,
                            Enchantment.getByName(enchName),
                            Integer.parseInt(enchants.get(enchName).toString())
                    );
                }
            }

            if (Objects.equals(property, "durability")) {
                item.data(Integer.parseInt(value.toString()));
            }

            if (Objects.equals(property, "unbreakable")) {
                item.unbreakable((boolean) value);
            }

            if (Objects.equals(property, "glow")) {
                assert value instanceof String;
                item.enchant((boolean) value);
            }

            if (Objects.equals(property, "armor_color")) {
                String[] color = value.toString().split(",");
                item.armorColor(Color.fromRGB(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
            }

            if (Objects.equals(property, "owner")) {
                item.owner((String) value);
            }
        }

        return item.build();
    }

    private void addBookEnchant(Enchantment enchant, int level) {
        if (!(itemStack.getItemMeta() instanceof EnchantmentStorageMeta)) {
            return;
        }
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
        itemMeta.addStoredEnchant(enchant, level, true);

        this.itemStack.setItemMeta(itemMeta);
    }

    public ItemBuilder name(String name) {
        if (name != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(CC.translate(name));
                this.itemStack.setItemMeta(meta);
            }
        }

        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.setLore(CC.translate(lore));
                this.itemStack.setItemMeta(meta);
            }
        }

        return this;
    }

    public ItemBuilder lore(String... lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.setLore(CC.translate(Arrays.asList(lore)));
                this.itemStack.setItemMeta(meta);
            }
        }

        return this;
    }

    public void amount(int amount) {
        this.itemStack.setAmount(amount);
    }

    public void enchant(boolean enchanted) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                this.itemStack.setItemMeta(meta);
            }
        }

    }

    public ItemBuilder enchant(boolean enchanted, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.addEnchant(Enchantment.DURABILITY, level, true);
                this.itemStack.setItemMeta(meta);
            }
        }

        return this;
    }

    public void enchant(boolean enchanted, Enchantment enchant, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                meta.addEnchant(enchant, level, true);
                this.itemStack.setItemMeta(meta);
            }
        }

    }

    public void unbreakable(boolean unbreakable) {
        if (unbreakable) {
            ItemMeta meta = this.itemStack.getItemMeta();
            if (meta != null) {
                try {
                    meta.spigot().setUnbreakable(true);
                    this.itemStack.setItemMeta(meta);
                } catch (NoSuchMethodError var4) {
                    this.enchant(true, Enchantment.DURABILITY, 100);
                }
            }
        }

    }

    public ItemBuilder data(int dur) {
        this.itemStack.setDurability((short)dur);
        return this;
    }

    public ItemBuilder owner(String owner) {
        if (this.itemStack.getType() == CompatibleMaterial.HUMAN_SKULL.getMaterial()) {
            SkullMeta meta = (SkullMeta)this.itemStack.getItemMeta();
            if (meta != null) {
                meta.setOwner(owner);
                this.itemStack.setItemMeta(meta);
            }

            return this;
        } else {
            throw new IllegalArgumentException("setOwner() only applicable for Skull Item");
        }
    }

    public void armorColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.itemStack.getItemMeta();
        if (leatherArmorMeta != null) {
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        } else {
            throw new IllegalArgumentException("setColor() only applicable for LeatherArmor");
        }
    }

    public static ItemStack createSkull(String owner, String displayName, List<String> lore) {
        return (new ItemBuilder(CompatibleMaterial.HUMAN_SKULL.getMaterial())).data(3).owner(owner).name(displayName).lore(lore).build();
    }

    public static String getDisplayName(ItemStack itemStack) {
        return itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : CC.toReadable(itemStack.getType());
    }

    public ItemStack build() {
        return this.itemStack;
    }
}