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

  public static String getScientificPitchNotation(int keyNum) {
    int octave = (int) Math.ceil((keyNum - 3) / 12.0);
    int moddedKeyNum = (keyNum + 8) % 12;
    String pitchName = switch(moddedKeyNum) {
      case 0 -> "C";
      case 1 -> "C#/Db";
      case 2 -> "D";
      case 3 -> "D#/Eb";
      case 4 -> "E";
      case 5 -> "F";
      case 6 -> "F#/Gb";
      case 7 -> "G";
      case 8 -> "G#/Ab";
      case 9 -> "A";
      case 10 -> "A#/Bb";
      case 11 -> "B";
      default -> throw new IllegalStateException("Illegal key num found " + keyNum); // handle irrelevant negative cases
    };

    return pitchName + octave;
  }

  private static double findFrequency(int keyNum, int cents) {
    double exponent = (((keyNum - 49) * 100) + cents) / 1200.0;
    return Math.pow(2, exponent) * FREQ_OF_A4;
  }
}
