import org.testng.Assert;
import org.testng.annotations.Test;

public class NotesTest {
  @Test
  public void testGetNoteForValidFrequency() {
    int actualKeyNum = Notes.findKeyNumFromFrequency(257.8125);
    int expectedKeyNum = 40;

    Assert.assertEquals(actualKeyNum, expectedKeyNum);
  }

  @Test
  public void testGetNoteForInvalidFrequency() {
    int actualKeyNum = Notes.findKeyNumFromFrequency(255.0);
    int expectedKeyNum = -1;

    Assert.assertEquals(actualKeyNum, expectedKeyNum);
  }
}
