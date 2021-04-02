## Ohjelmistotekniikka, harjoitustyö
----
# Varastonhallintasovellus
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/kuvat/Login%20screen.PNG" width="400"> 
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/kuvat/Hub%20screen.PNG" width="600">

Projekti kulkee nimellä Orderly, vaikka kävi ilmi että nimi on jo viety. </br>
Itse työpöytäsovellus on tarkoitettu varaston hallintaan ja tukee kolmea eri </br>
käyttäjä profiilia: Esimies, varastonhoitaja ja keräilijä. </br>
Perustoimillallisuuksia ovat varastohuoneiden lisääminen ja poistaminen. </br>
Tuotteiden katalogiin lisääminen ja sieltä poistaminen. </br>
Sekä kaikenlainen saldon pyörittely.

Sovelluksessa on kirjautumisikkuna, joka vertaa annettua nimeä ja salasanaa </br>
osoitteessa locahost:3306 olevaan MySQL tietokantaan nimeltä login. </br>
Järjestelmän ylläpitäjä lisää käyttäjiä sovellukseen eikä sovellus itse tarjoa </br>
mahdollisuutta rekisteröityä. </br>
</br>
Pääasiassa toiminta tapahtuu Hub-ikkunassa, jonka vasen puoli on sininen valikko, </br>
josta valitaan oikealla valkoisella pohjalla olevaan työtilaan ilmestyvä toiminnallisuus. </br>

### Arkkitehtuuri
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/kuvat/T%C3%A4ydennetty%20arkkitehtuuri.png" width="600" >
