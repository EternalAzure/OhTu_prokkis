# Arkkitehtuuri

##### Sisältö

[Grafiikan hakeminen ja esittäminen](#grafiikan-hakeminen-ja-esittäminen) </br>
[Tavaran vastaanottaminen](#tavaran-vastaanottaminen) </br>
[Tietokannan taulut](#tietokannan-taulut) </br>
[Toiminnallisuus ja logiikka](#toiminnallisuus-ja-logiikka) </br>
[Sekvenssikaavioita grafiikasta](#sekvenssikaavioita-grafiikasta) </br>

## Grafiikan hakeminen ja esittäminen

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Get workspace.PNG" width="1000">
</br>

_Hub_-luokka on ainoa luokka, joka näyttää käyttäjälle grafiikkaa. Hub sisältää UI elementit harmaaseen</br> 
yläpalkkiin ja siniseen valikkoon. Päätin eriyttää valkoisen työtilan grafiikan omaksi luokakseen _Workspaces._ </br>
Luokassa Workspaces on yksi metodia jokaista työtilaa kohti, jotka ovat valittavissa valikosta. Jokainen metodi </br>
palauttaa VBox olion. Tämä VBox olio asetetaan Hubissa olevan _workspaceParent_ nimisen BorderPane olion keskelle, </br>
jolloin se näkyy käyttäjälle. </br>
Jokaisella valikon hyperlinkillä on lambda joka asettaa setWorkspace() parametriksi sopivan metodin kuten </br>
```event -> setWorkSpace(workSpaces.addRoomWorkspace())``` </br>
Tavaran vastaanottaminen on poikkeus, jossa Workspace pyytää _ReceiveShipmentTable_-luokkaa rakentamaan UI elementin.</br>

### Tavaran vastaanottaminen

Aina näkymälle ei löydy suoraa vastinetta tietokannasta. Näin ollen olen toteuttanut pakkaukseen _dao_ luokkia joiden tehtävänä on rakentaa sopiva tietorakenne ja tarjota pääsy tähän tietoon. _ReceiveShipmentTable_ rakentaa taulukon tavaran vastaanottamista varten. Se käsittelee tietoa _Shipment_-luokkan kautta. _Shipment_ sisältää metodit tiedon hakemiseen, yhdistämiseen, ja tarjoamiseen. Shipment sisältää listan jonka indeksit vastaavat riviä taulukossa. Tiedon tallettava tietorakenne on _DataPackage_-aliluokka.
Listaan tarjotaan pääsy getDataPackage(int index) kautta, joka mahdollistaa listan helpon iteroimisen. DataPackage-aliluokka omaa gettereitä ja setterit vain arvoille _amount_ ja _room_, jotka ovat ainoat tiedot, joita käyttäjän halutaan muuttavan tavaaraa vastaanotettaessa. Syy toteuttaa DataPackage aliluokkana taulukon sijaan on inhimmillisten virheiden minimointi. Tulee vähemmän sekaannuksia, kun kutsuu getName() list[i] sijaan. </br>

### Tietokannan taulut

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Tauluikkunat.PNG" width="800">

Harmaasta valikkopalkista on valittavissa näyttää 5 eri taulua tietokannasta _warehouse_. Jokainen taulu saa oman ikkunansa, jota on mahdollista liikuttaa, ja jonka kokoa on mahdollista muuttaa. Jokaisesta taulusta voi olla yhdenaikaisesti auki vain yksi ikkuna. </br>

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

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Toiminnallisuus.SVG" width="1000">
</br>
Ohjelman pääikkunan on nimeltään hub kuten myös luokka _Hub_ jossa tämä stage olion. Käyttäjän toiveisiin vastaavat sinisen valikon kohdalli lambdat ja _HubController_. Koodin toisteisuuden vähentämiseksi _Utils_-luokka sisältää käteviä pikku metodeja, joita hyödynnetään syötteiden validoinnissa vähän siellä täällä. Kaikki toiminnallisuus on pyritty toteuttamaan siten, että palveluntarjoajan ei tarvitse tuntea palvelun pyytäjää. Tarvittavat riippuvuudet toteutetaan injektioina. Parhaana esimerkkinä Connection olio, joka luodaan ensimmäisen kerran pääikkunan lataamisen yhteydessä ja annetaan _Hub_-luokasta eteenpäin. Tämä helpottaa mm. testaamista, kun käytössä on testitietokanta. Logiikka metodit palauttavat käyttöliittymälle sopivan String arvon riippuen siitä mitä palautetta halutaan antaa. Ohjelman ytimessä on työskentely tietokannan kanssa. Kaikki SQL-koodi on siirretty _logic.dbinterfaces_ pakkaukseen. 

### DatabaseAccess

_DatabaseAccess_ tarjoaa ensisijaisesti SQL operaatioita, jotka käsittelevät useampaa kuin yhtä taulua. Toissijaisesti se tarjoaa erittäin siistin tavan päästä käsiksi muihin tietokantarajapintoihin. ```db.rooms.insertRoom("Huone");``` missä db on DatabaseAccess ja balance on BalanceInterface. Luokkaa käytetään lähes kaikkialla.

## Sekvenssikaavioita grafiikasta

Ensimmäinen kaavio kuvaa kuinka tietokannan taulu _products_ näytetään käyttäjälle.
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Taulun haku.PNG" width="500">
</br>

Toinen kaavio näyttää kuinka työtila näytetään käyttäjälle.</br>
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Työtilan haku.PNG" width="500">
