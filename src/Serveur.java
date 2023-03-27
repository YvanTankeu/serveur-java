import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serveur extends Postgres {
    private int port;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private Connection dbConnection;

    public Serveur(int port) {
        this.port = port;
        this.isRunning = false;
        this.dbConnection = null;
    }

    public void start() {
        try {
            // Créer une connexion à la base de données
            dbConnection =  connect();
            System.out.println("Database connection established");

            // Créer le serveur socket
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            // Démarrer le serveur
            isRunning = true;
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error starting server: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
                if (dbConnection != null) dbConnection.close();
            } catch (IOException | SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                // Lire les données envoyées par l'Arduino
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                StringBuilder requestData = new StringBuilder();
                
                String line;
                while ((line = in.readLine()) != null) {
                    requestData.append(line);
                }
                String data = requestData.toString();
                System.out.println(data);

                // extraire la valeur de température du message
            
                double temperature = 0.0;
                double humidite = 0.0;
                
                Pattern pattern = Pattern.compile("(\\d+\\.\\d+)"); // création d'un pattern qui recherche une expression contenant un ou plusieurs chiffres suivis d'un point et d'autres chiffres
                Matcher matcher = pattern.matcher(data); // création d'un matcher qui va chercher le pattern dans la chaine de caractère data

                if (matcher.find()) { // si le matcher trouve un résultat
                    temperature = Double.parseDouble(matcher.group()); // la température est extraite du résultat et convertie en double
                }

                if (matcher.find()) { // si le matcher trouve un autre résultat
                    humidite = Double.parseDouble(matcher.group()); // l'humidité est extraite du résultat et convertie en double
                }
                                                
                System.out.println("Temperature : " + temperature);
                System.out.println("Humidite : " + humidite);
                
                            // Préparation de la requête SQL
                String sql = "INSERT INTO donnees(datetime, temperature, humidite) VALUES (?, ?, ?)";
                PreparedStatement statement = dbConnection.prepareStatement(sql);

                // Définition des valeurs des paramètres de la requête SQL
                Timestamp datetime = new Timestamp(System.currentTimeMillis());
                statement.setTimestamp(1, datetime);
                statement.setDouble(2, temperature);
                statement.setDouble(3, humidite);

                // Exécution de la requête SQL
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Les données ont été insérées avec succès.");
                }

                // Fermeture des ressources JDBC
                statement.close();
            } catch (IOException | SQLException   e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }
    }
}
