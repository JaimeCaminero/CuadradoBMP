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
		bytesPadding = (4 - (3 * this.dimensionesImagen % 4)) % 4;
		tamFila = ((3 * this.dimensionesImagen + 3) / 4) * 4;
		endianDimensionesImagen = littleEndian(this.dimensionesCuadrado).clone();
		tamImagen = tamFila * this.dimensionesImagen;
		endianTamImagen = littleEndian(tamImagen).clone();
		tamFichero = CABECERA_POR_DEFECTO + tamImagen;
		endianTamFichero = littleEndian(tamFichero).clone();
		imagenPorDefecto = crearImagenInicial(); // Creamos la cabecera
		// crearImagen(); // Definimos el array que definirá la iamgen
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
		// UTILIZARLO COMO GUÍA Y CUANDO SEA DEFINITIVO EL CÓDIGO, QUITARLO
		for (int i = 0; i < imagen.length; i++)
			System.out.println(i + ": " + imagen[i]);
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

		int contadorSubIndice = 0;
		for (int i = 0; i < defecto.length; i++) {
			if (i >= INDICE_TAM_FICHERO && i <= INDICE_TAM_FICHERO + 3) {
				if (contadorSubIndice < 4) {
					defecto[i] = endianTamFichero[contadorSubIndice];
					contadorSubIndice++;
				}
				if (contadorSubIndice == 4)
					contadorSubIndice = 0;
			} else if (i >= INDICE_ANCHO && i <= INDICE_ANCHO + 3) {
				if (contadorSubIndice < 4) {
					defecto[i] = endianDimensionesImagen[contadorSubIndice];
					contadorSubIndice++;
				}
				if (contadorSubIndice == 4)
					contadorSubIndice = 0;
			} else if (i >= INDICE_ALTO && i <= INDICE_ALTO + 3) {
				if (contadorSubIndice < 4) {
					defecto[i] = endianDimensionesImagen[contadorSubIndice];
					contadorSubIndice++;
				}
				if (contadorSubIndice == 4)
					contadorSubIndice = 0;
			} else if (i >= INDICE_TAM_IMAGEN && i <= INDICE_TAM_IMAGEN + 3) {
				if (contadorSubIndice < 4) {
					defecto[i] = endianTamImagen[contadorSubIndice];
					contadorSubIndice++;
				}
				if (contadorSubIndice == 4)
					contadorSubIndice = 0;
			}

		}
		return defecto.clone();
	}

	public void crearImagen() {
		// QUITAR ESTOS SYSOS CUANDO SE TERMINE
		System.out.println("bytesPadding: " + bytesPadding);
		System.out.println("Dimension parcial: " + tamFila);
		imagen = new byte[tamFichero];
		for (int i = 0; i < imagen.length; i++) {
			if (i < CABECERA_POR_DEFECTO) {
				imagen[i] = imagenPorDefecto[i];
				// AÑADIR LOS COLORES SELECCIONADOS AQUÍ
				// IMPLEMENTAR LA LÓGICA DEL CUADRADO INTERIOR AQUÍ
			} else if (bytesPadding != 0
					&& (((i - CABECERA_POR_DEFECTO + 1) % tamFila < bytesPadding) && (i - CABECERA_POR_DEFECTO != 0)))
				imagen[i] = (byte) 0;
			else
				imagen[i] = (byte) 255;
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
