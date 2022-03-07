import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

// temp initializer class with hardcoded input
public class Main {
  public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
    String filePath = "src/test/resources/Piano.ff.C4.aif";
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);
    List<Double> frequenciesFound = audioFrequencyInfo.getFrequencies();
    for (double frequencyFound : frequenciesFound) {
      int keyNum = Notes.findKeyNumFromFrequency(frequencyFound);
      if (keyNum != -1) {
        System.out.printf("%s found!%n", Notes.getScientificPitchNotation(keyNum));
      }
    }
  }
}
