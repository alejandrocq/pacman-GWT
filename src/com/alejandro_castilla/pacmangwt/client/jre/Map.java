
package com.alejandro_castilla.pacmangwt.client.jre;

/**
 * Contiene una matriz de caracteres que representa un maze.
 * @author Alejandro Castilla Quesada e Ismael Yeste Espín
 */

public class Map {
   
    private static final String MAP[]={
        "1AAAAAAAAAAAA21AAAAAAAAAAAA2",
        "I............DI............D",
        "I.5BB6.5BBB6.DI.5BBB6.5BB6.D",
        "IoD  I.D   I.DI.D   I.D  IoD",
        "I.7AA8.7AAA8.78.7AAA8.7AA8.D",
        "I..........................D",
        "I.5BB6.56.5BBBBBB6.56.5BB6.D",
        "I.7AA8.DI.7AA21AA8.DI.7AA8.D",
        "I......DI....DI....DI......D",
        "3BBBB6.D3BB6 DI 5BB4I.5BBBB4",
        "     I.D1AA8 78 7AA2I.D     ",
        "     I.DI          DI.D     ",
        "     I.DI 5B____B6 DI.D     ",
        "AAAAA8.78 D      I 78.7AAAAA",
        "      .   D ---- I   .      ",
        "BBBBB6.56 D FFFF I 56.5BBBBB",
        "     I.DI 7AAAAAA8 DI.D     ",
        "     I.DI          DI.D     ",
        "     I.DI 5BBBBBB6 DI.D     ",
        "1AAAA8.78 7AA21AA8 78.7AAAA2",
        "I............DI............D",
        "I.5BB6.5BBB6.DI.5BBB6.5BB6.D",
        "I.7A2I.7AAA8.78.7AAA8.D1A8.D",
        "Io..DI................DI..oD",
        "3B6.DI.56.5BBBBBB6.56.DI.5B4",
        "1A8.78.DI.7AA21AA8.DI.78.7A2",
        "I......DI....DI....DI......D",
        "I.5BBBB43BB6.DI.5BB43BBBB6.D",
        "I.7AAAAAAAA8.78.7AAAAAAAA8.D",
        "I..........................D",
        "3BBBBBBBBBBBBBBBBBBBBBBBBBB4"
    };
    
    /**
     * Constantes de dirección.
     */
    public static final int LEFT =0x1, RIGHT =0x10, UP =0x100, BOTTOM =0x1000;
    /**
     * Constantes de puntuación.
     */
    public static final int COCONUT_POINTS =10, BIG_COCONUT_POINTS =50;
    /**
     * Constantes para definir cada una de las celdas del mapa.
     */
    public static final char
            RECTANGLE_UP ='A',
            RECTANGLE_DOWN ='B',
            RECTANGLE_RIGHT ='D',
            RECTANGLE_LEFT ='I',
            BOTTOM_RIGHT_CORNER='5',
            UPPER_RIGHT_CORNER='7',
            BOTTOM_LEFT_CORNER='6',
            UPPER_LEFT_CORNER='8',
            UPPER_RIGHT_BIG_CORNER='3',
            BOTTOM_RIGHT_BIG_CORNER='1',
            UPPER_LEFT_BIG_CORNER='4',
            BOTTOM_LEFT_BIG_CORNER='2',
            SMALL_DOT ='.',
            BIG_DOT ='o',
            WALL ='_',
            GHOST ='F',
            FREE =' ';
    
    
    private char[][] maze;
    private int maxPoints;
    
   /**
    * Crea un array de caracteres que contiene el map del juego.
    * @param map Array en el que cada caracter representa una celda
    * del map.
    */
   public Map(String[] map){
       int rows=map.length;
       maze =new char[rows][];
       for(int i=0;i<map.length;i++){
           maze[i]=map[i].toCharArray();
       }
       
       for(char[] row: maze){
           for(char element:row){
               if(element== SMALL_DOT)
                   maxPoints += COCONUT_POINTS;
               else if(element== BIG_DOT)
                   maxPoints += BIG_COCONUT_POINTS;
           }
       }
   }
   
   /**
    * Se usa para comprbar que se ha ganado.
    * @return Maxima puntuación obtenible.
    */
    public int getMaxPoints() {
        return maxPoints;
    }
     
   /**
    * Crea un array de caracteres que contiene el mapa del juego.
    * Utiliza el mapa por defecto.
    */
   public Map(){
        this(MAP);
    }
   
   /**
    * Comer el coco de la celda actual.
    * @param row Fila actual.
    * @param column Columna actual.
    * @return 0 si no había coco, 1 si había un coco pequeño y 2 si había un coco grande.
    */
   public int eat(int row, int column){
       if(maze[column][row]== SMALL_DOT){
           maze[column][row]= FREE;
           return COCONUT_POINTS;
       }
       else if(maze[column][row]== BIG_DOT){
           maze[column][row]= FREE;
           return BIG_COCONUT_POINTS;
       }
       return 0;
   }
   
   /**
    * Indica si un personaje se puede move en una dirección
    * si se encuentra en una determinada celda.
    * @param x Posición del personaje en el eje X.
    * @param y Posición del personaje en el eje Y.
    * @param direction Dirección del movimiento. Puede ser:
    * Map.UP, Map.BOTTOM, Map.LEFT o Map.RIGHT.
    * @return 
    */
   public boolean move(int x, int y, int direction){
        char next='-';
        try{
            switch(direction){
                case UP:
                    next= maze[y-1][x];
                    break;
                case BOTTOM:
                    next= maze[y+1][x];
                    break;
                case RIGHT:
                    next= maze[y][x+1];
                    break;
                case LEFT:
                    next= maze[y][x-1];
                    break;
            }
        }
        catch(ArrayIndexOutOfBoundsException ex){
            return false;
        }
        
        if(next== SMALL_DOT || next== BIG_DOT || next== FREE)
            return true;
        else if((next== WALL && direction== UP) || next== GHOST && direction== RIGHT)
            return true;
        return false;
   }
   
    /**
     * Ver elemento del mapa situado en la celda indicada.
     * @param fila Fila de la celda.
     * @param columna Columna de la celda.
     * @return Caracter correspondiente a la celda consultada.
     */
    public char getCellAt(int fila, int columna){
        return maze[columna][fila];
    }
    
   
    /**
     * Altura en filas del maze.
     * @return Número de filas del mapa.
     */
   public int getHeight(){
       return maze.length;
   }
   
   /**
    * Anchera en columnas del maze.
    * @return Número de columnas del mapa.
    */
   public int getWidth(){
       return maze[0].length;
   }
}
