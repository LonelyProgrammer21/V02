package features;

import features.constant.ConstantValues;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static features.constant.ConstantValues.COLORS;

public class Helper {

    private static final EmbedBuilder embedBuilder = new EmbedBuilder();
    public static final GregorianCalendar time = new GregorianCalendar();
    public static Locale guildLocale;

    public static EmbedBuilder sendHelp(@NotNull String type){
        embedBuilder.clear();
        if (!"help".equals(type.toLowerCase())) {
            return embedBuilder;
        }
        embedBuilder.setTitle("V02 Commands");
        embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length - 1)]);
        embedBuilder.addField("Prefix v!", "all commands starts with v! prefix", false);
        embedBuilder.addField("Guild Moderation ", """
                ban
                makerole
                invite
                promote
                modifyrole
                removerole
                createcategory
                maketextchannel
                modifytextchannelroles
                """, false);
        embedBuilder.addField("General Stuff", "info, avatar, version", false);
        embedBuilder.addField("General Fun", "blood, kiss, hug, cheers", false);
        return embedBuilder;
    }

    public static EmbedBuilder sendModerationCommandHelp(String type){

        embedBuilder.clear();
        switch (type) {
            case "ban" -> {
                embedBuilder.setTitle("Help | Ban");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.addField("Description", "Ban member(s) on the discord server", false);
                embedBuilder.addField("Commands", "v! ban @User @User1 .. -reason message (optional)", false);
                embedBuilder.addField("Examples", "v! ban @NullPointerException @V01\n!v ban @NullPointerException @VO2 -reason message", false);
                embedBuilder.setThumbnail("https://e7.pngegg.com/pngimages/250/966/png-clipart-jinwan-district-judge-court-trial-law-hammer-icon-angle-painted.png");
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "makeinvite" -> {
                embedBuilder.setTitle("Help | Invitation Links");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length - 1)]);
                embedBuilder.addField("Description", "Make an invitation to this server.", false);
                embedBuilder.setThumbnail("https://cdn-icons-png.flaticon.com/512/3128/3128605.png");
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "maketextchannel" -> {

                embedBuilder.setTitle("Help | Make Text Channel");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setDescription("Create a channel to a discord w/ specifed arguments");
                embedBuilder.addField("Syntax", """
                        private - set the channel to private
                         (You should mentioned the members/role
                        if the channel is private).""",false);
                embedBuilder.addField("Example making a public channel:", "v! maketextchannel channelName",true);
                embedBuilder.addField("Second Example making a private channel:", "v! makechannel channelName private @roleName/@member",false);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "modifyrole" -> {

                embedBuilder.setTitle("Help | Modify Role");
                embedBuilder.setDescription("Modify a role for a member (beta)");
                embedBuilder.addField("Syntax", "v! modifyrole @roleName @members...",false);
                embedBuilder.addField("Example adding a member to a role:", "v! modifyrole @foo @NullPointerException", true);
                embedBuilder.addField("Example adding multiple members:", "v! modifyrole @foo @NullPointerException @AHIHIHI @LenLen @ihatehumans",false);
                embedBuilder.addField("Example giving role to a specific role:", "v! modifyrole @roleNameOne @roleNameTwo",false);
                embedBuilder.setThumbnail("https://img.icons8.com/ios-filled/344/change-user-male.png");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "makerole" -> {
                embedBuilder.setTitle("Help | Making Roles");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.addField("Description", "Make a role on the server", false);
                embedBuilder.addField("Examples", "v! makerole rolename", false);
                embedBuilder.setThumbnail("https://img.freepik.com/free-vector/responsibility-sticker-professional-roles-icon-" +
                        "functions-responsibilities-duties-professional-member-idea-employer-employee-circle-worker-vector-isolated" +
                        "-background-eps-10_399089-2686.jpg?size=338&ext=jpg");
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "removerole" -> {

                embedBuilder.setTitle("Help | Remove Role");
                embedBuilder.setDescription("Remove role from a member or a role");
                embedBuilder.addField("Syntax", "!v removerole @subjectedRole @Members/@roles",false);
                embedBuilder.addField("Example removing role from a member", "!v removerole @subjectedRole @Member1 @Member2",false);
                embedBuilder.addField("Example removing role to an another role", "!v removerole @subjectedRole @role1 @role2",false);
                embedBuilder.setThumbnail("https://img.icons8.com/ios-filled/344/remove-user-male.png");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "createcategory" -> {

                embedBuilder.setTitle("Help | Create Category");
                embedBuilder.setDescription("Make a category with options on a server.\nBy default" +
                        " the category is set to private mode.\nNote: This action require" +
                        " the author have a permission to create category and manage roles.");
                embedBuilder.addField("Syntax","v! createcategory CategoryName\nNote: CategoryName should not have" +
                        " spaces.",false);
                embedBuilder.addField("Example making a category and move text-channels to it", "v! create" +
                        "category CategoryName #text-channel #text-channel1 #text-channel(n)", false);
                embedBuilder.addField("Example allowing a specific role/member to access the category.","v! " +
                        "createcategory CategoryName @Role @member", false);
                embedBuilder.setThumbnail("https://img.icons8.com/ios-glyphs/344/category.png");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }

            case "modifytextchannelroles" -> {

                embedBuilder.setTitle("Help | Modify Text Channel Roles");
                embedBuilder.setDescription("Modify a text channel's roles\nNote:The user should have sufficient permission " +
                        "to execute this command.");
                embedBuilder.addField("Syntax", "v! modifytextchannelroles",  false);
                embedBuilder.addField("Example modifying roles on text channel.",
                        "v! modifytextchannelroles #text-channel @role",false);
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
        }
        return embedBuilder;
    }

    public static EmbedBuilder sendMusicCommandHelp(String type){

        embedBuilder.clear();
        System.out.println(type);
        switch (type){

            case "join" -> {
                embedBuilder.setTitle("Help | Joining bot to a voice channel");
                embedBuilder.setDescription("Join a bot to a specific voice channel.");
                embedBuilder.addField("Syntax","v! join\nNote: The user should be in a voice channel " +
                        "and the bot is not on other voice channel to make it work",false);
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));
            }
            case "play" -> {

                embedBuilder.setTitle("Help | Playing music");
                embedBuilder.setDescription("Play music with the bot");
                embedBuilder.addField("Syntax","v! play URL/name\nNote: The user should be in a voice channel " +
                        "and the bot is not on other voice channel to make it work",false);
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %s", ConstantValues.MONTHS[time.get(Calendar.MONTH)],
                        new SimpleDateFormat("dd HH:m:ss a", guildLocale).format(new Date())));

            }
        }

        return embedBuilder;
    }

}
