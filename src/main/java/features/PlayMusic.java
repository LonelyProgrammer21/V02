package features;

import features.MusicPlayer.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlayMusic {

    public static MessageReceivedEvent event;
    private static TextChannel channel;
    private static Member self;
    private static GuildVoiceState selfVoiceState;
    private static Member theUser;
    private static GuildVoiceState memberVoiceState;
    private static AudioManager audioManager;
    private static  AudioChannel memberChannel;


    public static void getCommand(@NotNull String command, MessageReceivedEvent guildEvent, ArrayList<String> tokens){
        event = guildEvent;
        initValues();
        String title = "";
        for (String data: tokens) {
            title += " " + data;
        }
        title = title.trim();
        switch (command) {
            case "join" -> joinCommand();
            case "play" -> playCommand(title);
        }
    }

    private static void initValues() {

        channel = event.getTextChannel();
        self = event.getGuild().getSelfMember();
        selfVoiceState = self.getVoiceState();
        theUser = event.getMember();
        memberVoiceState = theUser.getVoiceState();
        audioManager = event.getGuild().getAudioManager();
        memberChannel = memberVoiceState.getChannel();
    }

    private static boolean canConnect(){

        if(selfVoiceState.inAudioChannel()){

            channel.sendMessage("I'm already in a voice channel.").queue();
            return false;
        }
     return isMemberOnVoiceChannel();
    }

    private static boolean isMemberOnVoiceChannel(){

        if(!memberVoiceState.inAudioChannel()){

            channel.sendMessage("Please join to a voice member to make this command work.").queue();
            return false;
        }
        return canAccessVoiceChannel();
    }

    private static boolean canAccessVoiceChannel(){

        if(!self.hasPermission(memberVoiceState.getChannel(), Permission.VOICE_CONNECT)){

            channel.sendMessage("I dont have permission to connect to your voice channel").queue();
            return false;
        }
        return true;
    }
    private static void joinCommand(){

        if (canConnect()){
            audioManager.openAudioConnection(memberChannel);
            channel.sendMessageFormat("Connecting to \uD83D\uDD0A %s ", memberChannel.getName()).queue();
        }
    }

    private static void playCommand(String url){

        boolean autoJoin = true;
        if (isMemberOnVoiceChannel()){
            if(autoJoin){
                if(!selfVoiceState.inAudioChannel() && isMemberOnVoiceChannel()){
                    joinCommand();
                }
            }

            if(!isUrl(url)){

                url = "ytsearch:" + url;
            }
            System.out.println(url);
            PlayerManager.getInstance().loadAndPlay(theUser,channel, url);
        }

    }

    private static boolean isUrl(String url){

        try {
            new URL(url);
            return true;
        }catch (MalformedURLException e){
            return false;
        }
    }

}
