package main;

import features.constant.CredentialRetriever;
import listeners.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {

        try {
            JDA accessPoint = JDABuilder.createDefault(new CredentialRetriever().retrieveFile())
                    .setActivity(Activity.listening("v! help")).enableIntents
                            (GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                                    GatewayIntent.GUILD_VOICE_STATES).enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE
                    ).setMemberCachePolicy(MemberCachePolicy.ALL).build();
            accessPoint.addEventListener(new MessageListener());

        }catch (LoginException e ){

            e.printStackTrace();
        }

    }
}
