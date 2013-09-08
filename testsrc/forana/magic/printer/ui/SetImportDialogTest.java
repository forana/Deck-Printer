package forana.magic.printer.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SetImportDialogTest {
	@Test
	public void testGuessCardName() {
		assertEquals("Test", SetImportDialog.guessCardName("Test.png"));
		assertEquals("Test", SetImportDialog.guessCardName("Test.full.png"));
		assertEquals("S.N.O.T.", SetImportDialog.guessCardName("S.N.O.T..jpg"));
		assertEquals("Plains", SetImportDialog.guessCardName("Plains4"));
		assertEquals("Plains", SetImportDialog.guessCardName("Plains(4)"));
	}
}
