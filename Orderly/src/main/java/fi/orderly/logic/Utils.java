package fi.orderly.logic;

/**
 * Luokka tarjoaa työkaluja syötteiden validointiin.
 */
public class Utils {

    /**
     * Palauttaa true jos yksikään syötte on tyhjä
     * @param input tekstisyöte
     * @return löytyikö tyhjä syöte
     */
    public static boolean isEmpty(String[] input) {
        for (String string: input) {
            if (string == null || string.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Yrittää parsia tekstistä kokonaisluvun. Jos parsiminen tuottaa
     * poikkeuksen, catch lohko palauttaa true
     * @param input tekstisyöte
     * @return oliko tuote kokonaisluku
     */
    public static boolean notInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    /**
     * Yrittää parsia teksteistä kokonaislukuja. Jos parsiminen tuottaa
     * poikkeuksen, catch lohko palauttaa true
     * @param input tekstisyöteet
     * @return olivatko tuoteet kokonaislukuja
     */
    public static boolean notInt(String[] input) {
        for (String s: input) {
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    /**
     * Yrittää parsia teksteistä desimaalilukuja. Jos parsiminen tuottaa
     * poikkeuksen, catch lohko palauttaa true
     * @param input tekstisyöteet
     * @return oliko tuote desimaaliluku
     */
    public static boolean notDouble(String input) {
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
    /**
     * Yrittää parsia teksteistä desimaalilukuja. Jos parsiminen tuottaa
     * poikkeuksen, catch lohko palauttaa true
     * @param input tekstisyötteet
     * @return olivatko tuoteet kokonaislukuja
     */
    public static boolean notDouble(String[] input) {
        for (String s: input) {
            try {
                Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }
}
