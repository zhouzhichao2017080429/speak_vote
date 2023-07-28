// ______  _   _   _____   _   _   ______  _   _   _   _____   _   _       ___   _____
//|___  / | | | | /  _  \ | | | | |___  / | | | | | | /  ___| | | | |     /   | /  _  \
//   / /  | |_| | | | | | | | | |    / /  | |_| | | | | |     | |_| |    / /| | | | | |
//  / /   |  _  | | | | | | | | |   / /   |  _  | | | | |     |  _  |   / / | | | | | |
// / /__  | | | | | |_| | | |_| |  / /__  | | | | | | | |___  | | | |  / /  | | | |_| |
///_____| |_| |_| \_____/ \_____/ /_____| |_| |_| |_| \_____| |_| |_| /_/   |_| \_____/
//
//ZHICHAO出品必属精品

package zzc.speak_vote;

import java.util.*;
import java.text.SimpleDateFormat;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class listen_player implements Listener {

    Timer timer = new Timer();

    Player player;
    CommandSender sender;
    String my_message;
    String something_content;
    String ad_key_str;
    String playername;
    String save_vote_paper;




    public void support_something(){
        Speak_vote.log.info("§6support_something");
        String[] a = Speak_vote.vote_option.get(something_content);
        int threshold = Integer.parseInt(a[a.length-2]);

        List<String> support_names = Speak_vote.support_name_map.get(something_content);
        List<String> unsupport_names = Speak_vote.unsupport_name_map.get(something_content);
//        Speak_vote.log.info("§6support_names：§f"+support_names);
//        Speak_vote.log.info("§6unsupport_names：§f"+unsupport_names);
        if(support_names.contains(playername) || unsupport_names.contains(playername)){return;}
        support_names.add(playername);
        int count = support_names.size()-unsupport_names.size();
//        Speak_vote.log.info("§6support_names：§f"+support_names);
//        Speak_vote.log.info("§6unsupport_names：§f"+unsupport_names);
        Speak_vote.support_name_map.put(something_content,support_names);
        String brod = "§6"+String.join(", ", support_names) + "请求"+something_content+"  "+count+"/"+threshold;

        if(count>=threshold){
            brod = "§6"+String.join(", ", support_names) + a[2];
            sender.getServer().dispatchCommand(sender, a[1]);
            List<String> empty_names = new ArrayList<>();
            Speak_vote.support_name_map.put(something_content,empty_names);
            Speak_vote.unsupport_name_map.put(something_content,empty_names);
        }
        sender.getServer().broadcastMessage(brod);
    }

    public void unsupport_something(){
        Speak_vote.log.info("§6unsupport_something");
        String[] a = Speak_vote.vote_option.get(something_content);
        int threshold = Integer.parseInt(a[a.length-2]);

        List<String> support_names = Speak_vote.support_name_map.get(something_content);
        List<String> unsupport_names = Speak_vote.unsupport_name_map.get(something_content);
        if(support_names.contains(playername) || unsupport_names.contains(playername)){return;}
        unsupport_names.add(playername);
        Speak_vote.unsupport_name_map.put(something_content,unsupport_names);
        int count = support_names.size()-unsupport_names.size();
        String brod = "§6"+String.join(", ", unsupport_names) + "反对"+something_content+"  "+count+"/"+threshold;
        sender.getServer().broadcastMessage(brod);
    }

    public int get_diff(List<String> already_get_supportvote, List<String> already_get_unsupportvote,String ad_key_str){
        int support_count = count_of_ListString(already_get_supportvote,ad_key_str);
        int unsupport_count = count_of_ListString(already_get_unsupportvote,ad_key_str);
        return support_count-unsupport_count;
    }

    public void support_somebody(){
        Speak_vote.log.info("§6support_somebody");
        String target_player_name = my_message.substring(ad_key_str.length(),my_message.length()).trim();
        List<String> already_get_supportvote = new ArrayList<>();
        List<String> already_get_unsupportvote = new ArrayList<>();

        if(Speak_vote.ad_support_name_map.containsKey(target_player_name)){
            already_get_supportvote = Speak_vote.ad_support_name_map.get(target_player_name);
        }
        if(Speak_vote.ad_unsupport_name_map.containsKey(target_player_name)){
            already_get_unsupportvote = Speak_vote.ad_unsupport_name_map.get(target_player_name);
        }
        if(already_get_supportvote.contains(save_vote_paper) || already_get_unsupportvote.contains(save_vote_paper)){return;}
        already_get_supportvote.add(save_vote_paper);
        Speak_vote.ad_support_name_map.put(target_player_name,already_get_supportvote);
//        Speak_vote.log.info("§6already_get_supportvote：§f"+already_get_supportvote);
//        Speak_vote.log.info("§6already_get_unsupportvote：§f"+already_get_unsupportvote);


        String[] b = Speak_vote.ad_vote_option.get(ad_key_str);
        int threshold = Integer.parseInt(b[b.length-2]);
        int diff = get_diff(already_get_supportvote,already_get_unsupportvote,ad_key_str);
        String all_name = namelist_of_ListString(already_get_supportvote,ad_key_str);
        String brod = "§6"+all_name + "请求"+ad_key_str+ target_player_name +"  "+diff+"/"+threshold;

        if(diff>threshold){
            brod = "§6"+all_name + ad_key_str+ target_player_name+"  "+diff+"/"+threshold;
            already_get_supportvote = removet_of_ListString(already_get_supportvote,ad_key_str);
            already_get_unsupportvote = removet_of_ListString(already_get_unsupportvote,ad_key_str);
            Speak_vote.ad_support_name_map.put(target_player_name,already_get_supportvote);
            Speak_vote.ad_unsupport_name_map.put(target_player_name,already_get_unsupportvote);
        }
        sender.getServer().broadcastMessage(brod);
    }

    public void unsupport_somebody(){
        Speak_vote.log.info("§6unsupport_somebody");
        String target_player_name = my_message.substring(2+ad_key_str.length(),my_message.length()).trim();
        List<String> already_get_supportvote = new ArrayList<>();
        List<String> already_get_unsupportvote = new ArrayList<>();

        if(Speak_vote.ad_unsupport_name_map.containsKey(target_player_name)){
            already_get_unsupportvote = Speak_vote.ad_unsupport_name_map.get(target_player_name);
        }
        if(Speak_vote.ad_support_name_map.containsKey(target_player_name)){
            already_get_supportvote = Speak_vote.ad_support_name_map.get(target_player_name);
        }
        if(already_get_supportvote.contains(save_vote_paper) || already_get_unsupportvote.contains(save_vote_paper)){return;}
        already_get_unsupportvote.add(save_vote_paper);
        Speak_vote.ad_unsupport_name_map.put(target_player_name,already_get_unsupportvote);



        String[] b = Speak_vote.ad_vote_option.get(ad_key_str);
        int threshold = Integer.parseInt(b[b.length-2]);
        int diff = get_diff(already_get_supportvote,already_get_unsupportvote,ad_key_str);

        String all_name = namelist_of_ListString(already_get_unsupportvote,ad_key_str);
        String brod = "§6"+all_name + "反对"+ad_key_str+ target_player_name +"  "+diff+"/"+threshold;


        sender.getServer().broadcastMessage(brod);
    }

    @EventHandler
    public void playchat(AsyncPlayerChatEvent event){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                player = event.getPlayer();
                playername = player.getName();
                sender = Bukkit.getConsoleSender();
                my_message = event.getMessage();
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                System.out.println(dateFormat.format(date));

                if(my_message.contains("发包")){
                    sender.getServer().broadcastMessage("§6未来1周服务器将对玩家:"+playername+"的行为进行智能监视...");
                    return;
                }

                if(my_message.contains("傻")||my_message.contains("草")||my_message.contains("狗")||my_message.contains("sb")||my_message.contains("滚")||my_message.contains("你妈死了")){
                    sender.getServer().dispatchCommand(sender, "shock "+playername);
                }

                something_content = my_message.substring(2, my_message.length());

                // 支持某事，反对某事，支持某人，反对某人代码完成的标志是：
                // 变量更新成功、广播成功 及 判断与置零成功
                if(Speak_vote.vote_option.containsKey(something_content)){
                    if(my_message.startsWith("请求")){support_something();}
                    if(my_message.startsWith("反对")){unsupport_something();}
                }

                for(String s: Speak_vote.ad_vote_option.keySet()){
                    ad_key_str = s;
                    save_vote_paper = ad_key_str+";"+playername;
                    if(my_message.startsWith(ad_key_str)){support_somebody(); }
                    if(my_message.startsWith("反对"+ad_key_str)){unsupport_somebody();}
                }

                switch (my_message){
                    case "r":
                    case "R":
                        PlayerInventory inventory = player.getInventory();//获得玩家的库存对象·
                        ItemStack[] armorContents = inventory.getContents();//获取库存中的所有物品
                        int nums = 0;
                        for (ItemStack armorContent : armorContents) {//遍历库存里面的所有物品
                            if(armorContent.getType().equals(Material.ROTTEN_FLESH)){//进行物品匹配，如果物品为腐肉则删除
                                nums = armorContent.getAmount();
                                player.sendMessage("§6"+nums);
                                player.sendMessage("§6服务器已收购您的" + nums + "块腐肉~");
                                sender.getServer().dispatchCommand(sender, "eco give " + playername + " " + nums);
                                inventory.remove(armorContent);//删除物品
                            }
                        }
                        player.sendMessage("§6"+nums);
                        if(nums==0){
                            player.sendMessage("§6您的身上没有垃圾哟~");
                        }

                }
            }
        };

        timer.schedule(timerTask, 1000);

    }

    // 计算目标子字符串在原始字符串中出现的次数
    private static int count_of_ListString(List<String> original, String target) {
        int count = 0;
        for(String s: original){
            if(s.contains(target)){count = count + 1;}
        }
        return count;
    }

    private static List<String> removet_of_ListString(List<String> original, String target) {
        for(String s: original){
            if(s.contains(target)){original.remove(s);}
        }
        return original;
    }

    private static String namelist_of_ListString(List<String> original, String target) {
        String all_name = "";
        for(String s: original){
            if(s.contains(target)){all_name = all_name + " " + s.split(";")[1];}
        }
        return all_name;
    }

}
