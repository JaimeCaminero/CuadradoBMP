package cuadradoBMP;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("El cuadrado de la MUERTE");
		String ruta = "cuadrado.bmp";
		Test test = new Test(ruta);
		try {
		ImagenBMP imagen = new ImagenBMP("pruebaImagenDefecto", (byte) 50, (byte) 50);
		imagen.crearFicherBMP();
//			test.crearFichero();
//			test.leerFicheroByte();
//			test.cerrarRecursos();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
