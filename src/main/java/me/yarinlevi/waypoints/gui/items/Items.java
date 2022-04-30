package me.yarinlevi.waypoints.gui.items;

import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author YarinQuapi
 **/
public class Items {
    public static final ItemStack ITEM_MENU;
    public static final ItemStack ITEM_NEXT;
    public static final ItemStack ITEM_PREVIOUS;

    public static final int ITEM_MENU_SLOT;
    public static final int ITEM_NEXT_SLOT;
    public static final int ITEM_PREVIOUS_SLOT;

    static {
        ITEM_MENU = new ItemStack(Material.NETHER_STAR, 1);
        ITEM_NEXT = new ItemStack(Material.LIME_WOOL, 1);
        ITEM_PREVIOUS = new ItemStack(Material.RED_WOOL, 1);

        ItemMeta itemMenuMeta = ITEM_MENU.getItemMeta();
        itemMenuMeta.setDisplayName(MessagesUtils.getMessageFromData("gui.items.menu.title"));
        ITEM_MENU.setItemMeta(itemMenuMeta);

        ItemMeta itemNextMeta = ITEM_NEXT.getItemMeta();
        itemNextMeta.setDisplayName(MessagesUtils.getMessageFromData("gui.items.next.title"));
        ITEM_NEXT.setItemMeta(itemNextMeta);

        ItemMeta itemPreviousMeta = ITEM_PREVIOUS.getItemMeta();
        itemPreviousMeta.setDisplayName(MessagesUtils.getMessageFromData("gui.items.previous.title"));
        ITEM_PREVIOUS.setItemMeta(itemPreviousMeta);

        ITEM_MENU_SLOT = MessagesUtils.getInt("gui.items.menu.slot")-1;
        ITEM_NEXT_SLOT = MessagesUtils.getInt("gui.items.next.slot")-1;
        ITEM_PREVIOUS_SLOT = MessagesUtils.getInt("gui.items.previous.slot")-1;
    }
}
