import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioReader {
  private static final int BUF_SIZE = 8192;

  public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
    String filePath = "src\\main\\java\\Middle C.wav";
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
    byte[] buf = new byte[BUF_SIZE];
    AudioFormat audioFormat = audioInputStream.getFormat();
    while (audioInputStream.read(buf) != -1) {
      double[] samples = getSamples(buf, audioFormat);
      Complex[] fft = FastFourierTransform.computeFft(samples);
      double[] magnitudes = convertFftToMagnitudes(fft);

      double peakMagnitude = Double.MIN_VALUE;
      int peakMagnitudeIdx = -1;
      for (int i = 0; i < magnitudes.length / 2 - 1; i++) {
        if (magnitudes[i] > peakMagnitude) {
          peakMagnitude = magnitudes[i];
          peakMagnitudeIdx = i;
        }
      }

      double frequencyFound = (peakMagnitudeIdx * audioFormat.getSampleRate()) / samples.length;
      if ((254.0 < frequencyFound) || (frequencyFound < 268.0)) {
        System.out.println("Middle C found!");
      }
    }
  }

  private static double[] getSamples(byte[] buf, AudioFormat format) {
    int arrSize = buf.length / format.getFrameSize();
    boolean isArrSizePowerOfTwo = (arrSize & (arrSize - 1)) == 0;
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

  private static int byteToIntLittleEndian(byte[] buf, int offset, int bytesPerSample) {
    int sample = 0;
    for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
      int aByte = buf[offset + byteIndex] & 0xff;
      sample += aByte << 8 * (byteIndex);
    }
    return sample;
  }

  private static int byteToIntBigEndian(byte[] buf, int offset, int bytesPerSample) {
    int sample = 0;
    for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
      int aByte = buf[offset + byteIndex] & 0xff;
      sample += aByte << (8 * (bytesPerSample - byteIndex - 1));
    }
    return sample;
  }

  private static int getNextPowerOfTwo(int num) {
    return (int) Math.pow(2, Math.ceil(Math.log(num) / Math.log(2)));
  }

  private static double[] convertFftToMagnitudes(Complex[] cps) {
    double[] magnitudes = new double[cps.length];
    for (int i = 0; i < cps.length; i++) {
      double re = cps[i].getRe();
      double im = cps[i].getIm();
      magnitudes[i] = Math.sqrt(re * re + im * im);
    }

    return magnitudes;
  }
}
