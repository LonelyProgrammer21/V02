package features;

import features.constant.ConstantValues;
import features.constant.CredentialRetriever;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static features.Helper.time;
import static features.constant.ConstantValues.COLORS;

public class Moderation {

    public static ArrayList<String> textInput;
    public static MessageReceivedEvent messageEvents;
    private static final EmbedBuilder embedBuilder = new EmbedBuilder();
    public static Guild guildActions;
    public static List<Member> mentionedMembers;
    public static Member messageAuthor;
    private static Role subjectedRole;
    private static final EnumSet<Permission> allowed = EnumSet.of(Permission.MESSAGE_SEND,Permission.VIEW_CHANNEL);
    private static final EnumSet<Permission> denied = EnumSet.of(Permission.CREATE_INSTANT_INVITE);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    public static void modifytextchannelroles(){

        if(textInput.size() > 3 && textInput.get(2).equalsIgnoreCase("help")){

            messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("modifytextchannelroles").build()).queue();
            return;
        }

        if(messageEvents.getMessage().getMentionedRoles().isEmpty() | messageEvents.getMessage().getMentionedChannels().isEmpty()){

            messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("modifytextchannelroles").build()).queue();
            return;
        }
        for(TextChannel ch: messageEvents.getMessage().getMentionedChannels()){

            for(Role e: messageEvents.getMessage().getMentionedRoles()){

                ch.getManager().putRolePermissionOverride(e.getIdLong(), allowed,denied).queue();
            }
        }
        messageEvents.getChannel().sendMessage("Done!").queue();
    }
    public static void makeInvite(){

        String url = Objects.requireNonNull(messageEvents.getGuild().getDefaultChannel()).createInvite().complete().getUrl();
        Member messageAuthor = messageEvents.getMember();

        assert messageAuthor != null;
        if(messageAuthor.hasPermission(Permission.CREATE_INSTANT_INVITE)){
            messageEvents.getChannel().sendMessage("Here is the invite link: " + url).queue();
        }else {

            messageEvents.getChannel().sendMessage("You don't have permissions to create an invite.").queue();
        }
    }

    public static void makeCategory(){

        String message = "Queuing please wait...";
        try {
            if(textInput.get(2).equalsIgnoreCase("help")){

                throw new Exception();
            }
            if(!messageAuthor.hasPermission(Permission.MANAGE_PERMISSIONS) && !messageAuthor.hasPermission(Permission.MANAGE_ROLES)){

                messageEvents.getChannel().sendMessage("You don't have permission to create category.").queue();
                return;
            }
            Role publicRole = guildActions.getPublicRole();
            String categoryName = textInput.get(2).trim();
            Category newCategory = guildActions.createCategory(categoryName).addRolePermissionOverride(publicRole.getIdLong(),
                    Permission.MESSAGE_SEND.getRawValue(), Permission.VIEW_CHANNEL.getRawValue()).complete();

            if(!messageEvents.getMessage().getMentionedChannels().isEmpty()){

                List<TextChannel> mentionedChannels = messageEvents.getMessage().getMentionedChannels();
                for(TextChannel channel : mentionedChannels){

                    channel.getManager().setParent(newCategory).queue();
                }
            }

            if(!messageEvents.getMessage().getMentionedMembers().isEmpty()) {

                for(Member member : messageEvents.getMessage().getMentionedMembers()){

                    newCategory.getManager().putMemberPermissionOverride(member.getIdLong(),allowed,denied).queue();
                }
            }

            if(!messageEvents.getMessage().getMentionedRoles().isEmpty()){

                for(Role role : messageEvents.getMessage().getMentionedRoles()){

                    newCategory.getManager().putRolePermissionOverride(role.getIdLong(),allowed,denied).queue();
                }
            }
            if(textInput.size() == 3){
                message = "Done! created: "+ categoryName;
            }

            messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder(message).build()).queue();
        }catch (Exception e){

            messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("createcategory").build()).queue();
        }


    }

    public static void banAction(){

        String reason = "";

        if(textInput.contains("-reason")){
            reason = textInput.get(textInput.indexOf("-reason")+1);

        }
        mentionedMembers = messageEvents.getMessage().getMentionedMembers();
        StringBuilder membersCollection = new StringBuilder();

        if(messageAuthor.hasPermission(Permission.ADMINISTRATOR)){

            if(canUserInteract()){
                if(!mentionedMembers.isEmpty()){
                    for (Member mentionedMember : mentionedMembers) {

                        membersCollection.append(mentionedMember.getAsMention()).append(" ");
                        guildActions.ban(mentionedMember.getUser(), 3).queue();

                    }

                    messageEvents.getChannel().sendMessage(String.format("User: %s has been banned. Reason: %s\n",
                            new String(membersCollection), reason.isBlank() ? "Not given": reason)).queue();
                }else {

                    messageEvents.getChannel().sendMessage("No members is given.").queue();
                }
            }
        }else {

            messageEvents.getChannel().sendMessage("You don't have permission to use this command").queue();
        }

    }

    public static void autoModifyRole(Guild guild, Role toReplace, Role subjectedRole){

       List<Member> memberList = guild.getMembersWithRoles(subjectedRole);

       List<Role> roles = guild.getRoles();
       for(Role role : roles){

           if(!role.getName().contains("day"))
               continue;
           System.out.println(role.getName());

           for (Member member : memberList){

               if(member.getRoles().contains(role))
                   guild.removeRoleFromMember(member, role).queue();
           }

       }

       for(Member member: memberList){

           guild.addRoleToMember(member, toReplace).queue();
       }
       TextChannel defaultChannel = guild.getTextChannelById(CredentialRetriever.DEFAULT_TEXT_CHANNEL);
        assert defaultChannel != null;
        defaultChannel.sendMessageEmbeds(getEmbedBuilder("Modifying roles automatically queueing...").build()).queue();

    }
    private static boolean canUserInteract(){

        for (Member mentionedMember : mentionedMembers) {

            if (!messageAuthor.canInteract(mentionedMember)) {
                return false;
            }
        }
        return true;
    }

    private static EmbedBuilder getEmbedBuilder(String message){
        embedBuilder.clear();

        embedBuilder.setDescription(message);
        embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(COLORS.length-1)]);
        embedBuilder.setFooter(dateFormat.format(new Date()) + " UTC");
        return  embedBuilder;
    }

    public static void modifyRole(){

        if(messageAuthor.hasPermission(Permission.MANAGE_ROLES)){

            try {
                if(textInput.get(2).equalsIgnoreCase("help")){

                    messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("modifyrole").build()).queue();
                    return;
                }

                if(!messageEvents.getMessage().getMentionedRoles().isEmpty()){
                    subjectedRole = messageEvents.getMessage().getMentionedRoles().get(0);

                    if(!messageEvents.getMessage().getMentionedMembers().isEmpty()){

                        mentionedMembers = messageEvents.getMessage().getMentionedMembers();
                        for(Member member:mentionedMembers){

                            guildActions.addRoleToMember(member,subjectedRole).queue();
                        }
                        messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder("Role is added to member(s)").build()).queue();
                    }else {
                        if(!messageEvents.getMessage().getMentionedRoles().isEmpty()){

                            Role mentionedRole = messageEvents.getMessage().getMentionedRoles().get(1);

                            if(!guildActions.getMembersWithRoles(mentionedRole).isEmpty()){
                                for(Member member: guildActions.getMembers()){

                                    for(Role memberRole : member.getRoles()){

                                        if(memberRole.getName().equalsIgnoreCase(mentionedRole.getName())){

                                            guildActions.addRoleToMember(member,subjectedRole).queue();
                                        }
                                    }
                                }
                            }else {

                                messageEvents.getChannel()
                                        .sendMessageEmbeds(getEmbedBuilder("No members is found on this role.").build()).queue();
                                return;
                            }
                            messageEvents.getChannel()
                                    .sendMessageEmbeds(getEmbedBuilder("Adding roles queuing...").build()).queue();
                        }else {
                            messageEvents.getChannel()
                                    .sendMessageEmbeds(getEmbedBuilder("Specify the options for the role.").build()).queue();
                        }
                    }
                }else {

                    throw new Exception();
                }


            }catch (Exception e){
                messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("modifyrole").build()).queue();
            }

        }else {

            messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder("You dont have permission to make a role.").build()).queue();
        }


    }

    public static void makeTextChannel(){

        String channelName;
        TextChannel theChannel;
        List<Role> mentionedRoles = messageEvents.getMessage().getMentionedRoles();
        List<Member> mentionedMembers = messageEvents.getMessage().getMentionedMembers();
        if(textInput.size() > 2){

            channelName = textInput.get(2);
            if(channelName.equalsIgnoreCase("help")){

               messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("maketextchannel").build()).queue();
                return;
            }
            if(!messageAuthor.hasPermission(Permission.MANAGE_CHANNEL)){
                messageEvents.getChannel()
                        .sendMessageEmbeds(getEmbedBuilder("You dont have permission to create text-channels.").build()).queue();
                return;
            }
            if(checkCurrentGuildInfo(textInput.subList(textInput.indexOf("maketextchannel")+1,textInput.size()),"maketextchannel")){

                messageEvents.getChannel()
                        .sendMessageEmbeds(getEmbedBuilder("Some channels are already existed. Remove it and try again.").build()).queue();
                return;
            }

            theChannel = messageEvents.getGuild().createTextChannel(channelName).setType(ChannelType.TEXT).complete();
                if(textInput.contains("private")){

                    theChannel.createPermissionOverride(messageEvents.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    if(mentionedRoles.isEmpty() && mentionedMembers.isEmpty()){
                        messageEvents.getChannel()
                                .sendMessageEmbeds(getEmbedBuilder("Specify the role or members to be added if the text channel is private.").build()).queue();
                        return;
                    }
                    if(!mentionedRoles.isEmpty()){

                        for(Role e: mentionedRoles){

                            theChannel.createPermissionOverride(e).setAllow(Permission.VIEW_CHANNEL).queue();
                        }
                    }
                    if(!mentionedMembers.isEmpty()){

                        for(Member e: mentionedMembers){

                            theChannel.createPermissionOverride(e).setAllow(Permission.VIEW_CHANNEL).queue();
                        }
                    }
                }
            messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder("Channels is now created!").build()).queue();
        }
    }

    public static void makeRole(){

        Color myColor;
        if(messageAuthor.hasPermission(Permission.MANAGE_ROLES)) {
            if (textInput.size() > 2) {
                if(textInput.get(2).equalsIgnoreCase("help")){

                    messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("makerole").build()).queue();
                    return;
                }

                if(checkCurrentGuildInfo(textInput.subList(textInput.indexOf("makerole")+1,textInput.size()), "makerole")){

                    messageEvents.getChannel().
                            sendMessageEmbeds(getEmbedBuilder("Some roles are already existed. Remove it and try again.").build()).queue();
                    return;
                }
                for(String roleNames : textInput.subList(textInput.indexOf("makerole")+1,textInput.size())){

                    myColor = COLORS[Computations.generateIndex(COLORS.length-1)];
                    guildActions.createRole().setName(roleNames).setColor(myColor).queue();

                }

                messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder("Done queuing...").build()).queue();
            }
        }else {
            messageEvents.getChannel().
                    sendMessageEmbeds(getEmbedBuilder("You dont have permissions to create roles.").build()).queue();
        }
}

