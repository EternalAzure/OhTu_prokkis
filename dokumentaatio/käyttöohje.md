# Käyttöohjeet

[Laitoksen esittely](#laitoksen-esittely) </br>
[Huoneet](#huoneiden-lisääminen-ja-poistaminen) </br>
[Tuotteet](#tuotteiden-lisääminen-ja-poistaminen) </br>
[Toimitus](#saapuvan-toimituksen-luominen) </br>
[Vastaanottaminen](#vastaanottaminen) </br>
[Saldo](#saldon-muuttaminen) </br>
[Siirtäminen](#siirtäminen) </br>
[Kerääminen](#kerääminen) </br>
[Lähettäminen](#lähettäminen) </br>
[Testaaminen](#testaaminen) </br>


## Laitoksen esittely 

Esimerkki laitoksessa on 4 huonetta
1. Raaka-ainevarasto 1 (lämpö 4 astetta)
2. Raaka-ainevarasto 2 (lämpö 14 astetta)
3. Tuotantotila 1 (lämpö 14 astetta)
4. Valmisvarasto 1 (lämpö 4 astetta)

Sekä lisäksi vastaanottoterminaali ja lähtöterminaali
</br>
</br>
__Tavaran liikkeeseen liittyy 7 vaihetta:__
1. Tilaus
2. Vastaanotto
3. Säilytys
4. Siirtäminen
5. Valmistus
6. Keräys
7. Lähetys

## Ohjelman toiminnallisuudet

### Huoneiden lisääminen ja poistaminen
Ennen kuin laitos voi aloittaa toimintansa tulee sinne lisätä huoneet. Huoneen lisäämisen yhteydessä huoneelle voi määrittää lämpötilan. Jos tuotetta jonka lämpötila alittaa huoneenlämmön ohjelma varoittaa siitä käyttäjää. Tämä ominaisuus odotta yhä toteuttamista. Huoneita voi lisätä vain yhden samaa nimeä. Poistettaessa huonetta kaikki viittaukset huoneeseen poistuvat tietokannasta. Tämä voi aiheuttaa suurta harmia. Lisääminen ja poistaminen tapahtuvat sinisestä valikosta valitun työtilan kautta. _Add new room_ ja _Remove room_

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Add new room.PNG" width="500">

### Tuotteiden lisääminen ja poistaminen
Seuraavaksi lisätään tuotteet, sillä tuotteen lisääminen vaatii ensin huoneen, joka asetetaan oletus varastohuoneeksi tuotteelle. Oletus huone helpottaa tavaran vastaanottoa ja pitää huolen, että tavara ei jää vastaanottamatta. Poistettaessa kaikki viitteet katoavat tietokannasta.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Add new product.PNG" width="500">

### Saapuvan toimituksen luominen
Tilaus jää ohjelman ulkopuolelle, mutta tilauksen yhteydessä luodaan tilausta vastaava uusi saapuva toimitus. Sinisestä valikosta löytyy _New shipment_, jota painamalla aukeaa oikealle sitä vastaava työtila. Toimitukset tallennetaan rivi kerrallaan tietokantaan. Toimituksen luomisen yhteydessä tuotteeseen liitetään eränumero (eng. _batch/batch number_) Tuotteiden ja erien suhde on monen suhde moneen. Tietokantaan ei kuitenkaan voi syöttää kahta kertaa samaa tuote-erä yhdistelmää. viimeisenä toimitukseen liitetään tilattu määrä. Tämä auttaa terminaalityöntekijää sekä vastaanottamaan toimituksen sekä nopeasti tarkistamaan jos jotain puuttuu.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/New shipment.PNG" width="500">
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/shipments data.PNG" width="500">
Tietokanta näyttää tältä kun olemme lisänneet toimitukseen viisi eri tuotetta kaikki samalla erällä.


### Vastaanottaminen
Valitse ensin valikosta _Receive shipment_ ja syötä jokin olemassa oleva toimitusnumero. Tämän jälkeen ohjelma näyttää tietokannasta toimituksen sekä muita hyödyllisiä tietoja, jotka auttavat käyttäjää. Syötä määrä ja valitse oikea varasto.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Receive shipment.PNG" width="500">

### Saldon muuttaminen
Aina välillä sattuu virheitä ja saldoa pitää muuttaa. _Change balance_ sallii muuttaa saldoa kunhan täyttää tiedot. Toistaiseksi ei ole mahdollisuutta kumulatiiviseen saldon muuttamiseen.

### Siirtäminen
Siirtäminen tarkoittaa tavaran siirtämistä huoneesta toiseen. Tämä tapahtuu ainakin kerran esimerkki laitoksessa, kun raaka-aineet siirretään tuotantoon. Pääset siirtämään valitsemalla _Transfer_

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Transfer.PNG" width="500">

### Valmistus
Valmistuksessa valitaan kuinka monta valmista tuotetta luodaan. Luomisen jälkeen tuotantotilasta poistetaan vastaavamäärä raaka-aineita. Myöhemmin on tarkoitus tilastoida tätä tapahtumaa. Valmiit tuotteet lisätään esimerkkilaitoksessamme _Valmisvarastoon_. Ominaisuus työn alla.

### Lähetyksen luominen
Jotta lähetys voidaan lähettää täytyy se luoda. _New delivery_ vie näkymään, jossa voi toimituksen tapaan luoda uusia rivejä lähetykset tauluun tietokannassa.

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/New delivery.PNG" width="500">

### Kerääminen
_Collect delivery_ vie näkymään, joka kysyy lähetysnumeroa. Annettuasi olemassa olevan lähetysnumeron, voit kerätä tuotteita, jotka ovat merkitty kyseiseen lähetykseen. Tuotteet ja niiden määrät tallennetaan lähetystä varten

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Collect delivery.PNG" width="500">

### Lähettäminen
_Send delivery_ kysyy numeroa ja annettuasi numeron lähetys lähtee ja lähetykseen merkityt määrät poistetaan saldosta. Jos saldo ylittyy merkitään saldoksi nolla ja virhe kirjataan ylös. 

## Testaaminen
Ylhäällä on harmaa valikkopalkki josta löytyy _Populate_. Se 'kansoittaa' tietokanta taulut testidatalla.</br>

| Huone             | Lämpö|
|-------            |------|
|Raaka-ainevarasto 1|4|
|Raaka-ainevarasto 2|14|
|Tuotantotila 1     |14|
|Valmisvarasto 1    |4|

| Tuotenimi       | Lämpö| Huone id| koodi| yksikkö|
|-------          |------| --      |  --- |    --- |
|Kaali            |4     |1        | 1000 |KG|
|Porkkana         |4     |1        | 2000 |KG|
|Peruna           |4     |1        | 3000 |KG|
|Kurpitsa         |14    |2        | 4000 |KG|
|Sipuli           |14    |2        | 5000 |KG|
|Kaalilaatikko    |4     |4        | 1100 |KG|
|Porkkanasuikaleet|4     |4        | 2200 |KG|
|Perunamuussi     |4     |4        | 3300 |KG|
|Kurpitsapalat    |4     |4        | 4400 |KG|
|Sipulirenkaat    |4     |4        | 5500 |KG|

|Saapuva toimitus| tuote id | erä | määrä |
|----------------|----------|-----|-------|
|1               | 1        |  1  | 10    |
|1               | 2        |  1  | 40    |
|1               | 3        |  1  | 160   |
|1               | 4        |  1  | 640   |
|1               | 5        |  1  | 2560  |

|Lähtevä toimitus| tuote id | määrä |
|----------------|----------|-------|
|1               | 1        | 5     |
|1               | 2        | 20    |
|1               | 3        | 80    |
|1               | 4        | 320   |
|1               | 5        | 1280  |
