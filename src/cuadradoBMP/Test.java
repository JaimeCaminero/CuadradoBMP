package cuadradoBMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Clase para hacer pruebas
 */

public class Test {

	private File fichero;
	private FileInputStream lecturaFichero;
	private final int comprobacionHeader = 54;

	public Test(String ruta) {
		fichero = new File(ruta);
	}

	public void crearFichero() throws IOException {
		if (!fichero.exists()) {
			fichero.createNewFile();
			System.out.println("Fichero creado correctamente");
		} else
			System.out.println("El fichero ya existe");
	}

	public void leerFicheroByte() throws IOException {
		int tam = (int) fichero.length();
		byte[] arrayBytes = new byte[tam];
		System.out.println("Tamaño fichero: " + tam + " bytes");
		lecturaFichero = new FileInputStream(fichero);
		lecturaFichero.read(arrayBytes);
		for (int i = 0; i < arrayBytes.length; i++) {
			System.out.println(i + ": " + arrayBytes[i]);
		}
	}
	
	public void cerrarRecursos() throws IOException {
		lecturaFichero.close();
	}

}
