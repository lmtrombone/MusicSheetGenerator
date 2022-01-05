import org.testng.Assert;
import org.testng.annotations.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public class AudioReaderTest {
  @Test
  public void testAudioReader() throws UnsupportedAudioFileException, IOException {
    String filePath = "src\\test\\resources\\Middle C.wav";
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);

    AudioFormat audioFormat = audioFrequencyInfo.getAudioFormat();

    Assert.assertEquals(audioFormat.getSampleRate(), 48000);
    Assert.assertTrue(isFrequencyFound(audioFrequencyInfo.getFrequencies(), 257.8125));
  }

  private boolean isFrequencyFound(List<Double> frequenciesFound, double expectedFrequency) {
    return frequenciesFound.stream().anyMatch(f -> f == expectedFrequency);
  }
}
