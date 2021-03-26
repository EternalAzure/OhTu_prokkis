package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/*
        kortin saldo alussa oikein
        rahan lataaminen kasvattaa saldoa oikein
        rahan ottaminen toimii
        saldo vähenee oikein, jos rahaa on tarpeeksi
        saldo ei muutu, jos rahaa ei ole tarpeeksi
        metodi palauttaa true, jos rahat riittivät ja muuten false
*/
public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }

    @Test
    public void saldoAlussaOikein(){
        assertEquals("saldo: 0.10", kortti.toString());
    }

    @Test
    public void rahanLisaaminenKasvattaaSaldoaOikein(){
        kortti.lataaRahaa(100);
        assertEquals("saldo: 1.10", kortti.toString());
        kortti.lataaRahaa(190);
        assertEquals("saldo: 3.0", kortti.toString());
        kortti.lataaRahaa(-150);
        assertEquals("saldo: 1.50", kortti.toString());
    }

    @Test
    public void rahanOttaminenToimii(){
        kortti.otaRahaa(3);
        assertTrue(kortti.saldo() < 10);
    }

    @Test
    public void saldoVaheneeOikeinJosRahaaTarpeeksi(){
        kortti.otaRahaa(10);
        assertTrue(kortti.saldo() == 0);
    }

    @Test
    public void saldoEiMuutuJosEiTarpeeksiRahaa(){
        kortti.otaRahaa(200);
        assertTrue(kortti.saldo() == 10);
    }

    @Test
    public void oikeaBooleanArvo(){
        assertTrue(kortti.otaRahaa(3));
        assertFalse(kortti.otaRahaa(30));
    }

}
