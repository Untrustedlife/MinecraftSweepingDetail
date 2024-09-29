package untrustedlife.mods.minecraftsweepingdetail;

/**
 * Helper functions.
 *
 * @author Untrustedlife
 */
public class UntrustedDiceRolling {
    /**
     * This function returns a number between 1 (inclusive) and maxDice (inclusive)
     * @param maxDice
     * @return
     */
    public static int rollDice(int maxDice){
       int answer = (int)(Math.ceil(Math.random()*maxDice));
       if (answer == 0){
        return 1;
       }
       else {
        return answer;
       }
    }

    /**
     * This function returns a number between 1 (exclusive) and -1 (inclusive)
     * @return a random number between -1 and just below 1
     */
    public static double generateRangeNegativeOneToOne(){
        return (Math.random()*2.0)-1.0;
    }


    /**
     * This function returns a random number between -max (inclusive) and max (exclusive).
     * @param max the upper bound of the range (mirrored as the lower bound)
     * @return a random number between -max and max, never reaching exactly max
     */
    public static double generateNormalizedValueBetween(double max){
        return (Math.random()*(max*2))-max;
    }


    /**
     * This method chooses a random element from a generic array.
     * 
     * @param <T> the type of elements in the array
     * @param array the array to choose from
     * @return a random element from the array
     * @throws IllegalArgumentException if the array is null or empty
     */
    public static <T> T choose(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        int randomIndex = (int)(Math.random() * array.length);  // Correct random index calculation
        return array[randomIndex];
    }
}
