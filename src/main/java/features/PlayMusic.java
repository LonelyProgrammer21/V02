package features;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayMusic {

    static ArrayList<String> textInput = new ArrayList<>();
    public static MessageReceivedEvent event;

    public static void getCommand(String command){

        switch (command) {
            case "join" -> joinCommand();
            case "play" -> playCommand();
        }
    }
    private static void playCommand(){


    }
    private static void joinCommand(){

        final TextChannel channel = event.getTextChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member theUser = event.getMember();
        final GuildVoiceState memberVoiceState = theUser.getVoiceState();

        if(selfVoiceState.inAudioChannel()){

            channel.sendMessage("I'm already in a voice channel.").queue();
            return;
        }

        if(!memberVoiceState.inAudioChannel()){

            channel.sendMessage("Please join to a voice member to make this command work.").queue();
            return;
        }

        if(!self.hasPermission(memberVoiceState.getChannel(), Permission.VOICE_CONNECT)){

            channel.sendMessage("I dont have permission to connect to your voice channel").queue();
            return;
        }

        final AudioManager audioManager = event.getGuild().getAudioManager();
        final AudioChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("Connecting to \uD83D\uDD0A %s ", memberChannel.getName()).queue();

    }

}
