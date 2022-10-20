package me.spartacus04.jext

import me.spartacus04.jext.command.CommandsRegistrant
import me.spartacus04.jext.config.ConfigData.Companion.CONFIG
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.ConfigManager
import me.spartacus04.jext.config.LanguageManager
import me.spartacus04.jext.listener.ListenersRegistrant
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Jext : JavaPlugin() {
    override fun onEnable() {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
        }
        ENABLED_MESSAGE.send()
    }

    override fun onDisable() {
        DISABLED_MESSAGE.send()
    }

    private fun load() {
        // Registers VERSION
        SpigotVersion.load(this)

        // Registers namespaces
        JextNamespace.registerNamespace(this)

        // Loads configuration
        ConfigManager.load(this)

        // Loads languages
        LANG = LanguageManager(CONFIG.LANGUAGE_MODE.lowercase() == "auto", this)

        // Setups commands
        CommandsRegistrant.registerCommands(this)

        // Registers listeners
        ListenersRegistrant.registerListeners(this)

        Updater(this, 103219).getVersion {
            if(it != description.version) {
                Bukkit.getConsoleSender().sendMessage(
                    "[§aJEXT§f] A new update is available!"
                )
            }
        }

        // Start metrics if allowed
        if(!CONFIG.ALLOW_METRICS) return

        Metrics(this, 16571)
    }

    companion object {
        private val ENABLED_MESSAGE = Log().okay().t("Enabled Jukebox Extender Reborn, Do Re Mi!")
        private val DISABLED_MESSAGE = Log().warn().t("Disabled Jukebox Extender Reborn, Mi Re Do!")
    }
}