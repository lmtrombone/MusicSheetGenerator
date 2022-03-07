import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public class AudioReaderTest {
  @DataProvider
  public Object[][] audioReaderDataProvider() {
    return new Object[][] {
        { "src/test/resources/Piano.ff.C2.aif", 64.2827486708685, 66.54967490706109 },
        { "src/test/resources/Piano.ff.Db2.aif", 68.10519982075921, 70.5069245056401 },
        { "src/test/resources/Piano.ff.D2.aif", 72.15494574405969, 74.69948441050268 },
        { "src/test/resources/Piano.ff.Eb2.aif", 76.44550209132854, 79.1413469005951 },
        { "src/test/resources/Piano.ff.E2.aif", 80.9911881955289, 83.84733627906691 },
        { "src/test/resources/Piano.ff.F2.aif", 85.80717486147108, 88.83315834800463 },
        { "src/test/resources/Piano.ff.Gb2.aif", 90.90953499696329, 94.11545282508624 },
        { "src/test/resources/Piano.ff.G2.aif", 96.31529725464736, 99.71184887709215 },
        { "src/test/resources/Piano.ff.Ab2.aif", 102.04250286354403, 105.64102395561048 },
        { "src/test/resources/Piano.ff.A2.aif", 108.11026583997763, 111.92276613129549 },
        { "src/test/resources/Piano.ff.Bb2.aif", 114.53883677882871, 118.57804013471406 },
        { "src/test/resources/Piano.ff.B2.aif", 121.34967043801211, 125.62905732418487 },
        { "src/test/resources/Piano.ff.C3.aif", 128.565497341737, 133.09934981412218 },
        { "src/test/resources/Piano.ff.C4.aif", 257.130994683474, 266.19869962824436 },
        { "src/test/resources/Piano.ff.Db4.aif", 272.42079928303684, 282.02769802256034 },
        { "src/test/resources/Piano.ff.D4.aif", 288.61978297623875, 298.79793764201065 },
        { "src/test/resources/Piano.ff.Eb4.aif", 305.78200836531414, 316.56538760238044 },
        { "src/test/resources/Piano.ff.E4.aif", 323.9647527821156, 335.38934511626763 },
        { "src/test/resources/Piano.ff.F4.aif", 343.22869944588433, 355.33263339201847 },
        { "src/test/resources/Piano.ff.Gb4.aif", 363.63813998785315, 376.46181130034495 },
        { "src/test/resources/Piano.ff.G4.aif", 385.2611890185895, 398.8473955083686 },
        { "src/test/resources/Piano.ff.Ab4.aif", 408.1700114541761, 422.56409582244186 },
        { "src/test/resources/Piano.ff.A4.aif", 432.44106335991046, 447.69106452518196},
        { "src/test/resources/Piano.ff.Bb4.aif", 458.15534711531484, 474.31216053885623},
        { "src/test/resources/Piano.ff.B4.aif", 485.39868175204845, 502.516229296739 },
        { "src/test/resources/Piano.ff.C5.aif", 514.261989366948, 532.3973992564887 },
        { "src/test/resources/Piano.ff.C6.aif", 1028.523978733896, 1064.7947985129774 },
        { "src/test/resources/Piano.ff.Db6.aif", 1089.6831971321474, 1128.1107920902416 },
        { "src/test/resources/Piano.ff.D6.aif", 1154.479131904955, 1195.1917505680426 },
        { "src/test/resources/Piano.ff.Eb6.aif", 1223.1280334612566, 1266.2615504095215 },
        { "src/test/resources/Piano.ff.E6.aif",1295.8590111284625, 1341.5573804650705 },
        { "src/test/resources/Piano.ff.F6.aif", 1372.9147977835373, 1421.3305335680739 },
        { "src/test/resources/Piano.ff.Gb6.aif", 1454.5525599514126, 1505.8472452013798 },
        { "src/test/resources/Piano.ff.G6.aif", 1541.044756074358, 1595.3895820334744 },
        { "src/test/resources/Piano.ff.Ab6.aif", 1632.6800458167045, 1690.2563832897674 },
        { "src/test/resources/Piano.ff.A6.aif", 1729.764253439642, 1790.7642581007278 },
        { "src/test/resources/Piano.ff.Bb6.aif", 1832.621388461259, 1897.248642155425 },
        { "src/test/resources/Piano.ff.B6.aif", 1941.5947270081938, 2010.0649171869582 },
        { "src/test/resources/Piano.ff.C7.aif", 2057.047957467792, 2129.589597025955 },
    };
  }

  @Test(dataProvider = "audioReaderDataProvider")
  public void testAudioReaderForVariousNotes(String filePath, double lowerBoundFrequency, double upperBoundFrequency) throws UnsupportedAudioFileException, IOException {
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);
    Assert.assertTrue(isFrequencyFound(audioFrequencyInfo.getFrequencies(), lowerBoundFrequency, upperBoundFrequency));
  }

  @Test
  public void testAudioReader() throws UnsupportedAudioFileException, IOException {
    String filePath = "src/test/resources/Piano.ff.C4.aif";
    AudioFrequencyInfo audioFrequencyInfo = AudioReader.getAudioFrequencyInfo(filePath);

    AudioFormat audioFormat = audioFrequencyInfo.getAudioFormat();

    Assert.assertEquals(audioFormat.getSampleRate(), 44100);
    Assert.assertTrue(isFrequencyFound(audioFrequencyInfo.getFrequencies(), 257.130994683474, 266.19869962824436));
    Assert.assertEquals(audioFrequencyInfo.getSampleSize(), 65536);
  }

  private boolean isFrequencyFound(List<Double> frequenciesFound, double lowerBoundFrequency, double upperBoundFrequency) {
    return frequenciesFound.stream().anyMatch(f -> (f >= lowerBoundFrequency && f <= upperBoundFrequency));
  }
}
