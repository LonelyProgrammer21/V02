package features;

import java.util.Random;

public class Computations {

    private static int lastindex;
    public static int generateIndex(int arrlen){

        Random randomNum = new Random();
        int resultIndex = 0;
        int currentReturn = randomNum.nextInt(arrlen);
        if(currentReturn != lastindex){
            resultIndex = currentReturn;
        }else {

            if(currentReturn+1 > arrlen){

                resultIndex = currentReturn-2;
            }else if(currentReturn-1 == -1){
                resultIndex = currentReturn+3;
            }
        }
        return resultIndex;
    }
}
