package features.constant;

public class CredentialRetriever {

    private String TOKEN;
    public static String V01 = "";
    public static String OURGUILDID;
    public static String TENOR_TOKEN;
    public static String HACKERRIST;
    public static String DEFAULT_TEXT_CHANNEL;

    private static void getEnv() {

            V01 = System.getenv("V01");
            OURGUILDID = System.getenv("OURGUILDID");
            TENOR_TOKEN = System.getenv("TENOR_TOKEN");
            DEFAULT_TEXT_CHANNEL = System.getenv("DEFAULT_TEXT_CHANNEL");
            HACKERRIST = System.getenv("HACKERRIST");
    }

    public CredentialRetriever(){

        try{

            TOKEN = System.getenv("TOKEN");
            getEnv();
        }catch (Exception e){

            System.out.printf("Error on getting ENV's StackTrace: %s%n", e.getMessage());
        }
    }

    public String retrieveFile(){

        return TOKEN;
    }
}
