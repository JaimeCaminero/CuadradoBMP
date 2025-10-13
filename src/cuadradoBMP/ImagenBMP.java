package cuadradoBMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImagenBMP {
	private String nombre;
	private byte dimensionesImagen;
	private byte tamCuadrado;
	private byte[] colorFondo;
	private byte[] colorCuadrado;
	
	private byte[] imagen;
	private byte tamFichero;
	private byte tamImagen;
	
	private final byte CABECERA_POR_DEFECTO = 54;
	private final byte TAM_FILA = (byte) (((3 * dimensionesImagen + 3) / 4) * 4);
	
	
	private File fichero;
	private FileOutputStream ficheroEscritura;
	private FileInputStream ficheroLectura;

	public ImagenBMP(String nombre, byte dimensionesImagen, byte tamCuadrado, byte[] colorFondo, byte[] colorCuadrado) {
		super();
		this.nombre = nombre;
		this.dimensionesImagen = dimensionesImagen;
		this.tamCuadrado = tamCuadrado;
		this.colorFondo = colorFondo.clone();
		this.colorCuadrado = colorCuadrado.clone();
		tamImagen = (byte) (TAM_FILA * this.dimensionesImagen);
		tamFichero = (byte) (CABECERA_POR_DEFECTO + tamImagen);
	}
	
	private void crearFicherBMP () {
		imagen = new byte [tamFichero];
	}
	
	private void crearImagen() {
		
		byte[] defecto = {
				66, 77,
//				?, ?, ?, ?, --> MIRAR CONVERSIÓN LITTLE-ENDIAN
				0, 0, 0, 0,
				54, 0, 0, 0,
				40, 0, 0, 0,
				dimensionesImagen, 0 , 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN
				dimensionesImagen, 0, 0, 0, // --> MIRAR CONVERSIÓN LITTLE-ENDIAN
				1, 0,
				24, 0,
				0, 0, 0, 0,
//				?, ?, ?, ?, --> MIRAR CONVERSIÓN LITTLE-ENDIAN
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0
		};
	}

}
