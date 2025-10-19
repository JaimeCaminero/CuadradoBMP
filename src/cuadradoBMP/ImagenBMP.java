package cuadradoBMP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class ImagenBMP {
	// Atributos que nos pasa el usuario
	private String nombre;
	private int dimensionesImagen;
	private int dimensionesCuadrado;
	private byte[] colorFondo;
	private byte[] colorCuadrado;
	// - Atributos necesarios para definir el header del .bmp
	// - Los atributos "final" indican el índice dentro del array de bytes donde
	// habrá que modificar datos
	// - Los array "endian" serán arrays necesarios para pasar los valores a valores
	// little endian
	private int tamFichero;
	private byte[] endianTamFichero;
	private final int INDICE_TAM_FICHERO = 2;
	private byte[] endianDimensionesImagen;
	private final int INDICE_ANCHO = 18;
	private final int INDICE_ALTO = 22;
	private int tamImagen;
	private byte[] endianTamImagen;
	private final int INDICE_TAM_IMAGEN = 34;
	// Necesitamos un array que defina una imagen por defecto (SÓLO LA CABECERA) y
	// un array que defina la imagen completa
	private byte[] imagen;
	private byte[] imagenPorDefecto;
	// Atributos auxiliares útiles
	private final int CABECERA_POR_DEFECTO = 54;
	private int bytesPadding; // bytes de padding, en caso de que lo haya
	private int tamFila; // tamaño de la fila
	// Atributos para manipular ficheros
	private File fichero;
	private FileOutputStream ficheroEscritura;
	private BufferedOutputStream bufferEscritura;

	// HAY QUEAÑADIR EL COLOR AL CONSTRCUTOR
	public ImagenBMP(String nombre, int dimensionesImagen, int dimensionesCuadrado) throws FileNotFoundException {
		this.nombre = nombre;
		this.dimensionesImagen = dimensionesImagen;
		this.dimensionesCuadrado = dimensionesCuadrado;
		System.out.println("Indica el color de fondo para el .BMP:");
		this.colorFondo = pedirColoresRGB();
		System.out.println("Indica el color de LA figura para el .BMP:");
		this.colorCuadrado = pedirColoresRGB();
//		this.colorFondo = colorFondo.clone();
//		this.colorCuadrado = colorCuadrado.clone();
		// Las siguientes fórmulas están sacadas de internet investigando que, si el
		// tamaño de la fila NO es múltiplo de 4, se necesitarán añadir bytes extras
		// como padding.
		// LO HE CORREGIDO, ANTES USÁBAMOS 4 PERO POR LO VISTO CON 3 ASEGURAS QUE SEA
		// MÚLTIPLO DE 4 Y EVITAS POSIBLES REDONDEOS O CÁLCULOS ERRÓNEOS
		tamFila = (3 * dimensionesImagen + 3) & ~3;
		bytesPadding = tamFila - 3 * dimensionesImagen;

		// tamaños
		tamImagen = tamFila * this.dimensionesImagen;
		tamFichero = CABECERA_POR_DEFECTO + tamImagen;

		// endian arrays
		endianDimensionesImagen = littleEndian(this.dimensionesImagen);
		endianTamImagen = littleEndian(tamImagen);
		endianTamFichero = littleEndian(tamFichero);

		imagenPorDefecto = crearImagenInicial();

		fichero = new File(this.nombre + ".bmp");
	}

	public void crearFicherBMP() throws IOException {
		if (!fichero.exists())
			fichero.createNewFile();
		ficheroEscritura = new FileOutputStream(fichero);
		bufferEscritura = new BufferedOutputStream(ficheroEscritura);
		bufferEscritura.write(imagen);
		// ESTE BUCLE ESTÁ HECHO PARA VER EL ARRAY QUE SE VA A INSCRIBIR DENTRO DEL
		// ARCHIVO
//		// UTILIZARLO COMO GUÍA Y CUANDO SEA DEFINITIVO EL CÓDIGO, QUITARLO
//		for (int i = 0; i < imagen.length; i++)
//			System.out.println(i + ": " + imagen[i]);
		bufferEscritura.flush();
		System.out.println("Se ha creado con éxito");
	}

	public void cerrarRecursos() throws IOException {
		bufferEscritura.close();
		ficheroEscritura.close();
	}

	// Generamos la cabecera del .bmp con valores por defecto válidos, exceptuando
	// algunos valores que son necesarios (para saber las dimensiones del fichero)
	private byte[] crearImagenInicial() {

		byte[] defecto = { 66, 77, 0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianTamFichero
				0, 0, 0, 0, 54, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN -->
																	// endianDimensionesImangenes
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianDimensionesImangenes
				1, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianTamImagen
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		// Insertar valores little-endian en los índices correspondientes
		// file size
		for (int i = 0; i < 4; i++)
			defecto[INDICE_TAM_FICHERO + i] = endianTamFichero[i];
		// width (INDICE_ANCHO)
		for (int i = 0; i < 4; i++)
			defecto[INDICE_ANCHO + i] = endianDimensionesImagen[i];
		// height (INDICE_ALTO)
		for (int i = 0; i < 4; i++)
			defecto[INDICE_ALTO + i] = endianDimensionesImagen[i];
		// image size (INDICE_TAM_IMAGEN)
		for (int i = 0; i < 4; i++)
			defecto[INDICE_TAM_IMAGEN + i] = endianTamImagen[i];

		return defecto;
	}

	public void crearImagen() {
		// QUITAR ESTOS SYSOS CUANDO SE TERMINE
		System.out.println("Dimension imagen: " + tamImagen);
		imagen = new byte[tamFichero];

		System.arraycopy(imagenPorDefecto, 0, imagen, 0, CABECERA_POR_DEFECTO);

		// Calculamos la posición del cuadrado interior
		// Utilizamos Math.in() para asegurarnos de que el tamaño no sea mayor que la
		// imagen y el max para evitar valores negativos insertados por el usuario
		int size = Math.max(0, Math.min(dimensionesCuadrado, dimensionesImagen));
		int inicio = (dimensionesImagen - size) / 2;
		int fin = inicio + size - 1;

		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce el grosor del borde (px): ");
		int grosorBorde = sc.nextInt();
		if (grosorBorde < 1) {
			grosorBorde = 1;
		}
		if (grosorBorde > size / 2) {
			grosorBorde = size / 2;
		}

		// REcorremos las filas y columnas. En el caso de los bmp se recorren de abajo a
		// arriba.
		for (int row = 0; row < dimensionesImagen; row++) {
			int filaInvertida = dimensionesImagen - 1 - row;
			int inicioFila = CABECERA_POR_DEFECTO + filaInvertida * tamFila;

			for (int col = 0; col < dimensionesImagen; col++) {
				int inicioPixel = inicioFila + col * 3;

				boolean dentroCuadrado = (col >= inicio && col <= fin && row >= inicio && row <= fin);
				boolean esBorde = (dentroCuadrado && (row < inicio + grosorBorde || row > fin - grosorBorde
						|| col < inicio + grosorBorde || col > fin - grosorBorde));

				byte[] color = esBorde ? colorCuadrado : colorFondo;

				imagen[inicioPixel] = color[0]; // B
				imagen[inicioPixel + 1] = color[1]; // G
				imagen[inicioPixel + 2] = color[2]; // R
			}

			// padding al final de la fila
			int inicioPadding = inicioFila + 3 * dimensionesImagen;
			int paddingFinal = inicioFila + tamFila;
			for (int i = inicioPadding; i < paddingFinal; i++) {
				imagen[i] = 0;
			}
		}
	}

	// Este método está investigado desde internet y verificado desde el javadoc.
	private byte[] littleEndian(int dato) {
		// 1. Creamos un objeto de ByteBuffer que nos va a poner en el buffer un array
		// del tamaño indicado en .alLocate
		ByteBuffer conversor = ByteBuffer.allocate(4);
		// 2. Después le decimos el tipo de orden que quiere tener. El tipo va a ser
		// Little Endian, necesario para transcribir lo que ha puesto el usuario de
		// tamaños a los valores del .bmp
		conversor.order(ByteOrder.LITTLE_ENDIAN);
		// 3. Se le pasa el dato a convertir
		conversor.putInt(dato);
		// 4. Como resultado da el array indicado
		return conversor.array();
	}

	private byte[] pedirColoresRGB() {
		Scanner sc = new Scanner(System.in);
		byte[] colores = new byte[3];

		try {
			System.out.println("Indica el color en formato RGB:");
			System.out.print("R(0-255): ");
			int r = sc.nextInt();
			System.out.print("G(0-255): ");
			int g = sc.nextInt();
			System.out.print("B(0-255): ");
			int b = sc.nextInt();

			if (r > 255 || r < 0 || b > 255 || b < 0 || g > 255 || g < 0) {
				throw new IllegalArgumentException("Los valores no están en los rangos correctos");
			}

			colores[0] = (byte) b;
			colores[1] = (byte) g;
			colores[2] = (byte) r;

		} catch (Exception e) {
			System.out.println("Error, el formato no es correcto " + e.getMessage());
			System.err.println("Se va a usar un color por defecto (ESTO SE DEBE CAMBIAR)");
			colores[0] = 0;
			colores[1] = 0;
			colores[2] = 0;
		}

		return colores;

	}

}
