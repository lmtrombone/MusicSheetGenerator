import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioReader {
  private static final int BUF_SIZE = 8192;

  public static List<Double> getFrequencies(String filePath) throws UnsupportedAudioFileException, IOException {
    AudioInputStream audioInputStream = null;
    List<Double> frequenciesFound = new ArrayList<>();

    try {
      audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
      AudioFormat audioFormat = audioInputStream.getFormat();
      byte[] buf = new byte[BUF_SIZE];

      while (audioInputStream.read(buf) != -1) {
        double[] samples = getSamples(buf, audioFormat);
        Complex[] fft = FastFourierTransform.computeFft(samples);
        double[] magnitudes = convertFftToMagnitudes(fft);

        float sampleRate = audioFormat.getSampleRate();
        int sampleSize = samples.length;
        double frequencyFound = findFrequency(magnitudes, sampleRate, sampleSize);

        frequenciesFound.add(frequencyFound);
      }
    } finally {
      if (audioInputStream != null) {
        audioInputStream.close();
      }
    }

    return frequenciesFound;
  }

  private static double[] getSamples(byte[] buf, AudioFormat format) {
    int arrSize = buf.length / format.getFrameSize();
    boolean isArrSizePowerOfTwo = isNumPowerOfTwo(arrSize);
    if (!isArrSizePowerOfTwo) {
      arrSize = getNextPowerOfTwo(arrSize);
    }

    double[] dbuf = new double[arrSize];
    for (int pos = 0; pos < buf.length; pos += format.getFrameSize()) {
      int sample = format.isBigEndian()
          ? byteToIntBigEndian(buf, pos, format.getFrameSize())
          : byteToIntLittleEndian(buf, pos, format.getFrameSize());
      dbuf[pos / format.getFrameSize()] = sample;
    }

    if (!isArrSizePowerOfTwo) {
      for (int i = buf.length; i < arrSize; i++) {
        dbuf[i] = 0.0;
      }
    }
    return dbuf;
  }

  private static boolean isNumPowerOfTwo(int num) {
    return (num > 0) && ((num & (num - 1)) == 0);
  }

  private static int getNextPowerOfTwo(int num) {
    return (int) Math.pow(2, Math.ceil(Math.log(num) / Math.log(2)));
  }

  private static int byteToIntBigEndian(byte[] buf, int offset, int bytesPerSample) {
    int sample = 0;
    for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
      int aByte = buf[offset + byteIndex] & 0xff;
      sample += aByte << (8 * (bytesPerSample - byteIndex - 1));
    }
    return sample;
  }

  private static int byteToIntLittleEndian(byte[] buf, int offset, int bytesPerSample) {
    int sample = 0;
    for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
      int aByte = buf[offset + byteIndex] & 0xff;
      sample += aByte << 8 * (byteIndex);
    }
    return sample;
  }

  private static double[] convertFftToMagnitudes(Complex[] cps) {
    double[] magnitudes = new double[cps.length];
    for (int i = 0; i < cps.length; i++) {
      magnitudes[i] = cps[i].getMagnitude();
    }

    return magnitudes;
  }

  private static double findFrequency(double[] magnitudes, float sampleRate, int sampleSize) {
    int peakMagnitudeBin = getPeakMagnitudeBin(magnitudes);
    return getFrequencyFromBin(peakMagnitudeBin, sampleRate, sampleSize);
  }

  private static int getPeakMagnitudeBin(double[] magnitudes) {
    double peakMagnitude = Double.MIN_VALUE;
    int peakMagnitudeIdx = -1;
    for (int i = 0; i < magnitudes.length / 2 - 1; i++) {
      if (magnitudes[i] > peakMagnitude) {
        peakMagnitude = magnitudes[i];
        peakMagnitudeIdx = i;
      }
    }

    return peakMagnitudeIdx;
  }

  private static double getFrequencyFromBin(int peakMagnitudeBin, double sampleRate, int sampleSize) {
    return (peakMagnitudeBin * sampleRate) / sampleSize;
  }
}
