package me.yarinlevi.waypoints.external;

import lombok.Getter;
import me.yarinlevi.waypoints.exceptions.ExtensionLoadingErrorException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

/**
 * @author YarinQuapi
 * Powered by VaultAPI
 **/
public class EconomyExtension {
    @Getter private final Economy economy;

    public EconomyExtension() throws ExtensionLoadingErrorException {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new ExtensionLoadingErrorException("VaultAPI was not found!");
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new ExtensionLoadingErrorException("No economy plugin installed! please install an economy plugin that supports VaultAPI!");
        }

        economy = rsp.getProvider();
    }
}
