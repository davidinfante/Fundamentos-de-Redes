package Yodafy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {
	public static void main(String[] args) {
		// Puerto de escucha
		int port=8989;
                ProcesadorYodafy procesador;
		
		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			DatagramSocket socketUDP = new DatagramSocket(port);
			
			// Mientras ... siempre!
			do {
				// Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
				// argumento el nuevo socket, para que realice el procesamiento
				// Este esquema permite que se puedan usar hebras más fácilmente.
				procesador = new ProcesadorYodafy(socketUDP);
				procesador.start();
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto " + port);
		}
	}
}
