/*
  Titre      : Projet Final Examen
  Auteur     : Yvan Tankeu
  Date       : 27/03/2023
  Description: 
    1- Création du serveur : il s'agit de mettre en place une infrastructure serveur 
            capable de stocker et de traiter les données météorologiques.
    2- Conception des objets intelligents : il faut concevoir des capteurs et autres
            dispositifs électroniques pour collecter les données météorologiques.
    3- Affichage des données via une interface web : une fois les données collectées et 
            stockées, elles doivent être présentées via une interface web conviviale et facile à utiliser.
  Version    : 0.0.1
*/

// Pour le moment il faut que Arduino et la machine qui heberge le serveur soient sur le meme reseau
public class App {
    public static void main(String[] args) {
        Serveur server = new Serveur(5500);
        // Demarrer le serveur sur 5500
        server.start();
    }
    
}
