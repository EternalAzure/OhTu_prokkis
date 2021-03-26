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


}
