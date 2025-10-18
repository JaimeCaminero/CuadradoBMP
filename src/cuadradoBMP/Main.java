package cuadradoBMP;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("El cuadrado de la MUERTE");
		// ESCRIBIR LAS ENTRADAS DE LOS USUARIOS Y HACER LAS COMPROBACIONES PERTINENTES,
		// PARA LUEGO PASARLO POR EL CONSTRUCTOR DE LA IMAGEN
		String ruta = "cuadrado.bmp";
		Test test = new Test(ruta);
		try {
			System.out.print("Dimensión de la imagen en px): ");
			int dimensionesImagen = sc.nextInt();

			System.out.print("Dimensión del cuadrado hueco (px menor que la imagen para apreciarlo bien): ");
			int dimensionesCuadrado = sc.nextInt();
			
			ImagenBMP imagen = new ImagenBMP("pruebaImagenDefecto", dimensionesImagen, dimensionesCuadrado);
			// Métodos invocados desde el MAIN, se ha hecho desde el constructor
//			byte [] colorFondo = imagen.pedirColoresRGB();
//			byte [] colorCuadrado = imagen.pedirColoresRGB();
//			imagen.setColorFondo(colorFondo);
//			imagen.setColorCuadrado(colorCuadrado);
			imagen.crearImagen();
			imagen.crearFicherBMP();
			imagen.cerrarRecursos();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
