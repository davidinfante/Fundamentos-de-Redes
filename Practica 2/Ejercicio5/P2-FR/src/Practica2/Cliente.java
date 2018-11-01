/*
 * Práctica 2 - 3 en raya - FR
 * Alumnos: Ángel Gómez Martín, David Infante Casas
 * ETSIIT, UGR, Granada, España
 * 31/10/2018
 */

package Practica2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        // Almacenamiento del movimiento del cliente
        String ficha;
        // Nombre del host donde se ejecuta el servidor
        String host = "localhost";
        // Puerto en el que espera al servidor
        int port = 8989;
        // Condición para finalizar el juego
        boolean finalizar = false;

        try {
            // Creamos el socket
            Socket socket = new Socket(host, port);
            // Obtenemos los flujos de escritura/lectura
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            // Entrada por teclado
            Scanner teclado = new Scanner(System.in);

            while (!finalizar) {
                String linea;
                // Mostramos la información del juego dada por el servidor
                for (int i = 0; i < 12 && !finalizar; ++i) {
                    linea = buffReader.readLine();
                    System.out.println(linea);
                    // En el caso de recibir un mensaje de final de partida (los cuales contienen HA) salimos del bucle
                    if (linea.contains("HA")) {
                        finalizar = true; 
                        System.out.print("\n\n");
                    }
                }
                if (!finalizar) {
                    ficha = teclado.next();
                    // Enviamos al servidor el movimiento
                    printWriter.println(ficha);
                    //Capturamos una posible linea de valor incorrecto
                    // .........
                }
            }
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
    }
    
}