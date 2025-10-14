package cuadradoBMP;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("El cuadrado de la MUERTE");
		String ruta = "cuadrado.bmp";
		Test test = new Test(ruta);
		try {
		ImagenBMP imagen = new ImagenBMP("pruebaImagenDefecto", 5, 5);
		imagen.crearFicherBMP();
		imagen.cerrarRecursos();
//			test.crearFichero();
//			test.leerFicheroByte();
//			test.cerrarRecursos();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
