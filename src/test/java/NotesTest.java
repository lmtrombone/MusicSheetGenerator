import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NotesTest {
  @Test
  public void testFindKeyNumForValidFrequency() {
    int actualKeyNum = Notes.findKeyNumFromFrequency(257.8125);
    int expectedKeyNum = 40;

    Assert.assertEquals(actualKeyNum, expectedKeyNum);
  }

  @Test
  public void testFindKeyNumForInvalidFrequency() {
    int actualKeyNum = Notes.findKeyNumFromFrequency(255.0);
    int expectedKeyNum = -1;

    Assert.assertEquals(actualKeyNum, expectedKeyNum);
  }

  @DataProvider
  public Object[][] keyNumPitchData() {
    return new Object[][] {
        { 1, "A0" },
        { 4, "C1" },
        { 10, "F#/Gb1" },
        { 15, "B1" },
        { 16, "C2"},
        { 88, "C8"}
    };
  }

  @Test(dataProvider = "keyNumPitchData")
  public void testScientificPitchNotation(int keyNum, String expectedScientificPitchNotation) {
    String actualScientificPitchNotation = Notes.getScientificPitchNotation(keyNum);
    Assert.assertEquals(actualScientificPitchNotation, expectedScientificPitchNotation);
  }
}