public static void removeRole(){

        List<Member> memberList = messageEvents.getMessage().getMentionedMembers();
        List<Role> roleList = messageEvents.getMessage().getMentionedRoles();

        if(textInput.get(2).equalsIgnoreCase("help")){

            messageEvents.getChannel().
                    sendMessageEmbeds(Helper.sendModerationCommandHelp("removerole").build()).queue();
            return;
        }
        if(!messageAuthor.hasPermission(Permission.MANAGE_ROLES)){

            messageEvents.getChannel().
                    sendMessageEmbeds(getEmbedBuilder("You dont have permission to use this command.").build()).queue();
            return;
        }
        if(roleList.isEmpty() && memberList.isEmpty()){

            messageEvents.getChannel().
                    sendMessageEmbeds(getEmbedBuilder("Specify the role/member to be remove.").build()).queue();
            return;
        }
        subjectedRole = roleList.get(0);

        if(!memberList.isEmpty()){

            for(Member member : memberList){

                messageEvents.getGuild().removeRoleFromMember(member, subjectedRole).queue();
            }
        }
        if(!roleList.isEmpty()){

            List<Member> memberwithThisRole;
            for (Role role : roleList) {

                memberwithThisRole = messageEvents.getGuild().getMembersWithRoles(role);
                for (Member member : memberwithThisRole) {
                    messageEvents.getGuild().removeRoleFromMember(member, subjectedRole).queue();
                }
            }
        }
        messageEvents.getChannel().sendMessageEmbeds(getEmbedBuilder("Done!").build()).queue();

}

private static boolean checkCurrentGuildInfo(List<String> tokens, @NotNull String command){

    List<Role> existingRoles = messageEvents.getGuild().getRoles();
    List<TextChannel> existingChannel = messageEvents.getGuild().getTextChannels();
        switch (command){

            case "makerole" -> {

                for(Role role : existingRoles){

                    for (String token : tokens) {

                        if (role.getName().equalsIgnoreCase(token)) {
                            return true;
                        }
                    }
                }

            }
            case "maketextchannel" -> {

                for(TextChannel channels : existingChannel){

                    for (String token : tokens) {

                        if (channels.getName().equalsIgnoreCase(token)) {
                            return true;
                        }
                    }
                }

            }

        }
        return false;
}
}
