package com.phenix.timecode;

import com.phenix.timecode.exception.SameNumberPartException;
import com.phenix.timecode.exception.TimecodeException;
import com.phenix.timecode.exception.TimecodeRuntimeException;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        for (int i = 0; i < /*24 **/ 60 * 60 * 30; i++) {
            Timecode fromImage = new Timecode(i, Framerate.F2997);
            Timecode fromSmpte = new Timecode(fromImage.toString().replace(";", ":"), Framerate.F2997);

            boolean erreur = false;

            // En nombre d'image:
            if (i != fromImage.toImage() || i != fromSmpte.toImage()) {
                erreur = true;
            }

            // En format SMPTE :
            if (!fromImage.toString().equals(fromSmpte.toString())) {
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
    public void testChangementFramerate() throws TimecodeException {
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

    /**
     * Représente un nombre en "digit".
     *
     * @deprecated Utiliser la fonction standard créée.
     *
     * @param valeur La valeur à convertir en digit (0-9).
     * @return String
     */
    @Deprecated
    @NotNull
    private static String digit(int valeur) {
        return (valeur <= 9) ? "0" + valeur : "" + valeur;
    }

    /**
     * On test la création de bobine "à la main".
     *
     * @throws TimecodeException
     */
    @Test
    public void testCreationBobine() throws TimecodeException {
        Framerate framerate = Framerate.F24;
        Part reel1 = new Part(1, "R01", new Timecode("00:11:00:00", framerate), new Timecode("01:00:00:00", framerate));
        Part reel2 = new Part(2, "R02", new Timecode("00:12:00:00", framerate), new Timecode("02:00:00:00", framerate));
        Part reel3 = new Part(3, "R03", new Timecode("00:13:00:00", framerate), new Timecode("03:00:00:00", framerate));
        Part reel4 = new Part(4, "R04", new Timecode("00:14:00:00", framerate), new Timecode("04:00:00:00", framerate));
        Part reel5 = new Part(5, "R05", new Timecode("00:15:00:00", framerate), new Timecode("05:00:00:00", framerate));
        Part reel6 = new Part(6, "R06", new Timecode("00:16:00:00", framerate), new Timecode("06:00:00:00", framerate));

        List<Part> listeReel = new ArrayList<Part>();
        listeReel.add(reel1);
        listeReel.add(reel2);
        listeReel.add(reel3);
        listeReel.add(reel4);
        listeReel.add(reel5);
        listeReel.add(reel6);

        // Ne doit pas créer d'erreur.
        assertDoesNotThrow(() -> Part.checkSameNumero(listeReel));

        // On ajoute une deuxième fois la reel 6 pour générer une erreur.
        listeReel.add(reel6);

        SameNumberPartException exception = assertThrows(SameNumberPartException.class, () -> {
            Part.checkSameNumero(listeReel);
        }, "L'erreur ne s'est pas produite.");

        assertNotNull(exception.getMessage());
        // On doit avoir qu'une entrée :
        assertEquals(1, exception.getListePartieDuplique().size());

        // Cela doit être le numéro 6, et doit avoir 2 parties concernées.
        assertEquals(2, exception.getListePartieDuplique().get(6).size());

        // Cela doit être le numéro 6, et le 1er élement doit être le numéro 6.
        assertEquals(6, exception.getListePartieDuplique().get(6).getFirst().getNumero());
    }

    /**
     * Vérifie l'égalité de 2 timecodes : sur le conversion en {@code String},
     * le framerate et le start timecode.
     *
     * @param expected Ce qui est attendu.
     * @param actual Le timecode à vérifier.
     */
    private void testEqualsTimecode(Timecode expected, Timecode actual) {
        assertNotNull(actual, "On doit avoir un timecode, et pas null.");
        assertEquals(expected.toString(), actual.toString());
        assertEquals(expected.getFramerate(), actual.getFramerate());
        assertEquals(expected.getStartTimecode(), actual.getStartTimecode());
    }

    /**
     * On test la création de bobine.
     *
     * @throws TimecodeException
     */
    @Test
    public void testCreationListeBobine() throws TimecodeException {
        Framerate framerate = Framerate.F24;
        Timecode[] dureeBobine = new Timecode[6];
        dureeBobine[0] = new Timecode("00:18:02:04", framerate);
        dureeBobine[1] = new Timecode("00:10:33:02", framerate);
        dureeBobine[2] = new Timecode("00:15:42:00", framerate);
        dureeBobine[3] = new Timecode("00:15:51:16", framerate);
        dureeBobine[4] = new Timecode("00:12:12:16", framerate);
        dureeBobine[5] = new Timecode("00:17:49:12", framerate);

        // Vérifie la création des bobines :
        List<Part> listeReel = Part.createListReel(dureeBobine);
        assertEquals(6, listeReel.size());

        for (int i = 0; i < listeReel.size(); i++) {
            Part reel = listeReel.get(i);
            assertEquals((i + 1), reel.getNumero());
            assertEquals("R" + digit(i + 1), reel.getNom());
            assertEquals(dureeBobine[i].toString(), reel.getDuree().toString());
            assertEquals(digit(i + 1) + ":00:00:00", reel.getStartTimecode().toString());
        }

        // == On fait les calcules de TC continu et bobine. ==
        // TC 01 (bobine 01) :
        String startTimecodeContinu = "01:00:00:00";

        Timecode timecodeContinu = new Timecode("01:00:00:00", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        Timecode timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("01:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 01 = start tc 01h.
        assertEquals("01:00:00:00", timecodeReel.getStartTimecode());

        Timecode timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);

        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 02 (bobine 01) :
        timecodeContinu = new Timecode("01:12:02:19", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("01:12:02:19", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 01 = start tc 01h.
        assertEquals("01:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 03 (bobine 01) :
        timecodeContinu = new Timecode("01:18:02:03", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("01:18:02:03", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 01 = start tc 01h.
        assertEquals("01:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 04 (bobine 02) :
        timecodeContinu = new Timecode("01:18:02:04", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("02:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 02 = start tc 02h.
        assertEquals("02:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 05 (bobine 02) :
        timecodeContinu = new Timecode("01:18:02:05", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("02:00:00:01", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 02 = start tc 02h.
        assertEquals("02:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 06 (bobine 02) :
        timecodeContinu = new Timecode("01:28:35:05", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("02:10:33:01", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 02 = start tc 02h.
        assertEquals("02:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 07 (bobine 03) :
        timecodeContinu = new Timecode("01:28:35:06", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("03:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 03 = start tc 03h.
        assertEquals("03:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 08 (bobine 03) :
        timecodeContinu = new Timecode("01:28:35:07", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("03:00:00:01", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 03 = start tc 03h.
        assertEquals("03:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 09 (bobine 03) :
        timecodeContinu = new Timecode("01:44:17:05", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("03:15:41:23", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 03 = start tc 03h.
        assertEquals("03:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 10 (bobine 04) :
        timecodeContinu = new Timecode("01:44:17:06", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("04:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 04 = start tc 04h.
        assertEquals("04:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 11 (bobine 04) :
        timecodeContinu = new Timecode("01:44:17:07", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("04:00:00:01", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 04 = start tc 04h.
        assertEquals("04:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 12 (bobine 04) :
        timecodeContinu = new Timecode("02:00:08:21", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("04:15:51:15", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 04 = start tc 04h.
        assertEquals("04:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 13 (bobine 05) :
        timecodeContinu = new Timecode("02:00:08:22", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("05:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 05 = start tc 05h.
        assertEquals("05:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 14 (bobine 05) :
        timecodeContinu = new Timecode("02:00:08:23", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("05:00:00:01", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 05 = start tc 05h.
        assertEquals("05:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 15 (bobine 05) :
        timecodeContinu = new Timecode("02:12:21:13", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("05:12:12:15", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 05 = start tc 05h.
        assertEquals("05:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 16 (bobine 06) :
        timecodeContinu = new Timecode("02:12:21:14", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("06:00:00:00", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 06 = start tc 06h.
        assertEquals("06:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 17 (bobine 06) :
        timecodeContinu = new Timecode("02:23:54:13", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("06:11:32:23", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 06 = start tc 06h.
        assertEquals("06:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 18 (bobine 06) :
        timecodeContinu = new Timecode("02:30:11:01", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        assertEquals("06:17:49:11", timecodeReel.toString());
        assertEquals(framerate.value, timecodeReel.getFramerate());
        // Bobine 06 = start tc 06h.
        assertEquals("06:00:00:00", timecodeReel.getStartTimecode());

        timecodeContinuRetour = Timecode.timecodeReelToContinu(timecodeReel, listeReel, startTimecodeContinu);
        
        // Si on fait le processus inverse, on doit revenir à l'origine.
        testEqualsTimecode(timecodeContinu, timecodeContinuRetour);

        // TC 19 (bobine 06 - hors bobine) :
        timecodeContinu = new Timecode("02:30:11:02", framerate);
        timecodeContinu.setStartTimecode(startTimecodeContinu);

        // Conversion continu en bobine.
        timecodeReel = Timecode.timecodeContinuToReel(timecodeContinu, listeReel);

        // On ne doit pas récupérer de TC.
        assertNull(timecodeReel);
    }
}
