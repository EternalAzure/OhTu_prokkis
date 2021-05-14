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
osoitteessa Azure pilvessä olevaan MySQL tietokantaan nimeltä login. </br>
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
[Tietokanta](dokumentaatio/skeema.txt)

### Ohjelman ajaminen:
__Tapa 1: Käynnistä projektikansiosta main metodi__
```
$ mvn compile exec:java -Dexec.mainClass=fi.orderly.ui.Main
```
__Tapa 2: Pakkaa jar tiedostoksi__
```
$ mvn package
```
__Aja .jar__
```
$ cd target/ && java -jar .jar
```
__Tapa 3: Lataa release__

__Windows__ </br>
Lataa viimeisimmän julkaisun jar-tiedosto koneellesi.</br>
Tuplaklikkaa tiedostoa. </br>
__Linux__ </br>
Linuxilla saatat joutua merkitsemään tiedoston ajettavaksi (executable) </br>
Korvaa ```Orderly-1.0-SNAPSHOT.jar``` käyttämäsi julkaisun tiedostonimellä.
```
$ chmod +x Orderly-1.0-SNAPSHOT.jar
```
Sen jälkeen voit kokeilla ajaa tiedoston komennolla
```
$ java -jar Orderly-1.0-SNAPSHOT.jar
```
Vaihtoehtoisesti voit tuplaklikata tiedostoa -> ominaisuudet -> luvat -> rastita ruutu.

### Testien ajaminen
Automaattisten testien ajaminen
```
$ mvn test
```
Testikattavuus raportin luominen
```
$ mvn test jacoco:report
```
Checkstyle raportin luominen
```
$ mvn jxr:jxr checkstyle:checkstyle
```
Jos testien ajamisessa on ongelmia, aja
```
$ mvn clean
```
IDE kannattaa myös sulkea eikä jättää päälle yöksi.

### JavaDoc
Dokumentaation luominen
```
$ mvn javadoc:javadoc
```

### Uusimmat julkaisut

[Linux](https://github.com/EternalAzure/ot-harjoitustyo/releases/tag/v1.0.0-linux)
[Windows](https://github.com/EternalAzure/ot-harjoitustyo/releases/tag/v1.0.0-windows)
