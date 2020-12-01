package xyz.yarinlevi.waypoints.gui.inventories;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;

public class CreateWaypointGui extends IGui {
    ItemStack basicItem = new ItemStack(Material.PAPER);

    @Override
    public void run(Player player) {
        new AnvilGUI.Builder()
                .onClose(player2 -> {                                        //called when the inventory is closing
                    player2.sendMessage("You closed the inventory.");
                })
                .onComplete((player2, text) -> {                             //called when the inventory output slot is clicked
                    if(text.equalsIgnoreCase("you")) {
                        player2.sendMessage("You have magical powers!");
                        return AnvilGUI.Response.close();
                    } else {
                        return AnvilGUI.Response.text("Incorrect.");
                    }
                })
                .preventClose()                                             //prevents the inventory from being closed
                .text("What is the meaning of life?")                       //sets the text the GUI should start with
                .itemLeft(new ItemStack(Material.IRON_SWORD))               //use a custom item for the first slot
                .itemRight(new ItemStack(Material.IRON_INGOT))              //use a custom item for the second slot
                .onLeftInputClick(player2 -> player2.sendMessage("sword"))    //called when the left input slot is clicked
                .onRightInputClick(player2 -> player2.sendMessage("ingot"))   //called when the right input slot is clicked
                .title("Enter your answer.")                                //set the title of the GUI (only works in 1.14+)
                .plugin(Waypoints.getInstance())                                   //set the plugin instance
                .open(player);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
            if (e.getRawSlot() == 0) {
                e.setCancelled(true);
            } else if (e.getRawSlot() == 1) {
                e.setCancelled(true);
            } else if (e.getRawSlot() == 2) {
                ItemStack item = e.getInventory().getItem(2);
                if (item == (basicItem)) {
                    e.getWhoClicked().sendMessage("Item is exactly the same.");
                } else {
                    e.getWhoClicked().sendMessage("Item is not the same.");
                }
            }
        }
    }
}
