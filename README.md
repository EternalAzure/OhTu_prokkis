## Ohjelmistotekniikka, harjoitustyö
----
# Varastonhallintasovellus

Projekti kulkee nimellä Orderly, vaikka kävi ilmi että nimi on jo viety. </br>
Itse työpöytäsovellus on tarkoitettu varaston hallintaan ja tukee kolmea eri </br>
käyttäjä profiilia: Esimies, varastonhoitaja ja keräilijä. </br>
Perustoimillallisuuksia ovat varastohuoneiden lisääminen ja poistaminen. </br>
Tuotteiden katalogiin lisääminen ja sieltä poistaminen. </br>
Sekä kaikenlainen saldon pyörittely. </br>
</br>

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

### Testiohjeet
__Vaaditaan kolme osaa:__ </br>
  1. Testiympäristö on _junit_ 
  2. Testit ajetaan _JfxRunnerilla_
  3. db ajuri on _mysql-connector-java_
  
Tarkista, että pom.xml tiedostosta löytyy seuraavat
```
<dependency>
  <groupId>de.saxsys</groupId>
  <artifactId>jfx-testrunner</artifactId>
</dependency>
```
```
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.13.2</version>
  <scope>test</scope>
  <version>1.2</version>
</dependency>
```
```
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.23</version>
</dependency>
```
Lisää kaikki osat Mavenin kautta projektiin. Ajurin </br>
lisääminen omalta koneelta aiheuttaa ongelmia terminaalin kanssa.
</br>
Tarkista, että importit ovat oikein ja ajat testit JfxRunnerilla.
```
import de.saxsys.javafx.test.JfxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(JfxRunner.class)
```
__Testien ajaminen terminaalista__ </br>
```
mvn test
```
ja jos käyttää jacocoa
```
mvn test jacoco:report
```
