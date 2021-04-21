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

Sovelluksessa on kirjautumisikkuna, joka vertaa annettua nimeä ja salasanaa </br>
osoitteessa locahost:3306 olevaan MySQL tietokantaan nimeltä login. </br>
Toistaiseksi järjestelmän ylläpitäjä lisää käyttäjiä sovellukseen eikä sovellus </br> 
itse tarjoa mahdollisuutta rekisteröityä. </br>
</br>

<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Hub%20screen.PNG" width="600">

Pääasiassa toiminta tapahtuu Hub-ikkunassa, joka jakautuu kolmeen perusosaan:  </br>
Valikkopalkkiin ylhäällä, siniseen valikkoon vasemmalla ja valkoiseen työtilaan </br>
oikealla. Vasen valikko määrää mitä työtilassa näkyy. Selkolukuisuuden vuoksi </br>
työtilan UI on eriytetty Workspaces luokkaan joka tarjoaa UI:n työtehtäviä vastaavasti </br>
nimettyjen metodien kautta Pane-olioina. Hub ja Workspaces käyttävät saman </br>
kontrollerin palveluita. Hubissa hyödynnetään tietokantaa warehouse. </br>

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
