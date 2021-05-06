# Käyttöohjeet

## Laitoksen esittely 

Esimerkki laitoksessa on 4 huonetta
1. Raaka-ainevarasto 1 (lämpö 4 astetta)
2. Raaka-ainevarasto 2 (lämpö 14 astetta)
3. Tuotantotila 1 (lämpö 14 astetta)
4. Valmisvarasto 1 (lämpö 4 astetta)

Sekä lisäksi vastaanottoterminaali ja lähtöterminaali
</br>
</br>
__Tavaran liikkeeseen liittyy 8 vaihetta:__
1. Tilaus
2. Vastaanotto
3. Säilytys
4. Liikutus
5. Valmistus
6. Liikutus
7. Keräys
8. Lähetys

## Ohjelman toiminnallisuudet

### Saapuvan toimituksen luominen
Tilaus jää ohjelman ulkopuolelle, mutta tilauksen yhteydessä luodaan tilausta vastaava uusi saapuva toimitus. Sinisestä valikosta löytyy _New shipment_, jota painamalla aukeaa oikealle sitä vastaava työtila. Toimitukset tallennetaan rivi kerrallaan tietokantaan. Toimituksen luomisen yhteydessä tuotteeseen liitetään eränumero (eng. _batch/batch number_) Tuotteiden ja erien suhde on monen suhde moneen. Tietokantaan ei kuitenkaan voi syöttää kahta kertaa samaa tuote-erä yhdistelmää. viimeisenä toimitukseen liitetään tilattu määrä. Tämä auttaa terminaalityöntekijää sekä vastaanottamaan toimituksen sekä nopeasti tarkistamaan jos jotain puuttuu.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Hub.PNG" width="500">
