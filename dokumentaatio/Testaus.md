# Testaus
Käyttöliittymä on jätetty yksikkötestien ulkopuolelle. Testit kattavat kaikki muut luokat. Testien kattavuus on hyvä.
Ohjelmaa on testattu manuaalisesti pitkin kehitystä. JavaFX elementit tuottivat vaikeuksia sen jälkeen, kun Java versio vaihdettiin 8:sta 11:een.
Lopullinen JDK on Zulu, joka sisältää JavaFX Java 8 tavoin, mutta aikaa ei jäänyt tämän hyödyntämiseen testauksessa.
Suuri osa virheellisistä syötteistä vältettiin tietokantatasolla.

## Yksikkötestaus
Testit on jaettu samalla konventiolla pakkauksiin kuin muukin koodi. Vaatimusmäärittelystä toteutuneet toiminnallisuudet on testattu. </br>
Myös muita interaktioita on testattu kuten tuotteiden tai huoneiden poistamisen vaikutus balance käyttöliittymä-tauluun.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Testi raportti.PNG" width="1000">
