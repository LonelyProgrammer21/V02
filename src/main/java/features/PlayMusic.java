package features;

import features.musicplayer.PlayerManager;
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
    private static boolean isJoined = true;


    public static void getCommand(@NotNull String command, MessageReceivedEvent guildEvent, ArrayList<String> tokens){
        event = guildEvent;
        PlayerManager.event = guildEvent;
        initValues();
        StringBuilder title = new StringBuilder();
        for (String data: tokens) {
            title.append(" ").append(data);
        }
        title = new StringBuilder(title.toString().trim());
        switch (command.toLowerCase()) {
            case "join" -> joinCommand();
            case "play" -> playCommand(title.toString());
            case "stop" -> stopCommand();
            case "skip" -> skipCommand();
        }
    }

    private static void initValues() {

        channel = event.getTextChannel();
        self = event.getGuild().getSelfMember();
        selfVoiceState = self.getVoiceState();
        theUser = event.getMember();
        //noinspection ConstantConditions
        memberVoiceState = theUser.getVoiceState();
        audioManager = event.getGuild().getAudioManager();
        memberChannel = memberVoiceState.getChannel();
    }

    private static boolean canConnect(){

        if(selfVoiceState.inAudioChannel() && !isJoined){

            channel.sendMessage("I'm already in a voice channel.").queue();
            return false;
        }
     return isMemberOnVoiceChannel();
    }

    private static boolean isMemberOnVoiceChannel(){

        if(!memberVoiceState.inAudioChannel()){

            channel.sendMessage("Please join to a voice channel to make this command work.").queue();
            return false;
        }
        return canAccessVoiceChannel();
    }

    private static boolean canAccessVoiceChannel(){

        //noinspection ConstantConditions
        if(!self.hasPermission(memberVoiceState.getChannel(), Permission.VOICE_CONNECT)){

            channel.sendMessage("I dont have permission to connect to your voice channel").queue();
            return false;
        }
        return true;
    }
    private static void joinCommand(){

        if (canConnect()){

            audioManager.openAudioConnection(memberChannel);
            if(!isJoined)
                channel.sendMessageFormat("Connecting to \uD83D\uDD0A %s ", memberChannel.getName()).queue();
        }
        isJoined = false;
    }

    private static void playCommand(String url){
        isJoined = true;
        if (isMemberOnVoiceChannel()){
            if(isJoined){
                if(!selfVoiceState.inAudioChannel() || isMemberOnVoiceChannel()){
                    joinCommand();
                }else {
                    return;
                }
            }

            if(!isUrl(url)){

                url = "ytsearch:" + url;
            }
            System.out.println(url);
            PlayerManager.getInstance().loadAndPlay(theUser,channel, url);
        }
        isJoined = false;

    }

    private static boolean checkStates(){

        if(!selfVoiceState.inAudioChannel()){

            event.getChannel().sendMessage("I am not in voice channel.").queue();
            return false;
        }
        if(isMemberOnVoiceChannel()){

            //noinspection ConstantConditions
            if(!selfVoiceState.getChannel().equals(memberVoiceState.getChannel())){

                event.getChannel().sendMessage("Please join to the channel where i was joined to make this work.").queue();
                return false;
            }

        }
        return true;
    }

    private static void stopCommand(){

        if(checkStates())
        PlayerManager.stop(event);
    }

    private static void skipCommand(){

        if (checkStates())
            PlayerManager.skip(event);
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
