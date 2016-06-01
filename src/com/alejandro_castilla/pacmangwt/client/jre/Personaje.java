
package com.alejandro_castilla.pacmangwt.client.jre;

/**
 * Cada uno de los personajes del juego.
 * @author Alejandro Castilla Quesada e Ismael Yeste Espín
 */
public class Personaje {
    private int x, y;
    private int offsetx, offsety;
    private final int MOVIMIENTOS_POR_CELDA;
    private int direccionActual= Map.RIGHT, direccionSiguiente= Map.RIGHT;
    /**
     * Laberinto.
     */
    protected Map map;
    private int x0, y0;
    
    /**
     * Constructor.
     * @param map Objeto Map que representa el laberinto.
     * @param x Coordenada x de la posición inicial.
     * @param y Coordenada y de la posición inicial.
     * @param movsPorCelda  Número de movimientos necesarios para pasar de una celda a otra. 
     * Cuanto mayor sea menor velocidad.
     */
    Personaje(Map map, int x, int y, int movsPorCelda){
        this.map = map;
        this.x=x0=x;
        this.y=y0=y;
        MOVIMIENTOS_POR_CELDA=movsPorCelda;
    }
    
    /**
     * Devuelve al personaje a su posición inicial.
     */
    public void reiniciar(){
        x=x0;
        y=y0;
        offsetx=offsety=0;
        direccionActual=direccionSiguiente= Map.RIGHT;
    }
    
    /**
     * Coordenada x.
     * @return Coordenada x de la posición.
     */
    public int getX() {
        return x;
    }

    /**
     * Coordenada y.
     * @return Coordenada y de la posición. 
     */
    public int getY() {
        return y;
    }

    /**
     * Offset del eje y. El offset se usa para indicar la posición relativa a la celda actual.
     * Entre dos celdas ha un offset de movsPorCelda indicado en el constructor.
     * @return Componente x del offset.
     */
    public int getOffsetx() {
        return offsetx;
    }

    /**
     * Offset del eje x. El offset se usa para indicar la posición relativa a la celda actual.
     * Entre dos celdas ha un offset de movsPorCelda indicado en el constructor.
     * @return Componente y del offset.
     */
    public int getOffsety() {
        return offsety;
    }

    /**
     * Cambia la direccion siguiente. La direccion actual se actualiza en el metodo move();
     * @param direccionSiguiente Direccion siguiente. Constantes definidas en la clase Map.
     */
    public void setDireccion(int direccionSiguiente) {
        this.direccionSiguiente = direccionSiguiente;
    }
    
    /**
     * Dirección actual.
     * @return Direccion actual
     */
    public int getDireccion () {
        return direccionActual;
    }
    
    /**
     * Número de movimientos entre dos celdas.
     * @return Número de movimientos entre dos celdas.
     */
    public int getMovimientosCelda() {
        return MOVIMIENTOS_POR_CELDA;
    }
      
    /**
     * Movimeinto del personaje. Cada vez que se llama incrementa el offset en la direccion 
     * actual o cambia de celda. En caso de que la dirección siguiente sea distinta de la actual, si 
     * es posible se actualiza.
     * @return Resultado del movimiento. Solo tiene sentido si se sobreescribe el metodo. Por defecto devuelve 0.
     */
    public int mover(){
        
        int signo=0;
        boolean vertical=false;
       
        
        if(offsetx==0 && offsety==0 && direccionActual!=direccionSiguiente)
            if(map.move(x, y, direccionSiguiente))
                direccionActual=direccionSiguiente;
     
        switch(direccionActual){
            case Map.LEFT:
                signo=-1;
                break;
            case Map.RIGHT:
                signo=1;
                break;
            case Map.UP:
                signo=-1;
                vertical=true;
                break;
            case Map.BOTTOM:
                signo=1;
                vertical=true;
                break;
        }
               
        if(!vertical){
            if(offsetx+signo==MOVIMIENTOS_POR_CELDA){
                x++;
                offsetx=0;
            }
            else if(offsetx+signo==-MOVIMIENTOS_POR_CELDA){
                x--;
                offsetx=0;
            }
            else if((offsetx+signo!=1 && offsetx+signo!=-1) || map.move(x, y, direccionActual)){
                offsetx+=signo;
            }
        }
        else {
            if(offsety+signo==MOVIMIENTOS_POR_CELDA){
                y++;
                offsety=0;
            }
            else if(offsety+signo==-MOVIMIENTOS_POR_CELDA){
                y--;
                offsety=0;
            }
            else if((offsety+signo!=1 && offsety+signo!=-1) || map.move(x, y, direccionActual)){
                offsety+=signo;
            }
        }
        
        if(x==0 && offsetx==0){
            x= map.getWidth()-1;
        }
        else if(x== map.getWidth()-1 && offsetx==0)
            x=0;
        
        return 0;
    }
}
