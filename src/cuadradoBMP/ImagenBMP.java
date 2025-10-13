package cuadradoBMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImagenBMP {
	private String nombre;
	private byte dimensionesImagen;
	private byte dimensionesCuadrado;
	private byte[] colorFondo;
	private byte[] colorCuadrado;
	
	private byte tamFichero;
	private byte[] endianTamFichero;
	private final byte INDICE_TAM_FICHERO = 2;
	private byte[] endianDimensionesImagen;
	private final byte INDICE_ANCHO = 18;
	private final byte INDICE_ALTO = 22;
	private byte tamImagen;
	private byte[] endianTamImagen;
	private final byte INDICE_TAM_IMAGEN = 34;
	
	private byte[] imagen;
	private byte[] imagenPorDefecto;
	
	private final byte CABECERA_POR_DEFECTO = 54;
	private final byte TAM_FILA = (byte) (((3 * dimensionesImagen + 3) / 4) * 4);
	
	
	private File fichero;
	private FileOutputStream ficheroEscritura;
	private FileInputStream ficheroLectura;

	public ImagenBMP(String nombre, byte dimensionesImagen, byte dimensionesCuadrado, byte[] colorFondo, byte[] colorCuadrado) {
		super();
		this.nombre = nombre;
		this.dimensionesImagen = dimensionesImagen;
		this.dimensionesCuadrado = dimensionesCuadrado;
		this.colorFondo = colorFondo.clone();
		this.colorCuadrado = colorCuadrado.clone();
		
		endianDimensionesImagen = littleEndian(this.dimensionesCuadrado).clone();
		tamImagen = (byte) (TAM_FILA * this.dimensionesImagen);
		endianTamImagen = littleEndian(tamImagen).clone();
		tamFichero = (byte) (CABECERA_POR_DEFECTO + tamImagen);
		endianTamFichero = littleEndian(tamFichero).clone();
	}
	
	private void crearFicherBMP () {
		imagen = new byte [tamFichero];
	}
	
	private void crearImagen() {
		
		byte[] defecto = {
				66, 77,
				0, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianTamFichero
				0, 0, 0, 0,
				54, 0, 0, 0,
				40, 0, 0, 0,
				0, 0 , 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN --> endianDimensionesImangenes
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
			if(i >= INDICE_TAM_FICHERO && i <= INDICE_TAM_FICHERO + 3) {
				if(contadorSubIndice < 4) {
					defecto[i] = endianTamFichero[contadorSubIndice];
					contadorSubIndice++;
				}
				if(contadorSubIndice == 3)
					contadorSubIndice = 0;
			}
			// CONTINUAR CON IFS AQUI
		}
	}
	
	private byte[] littleEndian (int dato) {
		ByteBuffer conversor = ByteBuffer.allocate(4);
		conversor.order(ByteOrder.LITTLE_ENDIAN);
		conversor.putInt(dato);
		return conversor.array();
	}

}
