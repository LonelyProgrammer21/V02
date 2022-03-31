package features;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import static features.constant.ConstantValues.COLORS;

public class Moderation {

    public static ArrayList<String> textInput;
    public static MessageReceivedEvent messageEvents;
    public static Guild guildActions;
    public static List<Member> mentionedMembers;
    public static Member messageAuthor;
    private static Role subjectedRole;
    private static final EnumSet<Permission> allowed = EnumSet.of(Permission.MESSAGE_SEND,Permission.VIEW_CHANNEL);
    private static final EnumSet<Permission> denied = EnumSet.of(Permission.CREATE_INSTANT_INVITE);

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

        if(textInput.get(2).equalsIgnoreCase("help")){

            messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("createcategory").build()).queue();
            return;
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

        messageEvents.getChannel().sendMessage("Done!").queue();

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
    private static boolean canUserInteract(){

        for (Member mentionedMember : mentionedMembers) {

            if (!messageAuthor.canInteract(mentionedMember)) {
                return false;
            }
        }
        return true;
    }

    public static void modifyRole(){

        if(messageAuthor.hasPermission(Permission.MANAGE_ROLES)){

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
                    messageEvents.getChannel().sendMessage("Members is added to the role.").queue();
                }else {
                    if(!messageEvents.getMessage().getMentionedRoles().isEmpty()){

                        Role mentionedRole = messageEvents.getMessage().getMentionedRoles().get(1);

                        for(Member member: guildActions.getMembers()){

                            for(Role memberRole : member.getRoles()){

                                if(memberRole.getName().equalsIgnoreCase(mentionedRole.getName())){

                                    guildActions.addRoleToMember(member,subjectedRole).queue();
                                }
                            }
                        }
                        messageEvents.getChannel().sendMessage("A role is added to another role").queue();
                    }else {
                        messageEvents.getChannel().sendMessage("Specify the options for the role.").queue();
                    }
                }
            }

        }else {

            messageEvents.getChannel().sendMessage("You dont have permission to make a role.").queue();
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
                messageEvents.getChannel().sendMessage("You dont have permission to create text-channels.").queue();
                return;
            }
            if(checkCurrentGuildInfo(textInput.subList(textInput.indexOf("maketextchannel")+1,textInput.size()),"maketextchannel")){

                messageEvents.getChannel().sendMessage("Some channels are already existed. Remove it and try again.").queue();
                return;
            }

            theChannel = messageEvents.getGuild().createTextChannel(channelName).setType(ChannelType.TEXT).complete();
                if(textInput.contains("private")){

                    theChannel.createPermissionOverride(messageEvents.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                    if(mentionedRoles.isEmpty() && mentionedMembers.isEmpty()){
                        messageEvents.getChannel().sendMessage("Specify the role or members to be added if the text channel is private.").queue();
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
            messageEvents.getChannel().sendMessage("The channels is now created!").queue();
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

                    messageEvents.getChannel().sendMessage("Some roles are already existed. Remove it and try again.").queue();
                    return;
                }
                for(String roleNames : textInput.subList(textInput.indexOf("makerole")+1,textInput.size())){

                    myColor = COLORS[Computations.generateIndex(COLORS.length-1)];
                    guildActions.createRole().setName(roleNames).setColor(myColor).queue();

                }

                messageEvents.getChannel().sendMessage("Done!").queue();
            }
        }else {
            messageEvents.getChannel().sendMessage("You dont have permissions to create roles.").queue();
        }
}

public static void removeRole(){

        List<Member> memberList = messageEvents.getMessage().getMentionedMembers();
        List<Role> roleList = messageEvents.getMessage().getMentionedRoles();

        if(textInput.get(2).equalsIgnoreCase("help")){

            messageEvents.getChannel().sendMessageEmbeds(Helper.sendModerationCommandHelp("removerole").build()).queue();
            return;
        }
        if(!messageAuthor.hasPermission(Permission.MANAGE_ROLES)){

            messageEvents.getChannel().sendMessage("You dont have permission to use this command.").queue();
            return;
        }
        if(roleList.isEmpty() && memberList.isEmpty()){

            messageEvents.getChannel().sendMessage("Specify the role/member to be remove.").queue();
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
        messageEvents.getChannel().sendMessage("Done!").queue();

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
