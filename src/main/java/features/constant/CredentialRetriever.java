package features.constant;

public class CredentialRetriever {

    private String TOKEN;
    public static String V01 = "";
    public static String OURGUILDID = "";

    private static void getEnv() {

            V01 = System.getenv("V01");
            OURGUILDID = System.getenv("OURGUILDID");
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
