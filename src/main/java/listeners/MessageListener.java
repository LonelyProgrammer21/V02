package listeners;

import features.*;
import features.constant.ConstantValues;
import features.constant.CredentialRetriever;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public final class MessageListener extends ListenerAdapter {

    private final ArrayList<String> commonCommands = new ArrayList<>(Arrays.asList(ConstantValues.COMMONCOMMANDS));
    private final ArrayList<String> textInput = new ArrayList<>();
    private final ArrayList<String> reactionCommands = new ArrayList<>(Arrays.asList(ConstantValues.REACTIONCOMMANDS));
    private final ArrayList<String> moderationCommands = new ArrayList<>(Arrays.asList(ConstantValues.MODERATIONCOMMANDS));
    private final ArrayList<String> musicCommand = new ArrayList<>(Arrays.asList(ConstantValues.MUSICCOMMANDS));
    private Guild guildActions = null;
    private MessageReceivedEvent messageEvents = null;
    List<Member> mentionedMembers = null;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){

        String fullMessage = event.getMessage().getContentRaw();
        guildActions = event.getGuild();
        messageEvents = event;
        textInput.addAll(Arrays.asList(fullMessage.split("\\s+")));
        String UID  = messageEvents.getAuthor().getId();
        if(!event.getAuthor().isBot()) {

            if(textInput.size() >= 2){
                if(textInput.get(0).equalsIgnoreCase("v!")){
                    System.out.println(textInput.get(1));
                    parsedCommand(textInput.get(1));
                }else {

                    if(textInput.get(1).equalsIgnoreCase("v02") ||  textInput.get(1).equalsIgnoreCase("Vo2")){
                        messageEvents.getChannel().sendMessage("Hello! "+ ConstantValues.HAPPYEMOTE[Computations.
                                generateIndex(ConstantValues.HAPPYEMOTE.length-1)]).queue();
                        return;
                    }

                    if(!messageEvents.getMessage().getMentionedMembers().isEmpty() && (textInput.get(0).equalsIgnoreCase("Happy") ||
                            textInput.get(0).equalsIgnoreCase("Merry"))){

                        if(messageEvents.getMessage().getMentionedMembers().get(0).getId().equals(guildActions.getSelfMember().getId())){
                            messageEvents.getChannel().sendMessage(new YearEventsGreetings().sendGreetings(textInput.get(1)
                                    ,guildActions.retrieveMemberById(UID).complete())).queue();
                        }
                    }

                }


            }

        }
        textInput.clear();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event){

        System.out.println("Ready to go!");
        Timer timer = new Timer();
        long perDay = 86400000;//Milliseconds conversion to 24 hours.
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                try {

                    Calendar cal = Calendar.getInstance();
                    int day = cal.get(Calendar.DAY_OF_WEEK);
                    Guild guild = event.getJDA().getGuildById(CredentialRetriever.OURGUILDID);
                    Role toReplace;
                    Role subjectedRole = guild.getRoleById(CredentialRetriever.HACKERRIST);
                    switch (day) {
                        case 1 -> {
                            toReplace = guild.getRolesByName("Assignment", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 2 -> {
                            toReplace = guild.getRolesByName("Monday", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 3 -> {
                            toReplace = guild.getRolesByName("Tuesday", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 4 -> {
                            toReplace = guild.getRolesByName("Wednesday", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 5 -> {
                            toReplace = guild.getRolesByName("Thursday", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 6 -> {
                            toReplace = guild.getRolesByName("Friday", true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        case 7 -> {
                            toReplace = guild.getRolesByName("Saturday",true).get(0);
                            Moderation.autoModifyRole(guild, toReplace, subjectedRole);
                        }
                        default -> {
                        }
                    }
                }catch (Exception e){

                    System.out.println("Something went wrong.");
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0L, perDay);
    }

    private void parsedCommand(String command){
        command = command.toLowerCase().trim();
        guildActions = messageEvents.getGuild();
        Member messageAuthor = guildActions.retrieveMember(messageEvents.getAuthor()).complete();

        command = command.toLowerCase();
        try {

            if(command.equalsIgnoreCase("help")){

                messageEvents.getChannel().sendMessageEmbeds(Helper.sendHelp(command).build()).queue();

            }else if(reactionCommands.contains(command)) {
                String OURGUILDID = CredentialRetriever.OURGUILDID;
                if(!messageEvents.getGuild().getId().equalsIgnoreCase(OURGUILDID))
                messageEvents.getChannel().sendMessageEmbeds(Fun.makeFunTemplate(command,
                        messageAuthor, messageEvents.getMessage().getMentionedMembers()).build()).queue();
                else
                    messageEvents.getChannel().sendMessage("No.").queue();
            }else if(commonCommands.contains(command)){

                    messageEvents.getChannel().sendMessageEmbeds(Fun.makeOutputTemplate(command,this.messageEvents
                    ).build()).queue();
            }else if (musicCommand.contains(command)){

                if(textInput.size() == 3 && textInput.get(2).equalsIgnoreCase("help")){

                    messageEvents.getChannel().sendMessageEmbeds(Helper.sendMusicCommandHelp(command).build()).queue();
                    return;
                }
                PlayMusic.event = messageEvents;
                PlayMusic.getCommand(command, messageEvents, new ArrayList<>(textInput.subList(textInput.indexOf(command)+1,
                        textInput.size())));
            }else{

                if(moderationCommands.contains(command)){

                    String type = moderationCommands.get(moderationCommands.indexOf(command));
                    System.out.println(type);
                    if(textInput.size() == 3 && textInput.get(2).equalsIgnoreCase("help")){

                        Helper.guildLocale = Objects.requireNonNull(messageEvents.getMember()).getGuild().getLocale();
                        messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp(command).build()).queue();
                        return;
                    }
                    Moderation.messageEvents = messageEvents;
                    Moderation.guildActions = guildActions;
                    Moderation.textInput = textInput;
                    Moderation.mentionedMembers = mentionedMembers;
                    Moderation.messageAuthor = messageAuthor;
                    switch (type) {

                        case "ban" -> Moderation.banAction();
                        case "makerole" -> Moderation.makeRole();
                        case "makeinvite" -> Moderation.makeInvite();
                        case "modifyrole" -> Moderation.modifyRole();
                        case "maketextchannel" -> Moderation.makeTextChannel();
                        case "removerole" -> Moderation.removeRole();
                        case "createcategory" -> Moderation.makeCategory();
                        case "modifytextchannelroles" -> Moderation.modifytextchannelroles();
                    }
                }

                try {
                    if(Moderation.textInput != null)
                        Moderation.textInput.clear();
                    if(Moderation.mentionedMembers != null)
                        Moderation.mentionedMembers.clear();

                }catch (Exception ignored){

                }

            }
        }catch (ArrayIndexOutOfBoundsException e){

            messageEvents.getChannel().sendMessage("Invalid arguments found").queue();
        }

    }
}
