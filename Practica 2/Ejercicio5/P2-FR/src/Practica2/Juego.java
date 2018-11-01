/*
 * Práctica 2 - 3 en raya - FR
 * Alumnos: Ángel Gómez Martín, David Infante Casas
 * ETSIIT, UGR, Granada, España
 * 31/10/2018
 */

package Practica2;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// Extendemos la clase thread para que sea concurrente
public class Juego extends Thread {

    // Tablero de juego
    private String[][] tablero = new String[3][3];
    // Enum con atributos para saber el estado del juego
    public enum Ganador {O, X, Draw, Continuar}

    // Atributos de la comunicación cliente/servidor
    private InputStream inputStream;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private BufferedReader buffReader;


    // Constructor
    public Juego(Socket socket) {
        // Inicializamos el tablero con los números de su posición
        int rellenar_tablero = 1;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                tablero[i][j] = Integer.toString(rellenar_tablero);
                ++rellenar_tablero;
            }
        }

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream, true);
            buffReader = new BufferedReader(new InputStreamReader(inputStream));

        } catch (IOException e) {
            System.err.println("Error al obtener los flujos de entrada/salida.");
        }
    }

    // Añadimos el método run() para iniciar la hebra
    @Override 
    public void run(){
        partida();
    }

    // Devuelve un String con el tablero actual
    public String informacionTablero() {
        String informacion_pantalla;

        informacion_pantalla = "\n\n--- Tablero ---";
        for (int i = 0; i < 3; ++i) {
            informacion_pantalla += "\n---------------\n";
            for (int j = 0; j < 3; ++j) {
                informacion_pantalla += " | " + tablero[i][j];
            }
            informacion_pantalla += " | ";
        }
        informacion_pantalla += "\n---------------\n";
        return informacion_pantalla;
    }
    
    // Intercambio de mensajes con el cliente para colocar una ficha
    public int colocarFicha() {
        String ficha = "";
        String informacion_pantalla;

        try {
            // Mandamos la información del estado de la partida
            informacion_pantalla = informacionTablero();
            informacion_pantalla += "\nEscribe el número de la posición donde quieres colocar tu ficha";
            printWriter.println(informacion_pantalla);
            do {
                // Recibimos la posición escogida por el cliente y comprobamos si es válida
                ficha = buffReader.readLine();
                if (!comprobarPosicionLibre(ficha)) {
                    String error = "Posición ocupada o inválida, elije de nuevo";
                    printWriter.println(error);
                }
            } while (!comprobarPosicionLibre(ficha));
            
        } catch (IOException e) {
            System.err.println("Error al mandar/enviar el mensaje");
        }
        return Integer.parseInt(ficha);
    }
    
    // Comprobar que la ficha que se quiere poner no está ya puesta - true libre / false ocupada
    public boolean comprobarPosicionLibre(String ficha) {
        switch (ficha) {
            case "1":
                if (!tablero[0][0].equals("O") && !tablero[0][0].equals("X")) return true;
            break;
            case "2":
                if (!tablero[0][1].equals("O") && !tablero[0][1].equals("X")) return true;
            break;
            case "3":
                if (!tablero[0][2].equals("O") && !tablero[0][2].equals("X")) return true;
            break;
            case "4":
                if (!tablero[1][0].equals("O") && !tablero[1][0].equals("X")) return true;
            break;
            case "5":
                if (!tablero[1][1].equals("O") && !tablero[1][1].equals("X")) return true;
            break;
            case "6":
                if (!tablero[1][2].equals("O") && !tablero[1][2].equals("X")) return true;
            break;
            case "7":
                if (!tablero[2][0].equals("O") && !tablero[2][0].equals("X")) return true;
            break;
            case "8":
                if (!tablero[2][1].equals("O") && !tablero[2][1].equals("X")) return true;
            break;
            case "9":
                if (!tablero[2][2].equals("O") && !tablero[2][2].equals("X")) return true;
            break;
        }
        return false;
    }

    // Actualizar el tablero con la ficha
    public void actualizarTablero(int ficha, String jugador) {
        int posicion_actual = 1;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (ficha == posicion_actual) tablero[i][j] = jugador;
                ++posicion_actual;
            }
        }
    }
    
    // Comprobar quién gana la partida
    public Ganador finPartida() {
        int i, j;
        boolean salir = false;
        //Comprobamos las filas
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3 && !salir; ++j) {
                if (!tablero[i][j].equals("O")) salir = true;
                if (j == 2 && salir == false) return Ganador.O;
            }
            salir = false;
            for (j = 0; j < 3 && !salir; ++j) {
                if (!tablero[i][j].equals("X")) salir = true;
                if (j == 2 && salir == false) return Ganador.X;
            }
            salir = false;
        }
        //Comprobamos las columnas
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3 && !salir; ++j) {
                if (!tablero[j][i].equals("O")) salir = true;
                if (j == 2 && salir == false) return Ganador.O;
            }
            salir = false;
            for (j = 0; j < 3 && !salir; ++j) {
                if (!tablero[j][i].equals("X")) salir = true;
                if (j == 2 && salir == false) return Ganador.X;
            }
            salir = false;
        }
        //Comprobamos las diagonales
        if (tablero[0][0].equals("O") && tablero[1][1].equals("O") && tablero[2][2].equals("O")) return Ganador.O;
        else if (tablero[0][2].equals("O") && tablero[1][1].equals("O") && tablero[2][0].equals("O")) return Ganador.O;
        else if (tablero[0][0].equals("X") && tablero[1][1].equals("X") && tablero[2][2].equals("X")) return Ganador.X;
        else if (tablero[0][2].equals("X") && tablero[1][1].equals("X") && tablero[2][0].equals("X")) return Ganador.X;
        //Comprobamos empate si no está lleno el tablero
        for (i = 0; i < 3 && !salir; ++i) {
            for (j = 0; j < 3 && !salir; ++j) {
                if (!tablero[i][j].equals("O") && !tablero[i][j].equals("X")) salir = true;
            }
        }
        //Si está lleno
        if (salir) return Ganador.Continuar;
        else return Ganador.Draw;
    }
    
    // Desarrollo de la partida
    public void partida() {
        String jugador = "O";
        int turno = 0;
        Ganador fin;
        do {
            if (turno % 2 == 0) jugador = "O";
            else jugador = "X";
            
            actualizarTablero(colocarFicha(), jugador);
            ++turno;
            fin = finPartida();
        } while (fin == Ganador.Continuar);

        String resultado = informacionTablero();
        switch (fin) {
            case O:
                resultado += "HA GANADO EL JUGADOR O";
            break;
            case X:
                resultado += "HA GANADO EL JUGADOR X";
            break;
            case Draw:
                resultado += "HA HABIDO UN EMPATE";
            break;
        }

        printWriter.println("\n"+resultado);
        System.out.print("\nFIN DEL JUEGO");
    }
}
