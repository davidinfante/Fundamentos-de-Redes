/*
 * Práctica 2 - 3 en raya - FR
 * Alumnos: Ángel Gómez Martín, David Infante Casas
 * ETSIIT, UGR, Granada, España
 * 31/10/2018
 */

package Practica2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {	
        // Puerto de escucha
        int puerto = 8989;

        try {
            // Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "puerto"
            ServerSocket serverSocket = new ServerSocket(puerto);

            do {
                // Aceptamos la nueva conexión
                Socket socket = serverSocket.accept();
                System.out.print("\nCONEXION INICIADA");
                // Comienza el juego
                Juego juego = new Juego(socket);
                juego.start();
            } while (true);
        } catch (IOException e) {
            System.err.println("Error al escuchar en el puerto " + puerto);
        }
    }
}