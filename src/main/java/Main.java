import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

// temp initializer class with hardcoded input
public class Main {
  public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
    String filePath = "src\\test\\resources\\Middle C.wav";
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);
    List<Double> frequenciesFound = audioFrequencyInfo.getFrequencies();
    for (int i = 0; i < frequenciesFound.size(); i++) {
      double frequencyFound = frequenciesFound.get(i);
      int keyNum = Notes.findKeyNumFromFrequency(frequencyFound);
      if (keyNum != -1) {
        System.out.printf("Note %d found!%n", keyNum);
      }
    }
  }
}
