import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioReader {
  private static final int BUF_SIZE = 262144;
  private static final double C5_FREQUENCY = 523.2511;

  public static AudioFrequencyInfo getAudioFrequencyInfo(String filePath) throws UnsupportedAudioFileException, IOException {
    AudioFrequencyInfo audioFrequencyInfo = new AudioFrequencyInfo();
    List<Double> frequenciesFound = new ArrayList<>();
    audioFrequencyInfo.setFrequencies(frequenciesFound);

    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath))) {
      AudioFormat audioFormat = audioInputStream.getFormat();
      audioFrequencyInfo.setAudioFormat(audioFormat);
      audioFrequencyInfo.setSampleSize(BUF_SIZE / audioFormat.getFrameSize());
      double[] hanningWindow = getHanningWindow(audioFrequencyInfo.getSampleSize());

      byte[] buf = new byte[BUF_SIZE];
      int bytesRead;
      while ((bytesRead = audioInputStream.read(buf)) != -1) {
        double[] samples = getSamples(buf, bytesRead, audioFormat);
        Complex[] fft = FastFourierTransform.computeFft(samples);

        double[] magnitudes = convertFftToMagnitudes(fft);
        float sampleRate = audioFormat.getSampleRate();
        int sampleSize = samples.length;
        double initialFrequencyFound = findFrequency(magnitudes, sampleRate, sampleSize);

        if (initialFrequencyFound > C5_FREQUENCY) {
          frequenciesFound.add(initialFrequencyFound);
        } else {
          // For notes of lower frequencies, the peak magnitude does not necessarily translate to the fundamental pitch.
          // To determine this, the Harmonic Product Spectrum is used.  However, this approach still has a limitation
          // and may not work for low frequencies due to noise.
          double[] magnitudesFromDownSampledFfts = getMagnitudesFromDownSampledFfts(fft, hanningWindow);
          double frequencyFound = findFrequency(magnitudesFromDownSampledFfts, sampleRate, sampleSize);
          frequenciesFound.add(frequencyFound);
        }
      }
    }

    return audioFrequencyInfo;
  }

  private static double[] getHanningWindow(int samplesLength) {
    double[] hanningWindow = new double[samplesLength];
    for (int i = 0; i < samplesLength; i++) {
      hanningWindow[i] = 0.5 * (1 - Math.cos((2 * Math.PI * i) / samplesLength));
    }

    return hanningWindow;
  }

  private static double[] getSamples(byte[] buf, int bytesRead, AudioFormat format) {
    int arrSize = buf.length / format.getFrameSize();
    double[] dbuf = new double[arrSize];
    for (int pos = 0; pos < bytesRead; pos += format.getFrameSize()) {
      int sample = format.isBigEndian()
          ? byteToIntBigEndian(buf, pos, format.getFrameSize())
          : byteToIntLittleEndian(buf, pos, format.getFrameSize());
      dbuf[pos / format.getFrameSize()] = sample;
    }

    if (bytesRead != buf.length) {
      for (int i = bytesRead; i < arrSize; i++) {
        dbuf[i] = 0.0;
      }
    }
    return dbuf;
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

  private static double[] getMagnitudesFromDownSampledFfts(Complex[] fft, double[] hanningWindow) {
    double[] downSampledFftMagnitudes2 = getDownSampledFftMagnitudes(fft, 2);
    double[] downSampledFftMagnitudes3 = getDownSampledFftMagnitudes(fft, 3);
    double[] downSampledFftMagnitudes4 = getDownSampledFftMagnitudes(fft, 4);
    double[] downSampledFftMagnitudes5 = getDownSampledFftMagnitudes(fft, 5);

    double[] magnitudes = new double[fft.length];
    for (int i = 0; i < fft.length; i++) {
      magnitudes[i] = fft[i].getMagnitude() * hanningWindow[i];
      magnitudes[i] *= downSampledFftMagnitudes2[i];
      magnitudes[i] *= downSampledFftMagnitudes3[i];
      magnitudes[i] *= downSampledFftMagnitudes4[i];
      magnitudes[i] *= downSampledFftMagnitudes5[i];
    }

    return magnitudes;
  }

  private static double[] getDownSampledFftMagnitudes(Complex[] fft, int downSampledRate) {
    double[] downSampledFftMagnitudes = new double[fft.length];
    int downSampledFftSize = fft.length / downSampledRate - 1;

    for (int i = 0; i < fft.length; i++) {
      if (i <= downSampledFftSize) {
        Complex complex = fft[downSampledRate * i];
        for (int j = 0; j < downSampledRate; j++) {
          complex = Complex.add(complex, fft[downSampledRate * i + j]);
        }
        complex = new Complex(complex.getRe() / downSampledRate, complex.getIm() / downSampledRate);

        downSampledFftMagnitudes[i] = complex.getMagnitude();
      } else {
        downSampledFftMagnitudes[i] = new Complex(1.0, 1.0).getMagnitude();
      }
    }

    return downSampledFftMagnitudes;
  }
}
