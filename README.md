## Ohjelmistotekniikka, harjoitustyö
----
# Varastonhallintasovellus
<p>
Ohjelma mahdollistaa saldon pyörittelyn ja jatkossa tähtää erikoistumaan </br> 
elintarviketuotannon resurssien hallintaan. Tällaisia toiminnallisuuksia ovat </br>
eräpäiväseuranta sekä raaka-aineiden muuttaminen lopputuotteeksi reseptin mukaan. </br>
Pyrkimys on myöhemmin toteuttaa moninaista tilastointia ja useita käyttäjä profiileita.
</p>

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Login%20screen.PNG" width="400"> 

<p>
Sovelluksessa on kirjautumisikkuna, joka vertaa annettua nimeä ja salasanaa </br>
osoitteessa locahost:3306 olevaan MySQL tietokantaan nimeltä login. </br>
Toistaiseksi järjestelmän ylläpitäjä lisää käyttäjiä sovellukseen eikä sovellus </br> 
itse tarjoa mahdollisuutta rekisteröityä.
</p>

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Hub.PNG" width="400">

<p>
Pääasiassa toiminta tapahtuu Hub-ikkunassa, joka jakautuu kolmeen perusosaan:  </br>
Valikkopalkkiin ylhäällä, siniseen valikkoon vasemmalla ja valkoiseen työtilaan </br>
oikealla. Vasen valikko määrää mitä työtilassa näkyy. Selkolukuisuuden vuoksi </br>
työtilan UI on eriytetty Workspaces luokkaan joka tarjoaa UI:n työtehtäviä vastaavasti </br>
nimettyjen metodien kautta VBox-olioina. Hub ja Workspaces käyttävät saman </br>
kontrollerin palveluita. Hubissa hyödynnetään tietokantaa warehouse.
</p>

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Data.PNG" width="400">

<p>
Lisäksi sovellus pystyy esittämään tietokannasta hakemansa datan graafisesti.</br>
Kuvassa otetaan vastaan saapuvaa lähetystä.
</p>

### Dokumentaatio
[Arkkitehtuuri](dokumentaatio/arkkitehtuuri.md) </br>
[Vaatimusmäärittely](dokumentaatio/vaatimusmaarittely.md) </br>
[Tuntikirjanpito](dokumentaatio/tuntikirjanpito.md) </br>

__Ajaminen terminaalista__ </br>
```
mvn compile exec:java -Dexec.mainClass=ui.Login
```
```
mvn test
```
```
mvn test jacoco:report
```
### Ohjeet .jar ajamiseen
Lataa viimeisimmän julkaisun jar-tiedosto koneellesi.</br>
Windowsilla tiedoston voi ajaa suoraan. Ohjelma herjaa tietokannan</br>
puuttumisesta, mutta älä anna sen häiritä.</br>
</br>
Linuxilla saatat joutua merkitsemään tiedoston ajettavaksi</br>
Korvaa ```Orderly-1.0-SNAPSHOT.jar``` käyttämälläsi julkaisulla.
```
chmod +x Orderly-1.0-SNAPSHOT.jar
```
Sen jälkeen voit kokeilla ajaa tiedoston komennolla
```
java -jar Orderly-1.0-SNAPSHOT.jar
```
