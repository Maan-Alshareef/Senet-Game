import java.util.*;

public class StickThrow {

    private static final Map<Integer, Double> probabilities = Map.of(
            1, 4.0 / 16,
            2, 6.0 / 16,
            3, 4.0 / 16,
            4, 1.0 / 16,
            5, 1.0 / 16
    );

    public static Map<Integer, Double> getProbabilities() {
        return probabilities;
    }

    public static int throwSticks() {
        Random rand = new Random();
        double r = rand.nextDouble();
        double cumulative = 0.0;

        for (Map.Entry<Integer, Double> entry : probabilities.entrySet()) {
            cumulative += entry.getValue();
            if (r < cumulative) {
                return entry.getKey();
            }
        }
        return 1; 
    }
}