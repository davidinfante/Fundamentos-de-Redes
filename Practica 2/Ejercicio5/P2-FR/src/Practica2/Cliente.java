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
        String eleccion = "";
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

            String linea;
            do {
                linea = buffReader.readLine();
                // Menu
                if (linea.contains("M00")) {
                    System.out.println("\n---------------\n1 Jugar\n2 Resultados\n3 Salir\n---------------\n");
                    eleccion = teclado.next();
                    printWriter.println(eleccion);
                }
                // Comenzar partida
                else if (linea.contains("M01")) {
                    finalizar = false;
                    do {
                        linea = buffReader.readLine();
                        // M1 Salida de tablero
                        if (linea.contains("M1")) {

                            String[] msjTablero =  linea.split("-");
                            String informacion_pantalla;

                            informacion_pantalla = "\n\n--- Tablero ---";
                            for (int i = 0; i < 9; ++i) {
                                if (i == 0 || i == 3 || i == 6) informacion_pantalla += "\n---------------\n";
                                informacion_pantalla += " | " + msjTablero[1].charAt(i);
                                if (i == 2 || i == 5 || i == 8) informacion_pantalla += " | ";
                            }
                            informacion_pantalla += "\n---------------\n";
                            if (msjTablero[1].charAt(9) == 'C') informacion_pantalla += "\nEscribe el número de la posición donde quieres colocar tu ficha";
                            System.out.println(informacion_pantalla);
                        }
                        // M2 Colocar Ficha
                        else if (linea.contains("M2")) {
                            ficha = teclado.next();
                            printWriter.println(ficha);
                        }
                        // M3 Error
                        else if (linea.contains("M3")) {
                            for (int i = 0; i < 1 && !finalizar; ++i) {
                                linea = buffReader.readLine();
                                System.out.println(linea);
                            }
                        }
                        // M4 Fin partida
                        else if (linea.contains("M4")) {
                            String[] resultado =  linea.split("-");
                            switch (resultado[1]) {
                                case "O":
                                    System.out.println("HA GANADO EL JUGADOR O");
                                break;
                                case "X":
                                    System.out.println("HA GANADO EL JUGADOR X");
                                break;
                                case "D":
                                    System.out.println("HA HABIDO UN EMPATE");
                                break;
                            }
                        }
                        // M5 Número de turnos
                        else if (linea.contains("M5")) {
                            String[] resultado =  linea.split("-");
                            System.out.println("El número de turnos total ha sido: " + resultado[1]);
                            finalizar = true;
                        }
                    } while (!finalizar);
                }
                // Resultados
                else if (linea.contains("M02")) {
                    String[] resultados =  linea.split("-");
                    System.out.println("\nNúmero de victorias del jugador O: " + resultados[1].charAt(1));
                    System.out.println("\nNúmero de victorias del jugador X: " + resultados[1].charAt(3));
                    System.out.println("\nNúmero de empates: " + resultados[1].charAt(5));
                    String turnos = "\nNúmero de turnos totales: ";
                    for (int i = 7; i < resultados[1].length(); ++i) turnos += resultados[1].charAt(i);
                    System.out.println(turnos);
                }
            } while (!eleccion.equals("3"));
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
    }
    
}