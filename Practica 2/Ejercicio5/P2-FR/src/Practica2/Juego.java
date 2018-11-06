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
    // Variables de la pestaña resultados
    public static int winO = 0;
    public static int winX = 0;
    public static int draws = 0;
    public static int num_turnos_total = 0;

    // Atributos de la comunicación cliente/servidor
    private InputStream inputStream;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private BufferedReader buffReader;


    // Constructor
    public Juego(Socket socket) {
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
    
    // Resetear tablero 
    private void resetearTablero() {
        int rellenar_tablero = 1;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                tablero[i][j] = Integer.toString(rellenar_tablero);
                ++rellenar_tablero;
            }
        }
    }
    
    // Mensaje tablero
    public String mensajeTablero() {
        String msjTablero = "M1-";
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                msjTablero += tablero[i][j];
            }
        }
        
        return msjTablero;
    }
    
    // Intercambio de mensajes con el cliente para colocar una ficha
    public int colocarFicha() {
        String ficha = "";

        try {
            // Mandamos la información del estado de la partida
            String msjTablero = mensajeTablero();
            msjTablero += 'C';
            System.out.print("\n" + msjTablero);
            printWriter.println(msjTablero);
            do {
                // Recibimos la posición escogida por el cliente y comprobamos si es válida
                System.out.print("\nM2");
                printWriter.println("M2");
                ficha = buffReader.readLine();
                if (!comprobarPosicionLibre(ficha)) {
                    System.out.print("\nM3");
                    printWriter.println("M3");
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
        //Si no está lleno
        if (salir) return Ganador.Continuar;
        else return Ganador.Draw;
    }
    
    // Desarrollo de la partida
    public void partida() {
        String eleccion = "";
        // Menu
        do {
            resetearTablero();
            System.out.print("\nM00");
            printWriter.println("M00");
            try {
                eleccion = buffReader.readLine();
            } catch (IOException e) {
                System.err.println("Error al recibir el mensaje");
            }
            switch (eleccion) {
                case "1":
                    System.out.print("\nM01");
                    printWriter.println("M01");
                    // Juego
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
                    String tableroFinal = ("\n" + mensajeTablero());
                    tableroFinal += 'F';
                    System.out.print(tableroFinal);
                    printWriter.println(tableroFinal);
                    switch (fin) {
                        case O:
                            ++winO;
                            System.out.print("\nM4-O");
                            printWriter.println("M4-O");
                        break;
                        case X:
                            ++winX;
                            System.out.print("\nM4-X");
                            printWriter.println("M4-X");
                        break;
                        case Draw:
                            ++draws;
                            System.out.print("\nM4-D");
                            printWriter.println("M4-D");
                        break;
                    }

                    String num_turnos = "M5-";
                    num_turnos += turno;
                    System.out.print("\n" + num_turnos);
                    printWriter.println(num_turnos);
                    num_turnos_total += turno;
                break;
                case "2":
                    String resultados = "M02-" + "O" + Integer.toString(winO) + "X" + Integer.toString(winX) + "D" + Integer.toString(draws) + "T" + Integer.toString(num_turnos_total);
                    System.out.print("\n" + resultados);
                    printWriter.println(resultados);
                break;
            }
        } while (!eleccion.equals("3"));
        System.out.print("\nFIN DE LA CONEXION");
        
    }
}
