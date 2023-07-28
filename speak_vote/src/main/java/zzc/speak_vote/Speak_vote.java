// ______  _   _   _____   _   _   ______  _   _   _   _____   _   _       ___   _____
//|___  / | | | | /  _  \ | | | | |___  / | | | | | | /  ___| | | | |     /   | /  _  \
//   / /  | |_| | | | | | | | | |    / /  | |_| | | | | |     | |_| |    / /| | | | | |
//  / /   |  _  | | | | | | | | |   / /   |  _  | | | | |     |  _  |   / / | | | | | |
// / /__  | | | | | |_| | | |_| |  / /__  | | | | | | | |___  | | | |  / /  | | | |_| |
///_____| |_| |_| \_____/ \_____/ /_____| |_| |_| |_| \_____| |_| |_| /_/   |_| \_____/
//
//ZHICHAO出品必属精品

package zzc.speak_vote;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Timer;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;





public class Speak_vote extends JavaPlugin{

    public static final Logger log = Logger.getLogger("Minecraft");


    //投票某件事
    //String[]只适用于固定长度
    public static HashMap<String,String[]> vote_option = new HashMap<String,String[]>();
    public static HashMap<String,List<String>> support_name_map = new HashMap<String,List<String>>();//给某件事用的
    public static HashMap<String,List<String>> unsupport_name_map = new HashMap<String,List<String>>();//给某件事用的

    //投票某个人
    public static HashMap<String,String[]> ad_vote_option = new HashMap<String,String[]>();
    public static HashMap<String,List<String>> ad_support_name_map = new HashMap<String,List<String>>();//给个人用的,记名制投票
    public static HashMap<String,List<String>> ad_unsupport_name_map = new HashMap<String,List<String>>();//给个人用的,记名制投票
    public static FileConfiguration config;



    // 创建定时器任务

    @Override
    public void onEnable(){
        createConfig();

        loadConfig();

        System.out.println("§6Speak vote loading...");

        List<String>  vote_list = config.getStringList("vote_something");
        for (String entry : vote_list) {
            String[] data = entry.split(";");
            String activate_str = data[0];
            vote_option.put(activate_str,data);
            support_name_map.put(activate_str,new ArrayList<>());
            unsupport_name_map.put(activate_str,new ArrayList<>());
            log.info("§6注册投票事件：§f"+activate_str);
        }

        List<String>  adv_vote_list = config.getStringList("vote_somebody");
        for (String entry : adv_vote_list) {
            String[] data = entry.split(";");
            String activate_str = data[0];
            ad_vote_option.put(activate_str,data);
            log.info("§6注册高级投票事件：§f"+activate_str);
        }

        Bukkit.getPluginManager().registerEvents(new listen_player(), this);
        CommandSender sender = Bukkit.getConsoleSender();
//        this.client.runTaskTimer(this, 0L, 1200L);



    }

    private void createConfig() {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();

        // 如果插件数据文件夹不存在，创建它
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        // 获取配置文件对象
        File configFile = new File(dataFolder, "config.yml");

        // 如果配置文件不存在，从资源文件中复制默认配置
        if (!configFile.exists()) {
            try (InputStream inputStream = getResource("config.yml")) {
                Files.copy(inputStream, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfig() {
        // 加载config.yml文件
        config = getConfig();
        // 保存默认的config.yml文件到插件数据文件夹中（如果config.yml不存在）
        saveDefaultConfig();
    }


}
