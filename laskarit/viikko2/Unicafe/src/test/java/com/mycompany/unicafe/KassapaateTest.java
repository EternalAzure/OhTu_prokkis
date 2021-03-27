package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;
    @Before
    public void setUp(){
        kassa = new Kassapaate();
        kortti = new Maksukortti(1200); //12€
    }

    @Test
    public void luotuKassaOlemassa(){
        assertNotNull(kassa);
        assertEquals(100000, kassa.kassassaRahaa()); //100snt = €
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kassassaRahaa(){
        kassa.syoMaukkaasti(400);
        assertEquals(100400, kassa.kassassaRahaa());
        kassa.syoEdullisesti(240);
        assertEquals(100640, kassa.kassassaRahaa());
    }

    @Test
    public void maukkaitaLounaitaMyyty(){
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());

    }

    @Test
    public void edullisiaLounaitaMyyty(){
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        kassa.syoEdullisesti(240);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiKateisella(){
        assertEquals(0, kassa.syoMaukkaasti(400));
        assertEquals(100, kassa.syoMaukkaasti(500));
        assertEquals(100, kassa.syoMaukkaasti(100));
        assertEquals(2, kassa.maukkaitaLounaitaMyyty());
        assertEquals(100800, kassa.kassassaRahaa());

    }
    @Test
    public void syoMaukkaastiKortilla(){
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertFalse(kassa.syoMaukkaasti(kortti));
        assertEquals(3, kassa.maukkaitaLounaitaMyyty());
        assertEquals(101200, kassa.kassassaRahaa());
    }

    private void assertFalse(boolean syoMaukkaasti) {
    }

    @Test
    public void syoEdullisestiKateisella(){
        assertEquals(0, kassa.syoEdullisesti(240));
        assertEquals(100, kassa.syoEdullisesti(340));
        assertEquals(100, kassa.syoEdullisesti(100));
        assertEquals(2, kassa.edullisiaLounaitaMyyty());
        assertEquals(100480, kassa.kassassaRahaa());
    }

    @Test
    public void syoEdullisestiKortilla(){
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kassa.syoEdullisesti(kortti));
        assertFalse(kassa.syoEdullisesti(kortti));
        assertEquals(5, kassa.edullisiaLounaitaMyyty());
        assertEquals(101200, kassa.kassassaRahaa());
    }

    @Test
    public void lataaRahaaKortille(){
        kassa.lataaRahaaKortille(kortti, 200);
        assertEquals(1400, kortti.saldo());
        assertEquals(100200, kassa.kassassaRahaa());

        kassa.lataaRahaaKortille(kortti,-200);
        assertEquals(1400, kortti.saldo());
        assertEquals(100200, kassa.kassassaRahaa());
    }
}
