package features.MusicPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import features.Computations;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static features.constant.ConstantValues.COLORS;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;
    private static EmbedBuilder builder = new EmbedBuilder();
    public PlayerManager(){

        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return  guildMusicManager;
        });
    }

    public static PlayerManager getInstance(){

        if (INSTANCE == null){
            INSTANCE = new PlayerManager();

        }
        return INSTANCE;
    }

    public EmbedBuilder sendPlayMessage(AudioTrackInfo trackInfo, String url, int queueSize, Member theUser){

        String format = "";

        if (queueSize == 0){

            format = "Now Playing";
        }else {

            format = "Added to Queue";
        }

        double milliseconds = trackInfo.length;
        int getSecond = (int)milliseconds / 1000;
        int minutes = getSecond / 60;
        int seconds = getSecond % 60;
        String songLen = String.format("%d:%d", minutes, seconds);
        builder.setTitle(format);
        builder.setDescription(trackInfo.title+"\n"+"`0:00 / "+songLen+"`"+"\nIn position #"+(queueSize+1)+"");
        builder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
        builder.setThumbnail(String.format("%s%s%s","https://img.youtube.com/vi/",url,"/mqdefault.jpg"));
        builder.setFooter("Requested by: "+ theUser.getUser().getName());

        return builder;
    }

    public void skip(MessageReceivedEvent evt){

            builder.clear();

    }

    public static void stop(MessageReceivedEvent evt){

        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(evt.getGuild());
        manager.scheduler.player.stopTrack();
        manager.scheduler.queue.clear();
        builder.clear();
        builder.setDescription("The music has been stopped and cleared.");
        builder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);

        evt.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    public void loadAndPlay(Member theUser, TextChannel channel, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);

                channel.sendMessageEmbeds(sendPlayMessage(audioTrack.getInfo(),audioTrack.getIdentifier(),
                        musicManager.scheduler.getQueueSize(), theUser).build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                trackLoaded(tracks.get(0));

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });

    }
}
