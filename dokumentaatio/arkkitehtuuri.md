# Arkkitehtuuri
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Hub.PNG" width="500">
<p>
Klikkaamalla vasemmalla olevaa sinistä valikkoa saa oikealle valkoiselle pohjalle näkyviin valitun työtilan.
</p>
</br>
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/GettingLayout.PNG" width="500">
</br>

Linkin _addRoom_ tapahtumankäsittelijä kutsuu metodia _setWorkspace(VBox)_ , joka ottaa parametrikseen luokan </br> 
_Workspaces_ metodin _addRoomWorkspace()_ palauttaman VBox olion ja asettaa sen _workspaceParent_ Borderpane olioon. </br>
Nyt työtila näkyy. Saapuvan lähetyksen tapauksessa ensimmäinen työnäkymä pyytää lähetyksen numeroa. _Apply_ </br>
napin tapahtumankäsittelijä pyytää luokalta _ShipmentWorkspace_ uutta VBox oliota ja asettaa sen Hub luokan </br>
_setWorkspace_ metodin parametriksi. _Workspaces_ on käytännössä vain _Hub_ luokan jatke. </br>
</br>
<img src="https://github.com/EternalAzure/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Functionality.PNG" width="500">
</br>
Joka kerta, kun käyttäjä painamalla Apply nappia työtilassa lisää tai poistaa jotain tietokannasta, kutsutaan </br>
HubController. HubController vastaa käyttäjän toiveisiin vastaamisesta.
