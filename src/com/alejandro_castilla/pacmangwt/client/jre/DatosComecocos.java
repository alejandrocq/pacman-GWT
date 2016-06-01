
package com.alejandro_castilla.pacmangwt.client.jre;

/**
 * Subclase de Personaje que representa al comecocos.
 * @author Alejandro Castilla Quesada e Ismael Yeste Espín
 */
public class DatosComecocos extends Personaje {
    

   /**
     * Constructor.
     * @param map Objeto Map que representa el laberinto.
     * @param x Coordenada x de la posición inicial.
     * @param y Coordenada y de la posición inicial.
     * @param movsPorCelda  Número de movimientos necesarios para pasar de una celda a otra. 
     * Cuanto mayor sea menor velocidad.
     */
    public DatosComecocos(Map map, int x, int y, int movsPorCelda) {
        super(map, x, y, movsPorCelda);
    }
    
    /**
     * Cuando se llama, se mueve al comecocos en la direccion correspondiente.
     * @return Puntos obtenidos. Puede ser 0, Map.COCONUT_POINTS o Map.BIG_COCONUT_POINTS.
     */
    @Override
    public int mover() {
        super.mover();
        int puntos= map.eat(getX(), getY());
        return puntos;
    }
    
}
