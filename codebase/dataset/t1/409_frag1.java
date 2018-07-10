import java.net.*;

import java.io.*;

import java.util.*;



public class JDPServer implements JDPCommandInterface {



    /**

   * socket on which server will listen

   */

    private ServerSocket serverSocket;



    /**

   * socket for listening to client input

   */

    private Socket clientSocket;



    /**

   * for writing output

   */

    private ObjectOutputStream out;



    /**

   * for reading input

   */

    private BufferedReader in;



    /**

   * port the server is running on

   */

    int port;



    /**

   * try to open a socket on a given port for listening

   * for client connections

   */

    public JDPServer(int initialPort) {

        port = initialPort;

        boolean socketOpened = false;

        while (!socketOpened) {

            try {

                serverSocket = new ServerSocket(port);

                System.out.println("JDPServer running on port " + port);

                socketOpened = true;

            } catch (IOException e) {

                if (++port > 65536) {

                    System.err.println("No available port");

                    System.exit(1);

                }

            }

        }

    }



    /**

   * accept a client connection

   */

    public void acceptConnection() {

        System.out.println("Waiting for connection...");

        try {

            clientSocket = serverSocket.accept();

            System.out.println("Connection detected!");

            out = new ObjectOutputStream(clientSocket.getOutputStream());

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {

            System.err.println("Accept failed.");

            System.exit(1);

        }

    }



    /**

   * close the client connection 

   */

    public void closeConnection() {

        try {

            out.close();

            in.close();

            clientSocket.close();

        } catch (IOException e) {

            System.err.println("Close failed.");

        }

    }
