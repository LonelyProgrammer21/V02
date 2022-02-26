package features;

import features.constant.ConstantValues;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static features.constant.ConstantValues.COLORS;

public class Helper {

    private static final EmbedBuilder embedBuilder = new EmbedBuilder();
    public static final GregorianCalendar time = new GregorianCalendar();

    public static EmbedBuilder sendHelp(@NotNull String type){
        embedBuilder.clear();
        switch (type.toLowerCase()) {
            case "ban" -> {
                embedBuilder.setTitle("Help | Ban");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.addField("Description", "Ban member(s) on the discord server", false);
                embedBuilder.addField("Commands", "v! ban @User @User1 .. -reason message (optional)", false);
                embedBuilder.addField("Examples", "v! ban @NullPointerException @V01\n!v ban @NullPointerException @VO2 -reason message", false);
                embedBuilder.setThumbnail("https://e7.pngegg.com/pngimages/250/966/png-clipart-jinwan-district-judge-court-trial-law-hammer-icon-angle-painted.png");
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "makeinvite" -> {
                embedBuilder.setTitle("Help | Invitation Links");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length - 1)]);
                embedBuilder.addField("Description", "Make an invitation to this server.", false);
                embedBuilder.setThumbnail("https://cdn-icons-png.flaticon.com/512/3128/3128605.png");
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "help" -> {

                embedBuilder.setTitle("V02 Commands");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.addField("Prefix v!", "all commands starts with v! prefix",false);
                embedBuilder.addField("Guild Moderation ", """
                        ban
                        makerole
                        invite
                        promote
                        modifyrole
                        removerole
                        """,false);
                embedBuilder.addField("General Stuff", "info, avatar, version",false);
                embedBuilder.addField("General Fun", "blood, kiss, hug, cheers",false);
            }

        }
        return embedBuilder;
    }

    public static EmbedBuilder sendCommandHelp(String type){

        embedBuilder.clear();
        switch (type) {
            case "maketextchannel" -> {

                embedBuilder.setTitle("Help | maketextchannel");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setDescription("Create a channel to a discord w/ specifed arguments");
                embedBuilder.addField("Syntax", """
                        private - set the channel to private
                         (You should mentioned the members/role
                        if the channel is private).""",false);
                embedBuilder.addField("Example making a public channel:", "v! maketextchannel channelName",true);
                embedBuilder.addField("Second Example making a private channel:", "v! makechannel channelName private @roleName/@member",false);
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "modifyrole" -> {

                embedBuilder.setTitle("Help | modifyrole");
                embedBuilder.setDescription("Modify a role for a member (beta)");
                embedBuilder.addField("Syntax", "v! modifyrole @roleName @members...",false);
                embedBuilder.addField("Example adding a member to a role:", "v! modifyrole @foo @NullPointerException", true);
                embedBuilder.addField("Example adding multiple members:", "v! modifyrole @foo @NullPointerException @AHIHIHI @LenLen @ihatehumans",false);
                embedBuilder.addField("Example giving role to a specific role:", "v! modifyrole @roleNameOne @roleNameTwo",false);
                embedBuilder.setThumbnail("https://img.icons8.com/ios-filled/344/change-user-male.png");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "makerole" -> {
                embedBuilder.setTitle("Help | Making Roles");
                embedBuilder.setColor(COLORS[Computations.generateIndex(COLORS.length-1)]);
                embedBuilder.addField("Description", "Make a role on the server", false);
                embedBuilder.addField("Examples", "v! makerole rolename", false);
                embedBuilder.setThumbnail("https://img.freepik.com/free-vector/responsibility-sticker-professional-roles-icon-" +
                        "functions-responsibilities-duties-professional-member-idea-employer-employee-circle-worker-vector-isolated" +
                        "-background-eps-10_399089-2686.jpg?size=338&ext=jpg");
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "removerole" -> {

                embedBuilder.setTitle("Help | removerole");
                embedBuilder.setDescription("Remove role from a member or a role");
                embedBuilder.addField("Syntax", "!v removerole @subjectedRole @Members/@roles",false);
                embedBuilder.addField("Example removing role from a member", "!v removerole @subjectedRole @Member1 @Member2",false);
                embedBuilder.addField("Example removing role to an another role", "!v removerole @subjectedRole @role1 @role2",false);
                embedBuilder.setThumbnail("https://img.icons8.com/ios-filled/344/remove-user-male.png");
                embedBuilder.setColor(ConstantValues.COLORS[Computations.generateIndex(ConstantValues.COLORS.length-1)]);
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
            case "createcategory" -> {

                embedBuilder.setTitle("Help | createcategory");
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
                embedBuilder.setFooter(String.format("Date: %s %d %d Time: %d:%d:%d", ConstantValues.MONTHS[time.get(Calendar.MONTH)], time.get(Calendar.DATE),
                        time.get(Calendar.YEAR),
                        time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND)));
            }
        }
        return embedBuilder;
    }

}
