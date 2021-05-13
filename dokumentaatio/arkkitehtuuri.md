# Arkkitehtuuri

##### Sisältö

[Grafiikan hakeminen ja esittäminen](#grafiikan-hakeminen-ja-esittäminen) </br>
[Tietokanta osaksi näkymää](#tietokanta-osaksi-näkymää) </br>
[Tietokannan taulut](#tietokannan-taulut) </br>
[Toiminnallisuus ja logiikka](#toiminnallisuus-ja-logiikka) </br>
[Sekvenssikaavioita grafiikasta](#sekvenssikaavioita-grafiikasta) </br>

## Grafiikan hakeminen ja esittäminen

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/GettingLayout.PNG" width="500">
</br>

_Hub_-luokka on ainoa luokka, joka näyttää käyttäjälle grafiikkaa. Hub sisältää UI elementit harmaaseen</br> 
yläpalkkiin ja siniseen valikkoon. Päätin eriyttää valkoisen työtilan grafiikan omaksi luokakseen _Workspaces._ </br>
Luokassa Workspaces on yksi metodia jokaista työtilaa kohti joka on valittavissa valikosta. Jokainen metodi </br>
palauttaa VBox olion. Tämä VBox olio asetetaan Hubissa olevan _workspaceParent_ nimisen BorderPane olion keskelle, </br>
jolloin se näkyy käyttäjälle. </br>
Jokaisella valikon hyperlinkillä on lambda joka asettaa setWorkspace() parametriksi sopivan metodin kuten </br>
```event -> setWorkSpace(workSpaces.addRoomWorkspace())``` </br>

### Tietokanta osaksi näkymää

Aina näkymälle ei löydy suoraa vastinetta tietokannasta. Näin ollen olen toteuttanut pakkaukseen _dao_ </br>
luokkia joiden tehtävänä on rakentaa sopiva tietorakenne ja tarjota pääsy tähän tietoon. </br>
_Shipment_-luokka sisältää metodit tiedon hakemiseen, yhdistämiseen, ja tarjoamiseen. Shipment rakentaa </br>
_DataPackage_-aliluokan avulla käyttäjälle näkyvään taulukkoon rivejä, jotka se tallettaa listaan. </br>
Listaan tarjotaan pääsy getDataPackage(int index) kautta, joka mahdollistaa listan helpon iteroimisen. </br>
DataPackage-aliluokka omaa vain gettereitä. Syy toteuttaa DataPackage aliluokkana taulukon sijaan on </br> 
inhimmillisten virheiden minimointi. Tulee vähemmän sekaannuksia, kun kutsuu getName() list[i] sijaan. </br>

### Tietokannan taulut

Harmaasta valikkopalkista on valittavissa näyttää 5 eri taulua tietokannasta _warehouse_. Jokainen taulu saa oman ikkunansa, jota on mahdollista liikuttaa ja jonka kokoa on mahdollista muuttaa. Jokaisesta taulusta voi olla yhdenaikaisesti vain yksi ikkuna. </br>
</br>
#### Toteutus
Kaksi osaa: _dao.tables_ ja _ui.tables_
##### ui.tables
Paketissa on luokka _TableViewInfiniteScrolling_, josta kaikki viisi _*TableView_-luokkaa perivät. Kaikille yhteinen käytös on display(), getVerticalScrollbar(TableView<?> table) ja scrolled(). Näiden tehtävä on vain näyttää taulun sisältöä. Luokassa on kaksi tärkeää muuttujaa
```
ObservableList<ITable> items = FXCollections.observableArrayList();
    TableView<ITable> table = new TableView<>();
```
Luokille yksilöllistä käytöstä on asettaa data listaan _items_ addItems() metodilla ja asettaa sarakkeet tauluun _table_ setUp() metodilla.

##### dao.tables
Jokainen paketin luokka toteuttaa tyhjää rajapintaa _ITable_. Luokissa on gettereitä ja settereitä ```StringProperty```ille sekä fetchData(int id), jota kutsutaan konstruktorista. fecthData(int id) hakee tietokannasta yhden rivin parametrin id perusteella. Tarkoitus oli saada taulut reagoimaan muutoksiin tietokannassa, mutta tämä ei ole vielä toteutunut.

## Toiminnallisuus ja logiikka

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Functionality.PNG" width="500">
</br>
Poislukien sinisen valikon _HubController_ vastaa käyttäjän toiveisiin vastaavasta toiminnallisuudesta. </br>
Koodin toisteisuuden vähentämiseksi _Utils_-luokka sisältää käteviä pikku metodeja, joita hyödynnetään </br>
vähän siellä täällä. Kaikki toiminnallisuus on pyrittä toteuttamaan siten, että palveluntarjoajan ei tarvitse </br>
tuntea palvelun pyytäjää. Tarvittavat riippuvuudet toteutetaan injektioina. Parhaana esimerkkinä Statement olio, </br>
joka luodaan ensimmäisen kerran yhdessä paikassa ja siitä eteenpäin annetaan eteenpäin tarvittaessa. </br>
Tämä helpottaa mm. testaamista, kun käytössä on testitietikanta. </br>
Vaikeuksia on tuottanut virheilmoitusten saaminen logiikalta käyttöliittymälle. Tästä syystä lähes kaikki </br>
arvot mukaan lukien boolean käsitellään string muodossa. Vaihtoehtoinen toteutus olisi tehtä lisää luokkia. </br>
Palautusarvona toimiva luokka sisältäisi esimerkiksi booleanin ja virheilmoituksen. </br>
Kolmas harkinnassa oleva tapa on injektoida kutsuva luokka ja tarjota pääsy sen sisäiseen tilaan. </br>

## Sekvenssikaavioita grafiikasta

Ensimmäinen kaavio kuvaa kuinka vaihdetaan työtila yksinkertaiseen näkymään joka ei vaadi dataa tietokannasta.</br>
Seuraavakaavio kuvaa kuinka tietokannasta haettu data liitetään osaksi näkymää, kun lähetysnumero on annettu.
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Receive%20shipment%201.PNG" width="500">
</br>
</br>
__Tässä vielä miltä seuravaa lopputulos näyttää__</br>
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Data.PNG" width="500">
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Receive%20shipment%202.PNG" width="1000">
