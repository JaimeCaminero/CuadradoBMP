package cuadradoBMP;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("El cuadrado de la MUERTE");
		// ESCRIBIR LAS ENTRADAS DE LOS USUARIOS Y HACER LAS COMPROBACIONES PERTINENTES,
		// PARA LUEGO PASARLO POR EL CONSTRUCTOR DE LA IMAGEN
		String ruta = "cuadrado.bmp";
		Test test = new Test(ruta);
		try {
			ImagenBMP imagen = new ImagenBMP("pruebaImagenDefecto", 50, 50);
			byte [] colorFondo = imagen.pedirColoresRGB();
			byte [] colorCuadrado = imagen.pedirColoresRGB();
			imagen.setColorFondo(colorFondo);
			imagen.setColorCuadrado(colorCuadrado);
			imagen.crearImagen();
			imagen.crearFicherBMP();
			imagen.cerrarRecursos();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
