package Yodafy;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

public class YodafyClienteTCP {
	public static void main(String[] args) {	
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";

                // Puerto en el que espera el servidor:
		int port=8989;
                byte[] buffer = new byte[256];
		
		try {
                        // Socket y dirección.
                        DatagramSocket socketUDP = new DatagramSocket();
                        InetAddress direccion = InetAddress.getByName(host);

			// Mensaje
                        String mensaje = "Al monte del volcán debes ir sin demora";
                        
                        // Enviamos paquete con mensaje
                        DatagramPacket paquete = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, direccion, port);
                        socketUDP.send(paquete);
                        
                        // Recibimos paquete con mensaje modificado
                        DatagramPacket paquete2 = new DatagramPacket(buffer, buffer.length);
                        socketUDP.receive(paquete2);
                        
			// Mostremos la cadena de caracteres recibidos:
			System.out.println("Recibido: ");
			String msg = new String(paquete2.getData(), Charset.forName("UTF-8"));
                        System.out.println(msg);
			
			// Cerramos socket
			socketUDP.close();
			
		// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
