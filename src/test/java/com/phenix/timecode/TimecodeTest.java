package com.phenix.timecode;

import com.phenix.timecode.exception.TimecodeException;
import com.phenix.timecode.exception.TimecodeRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests de la classe {@link Timecode}.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public final class TimecodeTest {

    /**
     * Lance les tests.
     */
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
        //System.out.println("tc : " + tc.toString());

        TimecodeRuntimeException exceptionInput = assertThrows(TimecodeRuntimeException.class, () -> {
            Timecode tc2 = new Timecode("00:00:00");
        });

        assertNotNull(exceptionInput.getMessage(), "Le message d'erreur ne peut pas être null.");

        TimecodeRuntimeException exceptionFormat = assertThrows(TimecodeRuntimeException.class, () -> {
            Timecode tc3 = new Timecode("null");
        });

        assertNotNull(exceptionFormat.getMessage(), "Le message d'erreur ne peut pas être null.");
    }

    /**
     * On test de bon timecode.
     */
    @Test
    public void testTimecodeBon() {
        Timecode tc1h24 = new Timecode("01:00:00:00", Framerate.F24);
        assertEquals("01:00:00:00", tc1h24.toString(), "Le timecode n'est pas juste.");
        assertEquals(24D, tc1h24.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(86400, tc1h24.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc1h25 = new Timecode("01:00:00:00", Framerate.F25);
        assertEquals("01:00:00:00", tc1h25.toString(), "Le timecode n'est pas juste.");
        assertEquals(25D, tc1h25.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(90000, tc1h25.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc1h30 = new Timecode("01:00:00:00", Framerate.F30);
        assertEquals("01:00:00:00", tc1h30.toString(), "Le timecode n'est pas juste.");
        assertEquals(30D, tc1h30.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(108000, tc1h30.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc1h2398 = new Timecode("01:00:00:00", Framerate.F23976);
        assertEquals("01:00:00:00", tc1h2398.toString(), "Le timecode n'est pas juste.");
        assertEquals(23.976D, tc1h2398.getFramerate(), "Le framerate n'est pas juste.");
        assertEquals(86400, tc1h2398.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc2398 = new Timecode("03:00:42:13", Framerate.F23976);
        assertEquals(260221, tc2398.toImage(), "Le nombre d'image n'est pas juste.");

        Timecode tc1h2997nd = new Timecode("01:00:00:00", Framerate.F2997ND);
        assertEquals("01:00:00:00", tc1h2997nd.toString(), "Le timecode n'est pas juste.");
        assertEquals(29.97D, tc1h2997nd.getFramerate(), "Le framerate n'est pas juste.");
        assertFalse(tc1h2997nd.isDropFrame(), "Cela ne doit pas être drop-frame.");
        assertEquals(108000, tc1h2997nd.toImage(), "Le nombre d'image n'est pas juste.");
    }

    /**
     * On test de bon timecode.
     */
    @Test
    public void testTimecodeDropFrame() {
        Timecode tc2997 = new Timecode("00:00:00:00", Framerate.F2997);
        assertEquals("00:00:00;00", tc2997.toString(), "Le timecode n'est pas juste.");
        assertEquals(29.97D, tc2997.getFramerate(), "Le framerate n'est pas juste.");
        assertTrue(tc2997.isDropFrame(), "Cela doit être drop-frame.");
        assertEquals(0, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        // 1min :
        tc2997 = new Timecode("00:00:59:29", Framerate.F2997);
        assertEquals(1799, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(1799, Framerate.F2997);
        assertEquals("00:00:59;29", tc2997.toString(), "Le timecode n'est pas juste.");

        tc2997 = new Timecode("00:01:00:02", Framerate.F2997);
        assertEquals(1800, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(1800, Framerate.F2997);
        assertEquals("00:01:00;02", tc2997.toString(), "Le timecode n'est pas juste.");

        // 2min :
        tc2997 = new Timecode("00:01:59:29", Framerate.F2997);
        assertEquals(3597, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(3597, Framerate.F2997);
        assertEquals("00:01:59;29", tc2997.toString(), "Le timecode n'est pas juste.");

        tc2997 = new Timecode("00:02:00:02", Framerate.F2997);
        assertEquals(3598, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(3598, Framerate.F2997);
        assertEquals("00:02:00;02", tc2997.toString(), "Le timecode n'est pas juste.");

        // 3min :
        tc2997 = new Timecode("00:02:59:29", Framerate.F2997);
        assertEquals(5395, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(5395, Framerate.F2997);
        assertEquals("00:02:59;29", tc2997.toString(), "Le timecode n'est pas juste.");

        tc2997 = new Timecode("00:03:00:02", Framerate.F2997);
        assertEquals(5396, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(5396, Framerate.F2997);
        assertEquals("00:03:00;02", tc2997.toString(), "Le timecode n'est pas juste.");

        // 5min :
        tc2997 = new Timecode("00:04:59:29", Framerate.F2997);
        assertEquals(8991, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(8991, Framerate.F2997);
        assertEquals("00:04:59;29", tc2997.toString(), "Le timecode n'est pas juste.");

        tc2997 = new Timecode("00:05:00:02", Framerate.F2997);
        assertEquals(8992, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(8992, Framerate.F2997);
        assertEquals("00:05:00;02", tc2997.toString(), "Le timecode n'est pas juste.");

        // 1h :
        tc2997 = new Timecode("01:00:00:00", Framerate.F2997);
        assertEquals(107892, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(107892, Framerate.F2997);
        assertEquals("01:00:00;00", tc2997.toString(), "Le timecode n'est pas juste.");

        // 2h :
        tc2997 = new Timecode("02:00:00:00", Framerate.F2997);
        assertEquals(215784, tc2997.toImage(), "Le nombre d'image n'est pas juste.");

        tc2997 = new Timecode(215784, Framerate.F2997);
        assertEquals("02:00:00;00", tc2997.toString(), "Le timecode n'est pas juste.");
    }

    /**
     * On test différents timecode en drop-frame.
     */
    @Test
    public void testDropFrame() {
        Timecode fromImage;
        Timecode fromSmpte;

        for (int i = 0; i < /*24 **/ 60 * 60 * 30; i++) {
            fromImage = new Timecode(i, Framerate.F2997);
            fromSmpte = new Timecode(fromImage.toString().replace(";", ":"), Framerate.F2997);

            boolean erreur = false;

            // En nombre d'image:
            if (i != fromImage.toImage() || i != fromSmpte.toImage()) {
                //System.out.println("Erreur (toImage) : " + i);
                erreur = true;
            }

            // En format SMPTE :
            if (!fromImage.toString().equals(fromSmpte.toString())) {
                //System.out.println("Erreur (toString) : " + i);
                erreur = true;
            }

            if (erreur) {
                //System.out.println(i + " -> TC " + fromImage.toImage() + " / " + fromImage.toString() + " -> " + fromSmpte.toImage() + " / " + fromSmpte.toString());
            }
        }
    }

    /**
     * On test des changements de framerate.
     *
     * @throws TimecodeException
     */
    @Test
    public void testChangemeFramerate() throws TimecodeException {
        Timecode tc1h24 = new Timecode("01:00:00:00", Framerate.F24);
        assertEquals(86400, tc1h24.toImage(), "Le nombre d'image n'est pas juste.");

        Exception exceptionStartTc = assertThrows(Exception.class, () -> {
            tc1h24.setStartTimecode(null);
            tc1h24.changeFramerate(25D);
        }, "L'erreur ne s'est pas lancée.");
        assertNotNull(exceptionStartTc.getMessage(), "Le message d'erreur ne peut pas être null.");

        tc1h24.setStartTimecode("00:00:00:00");
        tc1h24.changeFramerate(25D);

        assertEquals(86400, tc1h24.toImage(), "Le nombre d'image n'est pas juste.");
        assertEquals("00:57:36:00", tc1h24.toString(), "Le timecode n'est pas juste.");

        Timecode tc1h25 = new Timecode("01:00:00:00", Framerate.F25);
        assertEquals(90000, tc1h25.toImage(), "Le nombre d'image n'est pas juste.");
        tc1h25.changeFramerate(24D);
        assertEquals(90000, tc1h25.toImage(), "Le nombre d'image n'est pas juste.");
        assertEquals("01:02:30:00", tc1h25.toString(), "Le timecode n'est pas juste.");
    }
}
