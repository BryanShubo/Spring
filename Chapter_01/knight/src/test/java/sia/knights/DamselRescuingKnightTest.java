package sia.knights;

import org.junit.Test;

public class DamselRescuingKnightTest {

    @Test
    public void testKnightEmbarkOnQuest() {
        DamselRescuingKnight damselRescuingKnight = new DamselRescuingKnight();
        damselRescuingKnight.embarkOnQuest(); // You still cannot assert that the quest’s embark() method is called when the knight’s embarkOnQuest() method is called.
    }
}
