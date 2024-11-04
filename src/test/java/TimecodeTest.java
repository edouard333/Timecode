
import com.phenix.timecode.Framerate;
import com.phenix.timecode.Timecode;
import java.util.InputMismatchException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class TimecodeTest {

    public TimecodeTest() {
    }

    /**
     * Ce qui se passe avant tous les tests.
     */
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * Ce qui se passe après tous les tests.
     */
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Exécuter avant chaque test.
     */
    @BeforeEach
    public void setUp() {
    }

    /**
     * Exécute après chaque test.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * On test avec aucun fichier d'accès.
     */
    @Test
    public void testTimecodeMauvais() {
        Timecode tc = new Timecode();

        assertEquals("-1", tc.toString(), "Quand il n'y a pas de timecode, cela doit retourner '-1'.");
        System.out.println("tc : " + tc.toString());

        InputMismatchException exception_input = assertThrows(InputMismatchException.class, () -> {
            Timecode tc2 = new Timecode("00:00:00:00:00");
        });

        assertNotEquals(null, exception_input.getMessage(), "Le message d'erreur ne peut pas être null.");

        NumberFormatException exception_format = assertThrows(NumberFormatException.class, () -> {
            Timecode tc3 = new Timecode("null");
        });

        assertNotEquals(null, exception_format.getMessage(), "Le message d'erreur ne peut pas être null.");
    }

    /**
     * On test de bon timecode.
     *
     * @throws Exception
     */
    @Test
    public void testTimecodeBon() throws Exception {
        Timecode tc_1h_24 = new Timecode("01:00:00:00", Framerate.F24);
        assertEquals("01:00:00:00", tc_1h_24.toString(), "Le timecode n'est pas juste.");
        assertEquals(24D, tc_1h_24.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(86400, tc_1h_24.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc_1h_25 = new Timecode("01:00:00:00", Framerate.F25);
        assertEquals("01:00:00:00", tc_1h_25.toString(), "Le timecode n'est pas juste.");
        assertEquals(25D, tc_1h_25.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(90000, tc_1h_25.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc_1h_30 = new Timecode("01:00:00:00", Framerate.F30);
        assertEquals("01:00:00:00", tc_1h_30.toString(), "Le timecode n'est pas juste.");
        assertEquals(30D, tc_1h_30.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(108000, tc_1h_30.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc_1h_2398 = new Timecode("01:00:00:00", Framerate.F23976);
        assertEquals("01:00:00:00", tc_1h_2398.toString(), "Le timecode n'est pas juste.");
        assertEquals(23.976D, tc_1h_2398.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(86400, tc_1h_2398.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc_2398 = new Timecode("03:00:42:13", Framerate.F23976);
        assertEquals(260221, tc_2398.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc_1h_2997nd = new Timecode("01:00:00:00", Framerate.F2997ND);
        assertEquals("01:00:00:00", tc_1h_2997nd.toString(), "Le timecode n'est pas juste.");
        assertEquals(29.97D, tc_1h_2997nd.getFramerate(), "Le framerate n'est pas juste.");
        assertFalse(tc_1h_2997nd.isDropFrame(), "Cela ne doit pas être drop-frame.");
        assertEquals(108000, tc_1h_2997nd.toImage(), "Le nombre d'image n'est pas juste.");
    }

    /**
     * On test de bon timecode.
     *
     * @throws Exception
     */
    @Test
    public void testTimecodeDropFrame() throws Exception {
        Timecode tc_2997 = new Timecode("00:00:00:00", Framerate.F2997);
        assertEquals("00:00:00;00", tc_2997.toString(), "Le timecode n'est pas juste.");
        assertEquals(29.97D, tc_2997.getFramerate(), "Le framerate n'est pas juste.");
        assertTrue(tc_2997.isDropFrame(), "Cela doit être drop-frame.");
        assertEquals(0, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        // 1min :
        tc_2997 = new Timecode("00:00:59:29", Framerate.F2997);
        assertEquals(1799, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(1799, Framerate.F2997);
        assertEquals("00:00:59;29", tc_2997.toString(), "Le timecode n'est pas juste.");

        tc_2997 = new Timecode("00:01:00:02", Framerate.F2997);
        assertEquals(1800, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(1800, Framerate.F2997);
        assertEquals("00:01:00;02", tc_2997.toString(), "Le timecode n'est pas juste.");

        // 2min :
        tc_2997 = new Timecode("00:01:59:29", Framerate.F2997);
        assertEquals(3597, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(3597, Framerate.F2997);
        assertEquals("00:01:59;29", tc_2997.toString(), "Le timecode n'est pas juste.");

        tc_2997 = new Timecode("00:02:00:02", Framerate.F2997);
        assertEquals(3598, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(3598, Framerate.F2997);
        assertEquals("00:02:00;02", tc_2997.toString(), "Le timecode n'est pas juste.");

        // 3min :
        tc_2997 = new Timecode("00:02:59:29", Framerate.F2997);
        assertEquals(5395, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(5395, Framerate.F2997);
        assertEquals("00:02:59;29", tc_2997.toString(), "Le timecode n'est pas juste.");

        tc_2997 = new Timecode("00:03:00:02", Framerate.F2997);
        assertEquals(5396, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(5396, Framerate.F2997);
        assertEquals("00:03:00;02", tc_2997.toString(), "Le timecode n'est pas juste.");

        // 5min :
        tc_2997 = new Timecode("00:04:59:29", Framerate.F2997);
        assertEquals(8991, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(8991, Framerate.F2997);
        assertEquals("00:04:59;29", tc_2997.toString(), "Le timecode n'est pas juste.");

        tc_2997 = new Timecode("00:05:00:02", Framerate.F2997);
        assertEquals(8992, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(8992, Framerate.F2997);
        assertEquals("00:05:00;02", tc_2997.toString(), "Le timecode n'est pas juste.");

        // 1h :
        tc_2997 = new Timecode("01:00:00:00", Framerate.F2997);
        assertEquals(107892, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(107892, Framerate.F2997);
        assertEquals("01:00:00;00", tc_2997.toString(), "Le timecode n'est pas juste.");

        // 2h :
        tc_2997 = new Timecode("02:00:00:00", Framerate.F2997);
        assertEquals(215784, tc_2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc_2997 = new Timecode(215784, Framerate.F2997);
        assertEquals("02:00:00;00", tc_2997.toString(), "Le timecode n'est pas juste.");
    }

    /**
     * On test différents timecode en drop-frame.
     */
    @Test
    public void testDropFrame() {
        Timecode from_image;
        Timecode from_smpte;

        for (int i = 0; i < /*24 **/ 60 * 60 * 30; i++) {
            from_image = new Timecode(i, Framerate.F2997);
            from_smpte = new Timecode(from_image.toString().replace(";", ":"), Framerate.F2997);

            boolean erreur = false;

            // En nombre d'image:
            if (i != from_image.toImage() || i != from_smpte.toImage()) {
                //System.out.println("Erreur (toImage) : " + i);
                erreur = true;
            }

            // En format SMPTE :
            if (!from_image.toString().equals(from_smpte.toString())) {
                //System.out.println("Erreur (toString) : " + i);
                erreur = true;
            }

            if (erreur) {
                System.out.println(i + " -> TC " + from_image.toImage() + " / " + from_image.toString() + " -> " + from_smpte.toImage() + " / " + from_smpte.toString());
            }
        }
    }

    /**
     * On test des changements de framerate.
     *
     * @throws Exception
     */
    @Test
    public void testChangemeFramerate() throws Exception {
        Timecode tc_1h_24 = new Timecode("01:00:00:00", Framerate.F24);
        assertEquals(86400, tc_1h_24.toImage(), "Le nombre d'image n'est pas juste.");

        Exception exception_start_tc = assertThrows(Exception.class, () -> {
            tc_1h_24.setStartTimecode(null);
            tc_1h_24.changeFramerate(25D);
        }, "L'erreur ne s'est pas lancée.");
        assertNotEquals(null, exception_start_tc.getMessage(), "Le message d'erreur ne peut pas être null.");

        tc_1h_24.setStartTimecode("00:00:00:00");
        tc_1h_24.changeFramerate(25D);

        assertEquals(86400, tc_1h_24.toImage(), "Le nombre d'image n'est pas juste.");
        assertEquals("00:57:36:00", tc_1h_24.toString(), "Le timecode n'est pas juste.");

        Timecode tc_1h_25 = new Timecode("01:00:00:00", Framerate.F25);
        assertEquals(90000, tc_1h_25.toImage(), "Le nombre d'image n'est pas juste.");
        tc_1h_25.changeFramerate(24D);
        assertEquals(90000, tc_1h_25.toImage(), "Le nombre d'image n'est pas juste.");
        assertEquals("01:02:30:00", tc_1h_25.toString(), "Le timecode n'est pas juste.");
    }
}
