package cuadradoBMP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ImagenBMP {
	private String nombre;
	private int dimensionesImagen;
	private int dimensionesCuadrado;
	private byte[] colorFondo;
	private byte[] colorCuadrado;

	private int tamFichero;
	private byte[] endianTamFichero;
	private final int INDICE_TAM_FICHERO = 2;
	private byte[] endianDimensionesImagen;
	private final int INDICE_ANCHO = 18;
	private final int INDICE_ALTO = 22;
	private int tamImagen;
	private byte[] endianTamImagen;
	private final int INDICE_TAM_IMAGEN = 34;

	private byte[] imagen;
	private byte[] imagenPorDefecto;

	private final int CABECERA_POR_DEFECTO = 54;
	private int bytesPadding;
	private int tamFila;

	private File fichero;
	private FileOutputStream ficheroEscritura;
	private BufferedOutputStream bufferEscritura;
	private FileInputStream ficheroLectura;
	private BufferedInputStream bufferLectura;

	// HAY QUEAÑADIR EL COLOR AL CONSTRCUTOR
	public ImagenBMP(String nombre, int dimensionesImagen, int dimensionesCuadrado) throws FileNotFoundException {
		this.nombre = nombre;
		this.dimensionesImagen = dimensionesImagen;
		this.dimensionesCuadrado = dimensionesCuadrado;
//		this.colorFondo = colorFondo.clone();
//		this.colorCuadrado = colorCuadrado.clone();
		bytesPadding = (4 - (3 * this.dimensionesImagen % 4)) % 4;
		tamFila = ((3 * this.dimensionesImagen + 3) / 4) * 4;
		endianDimensionesImagen = littleEndian(this.dimensionesCuadrado).clone();
		tamImagen = tamFila * this.dimensionesImagen;
		endianTamImagen = littleEndian(tamImagen).clone();
		tamFichero = CABECERA_POR_DEFECTO + tamImagen;
		endianTamFichero = littleEndian(tamFichero).clone();
		imagenPorDefecto = crearImagenInicial();
		crearImagen();

		fichero = new File(nombre + ".bmp");
	}

	public void crearFicherBMP() throws IOException {
		if (!fichero.exists())
			fichero.createNewFile();
		ficheroEscritura = new FileOutputStream(fichero);
		bufferEscritura = new BufferedOutputStream(bufferEscritura);
		bufferEscritura.write(imagen);
		for (int i = 0; i < imagen.length; i++)
			System.out.println(i + ": " + imagen[i]);
		bufferEscritura.flush();
		System.out.println("Se ha creado con éxito");
	}

	public void cerrarRecursos() throws IOException {
		bufferEscritura.close();
		ficheroEscritura.close();
	}

	private byte[] crearImagenInicial() {

		byte[] defecto = {
				66, 77,
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianTamFichero
				0, 0, 0, 0,
				54, 0, 0, 0,
				40, 0, 0, 0,
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianDimensionesImangenes
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianDimensionesImangenes
				1, 0,
				24, 0,
				0, 0, 0, 0,
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianTamImagen
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0
		};

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

	private void crearImagen() {
		System.out.println("bytesPadding: " + bytesPadding);
		System.out.println("Dimension parcial: " + tamFila);
		imagen = new byte[tamFichero];
		for (int i = 0; i < imagen.length; i++) {
			if (i < CABECERA_POR_DEFECTO) {
				imagen[i] = imagenPorDefecto[i];
			} else if (bytesPadding != 0 && (((i - CABECERA_POR_DEFECTO + 1) % tamFila  < bytesPadding) && (i - CABECERA_POR_DEFECTO != 0)))
				imagen[i] = (byte) 0;
			else
				imagen[i] = (byte) 255;
		}
	}

	private byte[] littleEndian(int dato) {
		System.out.println("Dato: " + dato);
		ByteBuffer conversor = ByteBuffer.allocate(4);
		conversor.order(ByteOrder.LITTLE_ENDIAN);
		conversor.putInt(dato);
		System.out.println("Array: " + Arrays.toString(conversor.array()));
		return conversor.array();
	}

}
