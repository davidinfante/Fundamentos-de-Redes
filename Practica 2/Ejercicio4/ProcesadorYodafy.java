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
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class ProcesadorYodafy extends Thread {
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private DatagramSocket socketUDP;
	
	// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
	private Random random;
        
        // Búffer
        byte[] buffer = new byte[256];
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public ProcesadorYodafy(DatagramSocket socketUDP) {
            this.socketUDP = socketUDP;
            random = new Random();
	}
	
        @Override 
        public void run(){
            procesa();
        }
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		try {
                    // Creamos paquete y lo recibimos
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socketUDP.receive(paquete);
                    
                    // Dirección conexión
                    InetAddress direccion = paquete.getAddress();
                    
                    // Puerto
                    int puerto = paquete.getPort();
                    
                    // Yoda hace su magia:
                    // Creamos un String a partir de la linea leida
                    String peticion = new String(paquete.getData(), Charset.forName("UTF-8"));

                    // Yoda reinterpreta el mensaje:
                    String respuesta = yodaDo(peticion);
                    
                    // Enviamos paquete con la respuesta
                    DatagramPacket paquete2 = new DatagramPacket(respuesta.getBytes(), respuesta.getBytes().length, direccion, puerto);
                    socketUDP.send(paquete2);
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}

	}

	// Yoda interpreta una frase y la devuelve en su "dialecto":
	private String yodaDo(String peticion) {
		// Desordenamos las palabras:
		String[] s = peticion.split(" ");
		String resultado="";
		
		for(int i=0;i<s.length;i++){
			int j=random.nextInt(s.length);
			int k=random.nextInt(s.length);
			String tmp=s[j];
			
			s[j]=s[k];
			s[k]=tmp;
		}
		
		resultado=s[0];
		for(int i=1;i<s.length;i++){
		  resultado+=" "+s[i];
		}
		
		return resultado;
	}
}
