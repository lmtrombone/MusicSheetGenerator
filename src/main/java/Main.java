import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

// temp initializer class with hardcoded input
public class Main {
  public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
    String filePath = "src\\main\\java\\Middle C.wav";
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);
    List<Double> frequenciesFound = audioFrequencyInfo.getFrequencies();
    for (int i = 0; i < frequenciesFound.size(); i++) {
      double frequencyFound = frequenciesFound.get(i);
      if ((254.0 < frequencyFound) && (frequencyFound < 268.0)) {
        System.out.printf("Middle C found at position %d%n", i);
      }
    }
  }
}
