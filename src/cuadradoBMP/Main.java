package cuadradoBMP;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenido al programa para dibujar un cuadrado");
		String ruta = "cuadrado";
		try {
			System.out.print("Dimensión de la imagen en px): ");
			int dimensionesImagen = sc.nextInt();

			System.out.print("Dimensión del cuadrado hueco (px menor que la imagen para apreciarlo bien): ");
			int dimensionesCuadrado = sc.nextInt();

			ImagenBMP imagen = new ImagenBMP(ruta, dimensionesImagen, dimensionesCuadrado);
			imagen.crearImagen();
			imagen.crearFicherBMP();
			imagen.cerrarRecursos();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
