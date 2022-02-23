package features;

import features.constant.ConstantValues;
import features.constant.CredentialRetriever;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import java.util.HashMap;

public class YearEventsGreetings {

    private final String V01ID;
    private final HashMap <String,String> events = new HashMap<>();

    public YearEventsGreetings(){

        V01ID = CredentialRetriever.V01;
        events.put("newyear", "January 1");
        events.put("valentines", "February 14");
        events.put("christmas", "December 25");
        events.put("halloween", "November 1");

    }
    public  String sendGreetings(String type, final @NotNull Member author){

        String greeting = "No its not " + type + " today " + ConstantValues.
                ANGRYEMOTE[Computations.generateIndex(ConstantValues.ANGRYEMOTE.length-1)];
        String emote = ConstantValues.HAPPYEMOTE[Computations.generateIndex(ConstantValues.HAPPYEMOTE.length-1)];
        String username = author.getEffectiveName();
        String date = ConstantValues.MONTHS[Helper.time.get(Calendar.MONTH)]+" "+Helper.time.get(Calendar.DATE);

        if(author.getId().equals(V01ID)){
            username = "V01";
        }
        switch (type.toLowerCase()){

            case "newyear" -> {

                if(events.get(type).equalsIgnoreCase(date)){
                    greeting = "Happy new year !" + username + emote;
                }
            }
            case "valentines" -> {

                if(events.get(type).equalsIgnoreCase(date)){
                    greeting = "Happy Valentines!" + username + emote;
                }
            }

            case "christmas" -> {

                if(events.get(type).equalsIgnoreCase(date)){
                    greeting = "Merry Christmas !" + username + emote;
                }
            }

            case "halloween" -> {

                if(events.get(type).equalsIgnoreCase(date)){
                    greeting = "Happy Halloween !" + username + emote;
                }
            }
        }
        return greeting;
    }
}
