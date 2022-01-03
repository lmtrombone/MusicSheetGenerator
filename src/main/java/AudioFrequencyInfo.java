import javax.sound.sampled.AudioFormat;
import java.util.List;

public class AudioFrequencyInfo {
  private AudioFormat audioFormat;
  private int sampleSize;
  private List<Double> frequencies;

  public AudioFormat getAudioFormat() {
    return audioFormat;
  }

  public void setAudioFormat(AudioFormat audioFormat) {
    this.audioFormat = audioFormat;
  }

  public int getSampleSize() {
    return sampleSize;
  }

  public void setSampleSize(int sampleSize) {
    this.sampleSize = sampleSize;
  }

  public List<Double> getFrequencies() {
    return frequencies;
  }

  public void setFrequencies(List<Double> frequencies) {
    this.frequencies = frequencies;
  }
}
