package features;

import features.constant.ConstantValues;
import features.constant.CredentialRetriever;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Fun {

    private static final EmbedBuilder embedBuilder = new EmbedBuilder();
    private static final GregorianCalendar time = new GregorianCalendar();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM:dd:yyyy");
    private static final String VO1 = CredentialRetriever.V01;

    public static EmbedBuilder makeFunTemplate(@NotNull String type, Member v, List<Member> memberList){

        Member mentioned = null;
        if(!memberList.isEmpty()){

            mentioned = memberList.get(0);
        }
        embedBuilder.clear();
        int lastindex;

            switch (type){

                case "blood"->{

                    if(v.getId().equalsIgnoreCase(VO1)){

                        lastindex = Computations.generateIndex(ConstantValues.ANGRYEMOTE.length-1);

                        embedBuilder.setTitle(ConstantValues.ANGRYEMOTE[lastindex]);
                        lastindex = Computations.generateIndex(ConstantValues.BLOODGIFS.length-1);

                        embedBuilder.setImage(ConstantValues.BLOODGIFS[lastindex]);
                        embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                    }else {

                        embedBuilder.setTitle("Insufficient Permission.");
                        embedBuilder.addField("Only one person can use this command. "+ ConstantValues.
                                HAPPYEMOTE[Computations.generateIndex(ConstantValues.HAPPYEMOTE.length-1)], "",true);
                    }

                }
                case "hug" -> {

                    lastindex = Computations.generateIndex(ConstantValues.HAPPYEMOTE.length-1);

                    embedBuilder.setTitle(mentioned == null ? ConstantValues.HAPPYEMOTE[lastindex]:
                            v.getEffectiveName() + " Hugs " + mentioned.getEffectiveName());

                    lastindex = Computations.generateIndex(ConstantValues.HUGGIFS.length-1);
                    embedBuilder.setImage(ConstantValues.HUGGIFS[lastindex]);
                    embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);

                }

                case "kiss" -> {

                    lastindex = Computations.generateIndex(ConstantValues.KISSEMOTE.length-1);
                    embedBuilder.setTitle(mentioned == null ? ConstantValues.KISSEMOTE[lastindex] :
                            v.getEffectiveName() + " Kiss " + mentioned.getEffectiveName());
                    lastindex = Computations.generateIndex(ConstantValues.KISSGIFS.length-1);
                    embedBuilder.setImage(ConstantValues.KISSGIFS[lastindex]);

                    embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);

                }

                case "cheer" -> {

                    lastindex = Computations.generateIndex(ConstantValues.CHEERINGEMOTE.length-1);
                    embedBuilder.setTitle(mentioned == null ? ConstantValues.CHEERINGEMOTE[lastindex] :
                            v.getEffectiveName() + " Cheer's " + mentioned.getEffectiveName());
                    lastindex = Computations.generateIndex(ConstantValues.CHEERINGGIF.length-1);
                    embedBuilder.setImage(ConstantValues.CHEERINGGIF[lastindex]);

                    embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);

                }

            }

        return embedBuilder;
    }

    public static EmbedBuilder makeOutputTemplate(String type, @NotNull MessageReceivedEvent messageEvents){

        embedBuilder.clear();
        User mentioned = messageEvents.getAuthor();
        Guild guildActions = messageEvents.getGuild();
        long userID;
        if(messageEvents.getMessage().getMentionedMembers().isEmpty()){
            userID = mentioned.getIdLong();
        }else {

            userID = messageEvents.getMessage().getMentionedMembers().get(0).getIdLong();
        }
        Member member = guildActions.retrieveMemberById(userID).complete();


        if (!messageEvents.getMessage().getMentionedMembers().isEmpty())
            member = messageEvents.getMessage().getMentionedMembers().get(0);

        guildActions.getMembersByName(member.getEffectiveName(),false);

        switch (type.toLowerCase()) {
            case "version" -> {
                embedBuilder.setTitle("Who am i?");
                embedBuilder.addField("Hello my name is V02!", " was created in the philippines with only minimal " +
                        "features but well construct manner to do things. OwO", false);
                embedBuilder.addField("Bot abilities:", """
                        Discord Moderation
                        Playing music (Not Implemented yet)
                        Displaying info
                        Math computations (Not Implemented yet)
                        and More!""", false);
                embedBuilder.addField("Created by", "$USER (lonelyprogrammer)",false);
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setThumbnail("https://cdn.discordapp.com/avatars/711786745276137585/47567130960e1b2c5594ba13cb314e3c.png");
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "info" -> {

                embedBuilder.setTitle(mentioned.getName() + " Info");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.addField("Name: ", mentioned.getName(), true);
                embedBuilder.addField("User Tag: ", mentioned.getAsTag(), false);
                embedBuilder.addField("Additional Info", String.format("Time Joined : %s \nUser age %s"
                        , dateTimeFormatter.format(member.getTimeJoined()),dateTimeFormatter.format(member.getTimeCreated())),false);
                embedBuilder.addField("Status:",String.format("%s", member.getOnlineStatus()), false);
                embedBuilder.setThumbnail(mentioned.getAvatarUrl());
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "avatar" -> {

                embedBuilder.setTitle("Avatar | "+ member.getEffectiveName());
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setImage(member.getEffectiveAvatarUrl());
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
        }
        return embedBuilder;
    }
}
