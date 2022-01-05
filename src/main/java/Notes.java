import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Notes {
  private static final NavigableMap<Double, ImmutablePair<Double, Integer>> NOTES_MAP;
  private static final int NUM_OF_KEYS = 88;
  private static final int FREQ_OF_A4 = 440;
  private static final int CENTS_THRESHOLD = 30;

  static {
    NOTES_MAP = new TreeMap<>();
    for (int keyNum = 1; keyNum <= NUM_OF_KEYS; keyNum++) {
      double lowerBoundFrequency = findFrequency(keyNum, -CENTS_THRESHOLD);
      double upperBoundFrequency = findFrequency(keyNum, CENTS_THRESHOLD);
      NOTES_MAP.put(lowerBoundFrequency, new ImmutablePair<>(upperBoundFrequency, keyNum));
    }
  }

  public static int findKeyNumFromFrequency(double frequency) {
    int keyNum = -1;

    Map.Entry<Double, ImmutablePair<Double, Integer>> frequencyInfoEntry = NOTES_MAP.floorEntry(frequency);
    if (frequencyInfoEntry == null) {
      return keyNum;
    }

    double lowerBoundFrequency = frequencyInfoEntry.getKey();
    double upperBoundFrequency = frequencyInfoEntry.getValue().getKey();

    if ((lowerBoundFrequency <= frequency) && (frequency <= upperBoundFrequency)) {
      keyNum = frequencyInfoEntry.getValue().getValue();
    }

    return keyNum;
  }

  private static double findFrequency(int keyNum, int cents) {
    double exponent = (((keyNum - 49) * 100) + cents) / 1200.0;
    return Math.pow(2, exponent) * FREQ_OF_A4;
  }
}
