
package com.alejandro_castilla.pacmangwt.client.jre;


import pacman.jre.ComecocosFrame;
import pacman.jre.LaberintoFrame;
import pacman.jre.LaberintoFrame.Modo;
import javax.swing.JOptionPane;

/**
 * Hebra que implementa el movimiento del juego.
 * @author Alejandro Castilla Quesada e Ismael Yeste Espín
 */
public class Mueve extends Thread{
    private DatosComecocos comecocos;
    private Fantasma[] fantasmas;
    private boolean finJuego=false, enPausa=false;
    private ComecocosFrame frame;
    private LaberintoFrame panel;
    private int puntuacion=0;
    private int modoDios=0;
    private int maxPuntos;
    private int deltaT;

    /**
     * Constructor. En el se calcula el parámetro deltaT que es el tiempo entre dos frames.
     * @param cf Ventana de juego.
     * @param lf Panel donde se dibuja el laberinto.
     * @param pacman Personaje: Pacman.
     * @param fantasmas Personajes: Fantasmas.
     * @param maxPuntos Puntuación máxima. Si se alcanza se ha ganado.
     * @param nivel Nivel del juego ente 0 y 10.
     */
    public Mueve(ComecocosFrame cf, LaberintoFrame lf, DatosComecocos pacman, Fantasma[] fantasmas, 
            int maxPuntos, int nivel){
        comecocos=pacman;
        this.fantasmas=fantasmas;
        frame=cf;
        panel=lf;
        this.maxPuntos=maxPuntos;
        deltaT=50-nivel*5;
    }
    
    /**
     * Cambia la dirección del movimiento del comecocos. El cambio no es instantaneo, solo se
     * establece la dirección siguiente. El objeto apuntado por pacman es el encargado de actualizar.
     * @param direccion Direccion siguiente.
     */
    public void moverComecocos(int direccion){
        comecocos.setDireccion(direccion);
    }
    
    public synchronized void finalizar(){
        finJuego=true;
    }
    
    /**
     * Estado del juego.
     * @return true si el juego está en pausa.
     */
    public boolean enPausa(){
        return enPausa;
    }
    
    /**
     * Detiene el juego.
     */
    public synchronized void pausa(){
        enPausa=true;
    }
    
    /**
     * Reanuda el juego.
     */
    public synchronized void reanudar(){
        enPausa=false;
        notifyAll();
    }
    
    /**
     * Llama a los metodos move para cada personaje cada deltaT ms. Si pausa es true
     * se detiene la hebra. Si el comecocos se come un coco aumenta su puntuación.
     * Si el comecocos se come un coco grande, aumenta su puntuación y activa el modo
     * Dios durante 10 s. Si el comecocos se come un fantasma en el modo Dios, el fantasma
     * es devuelto a su posición inicial.Si se ha alcanzado la puntuación máxima se muestra un mensaje de
     * victoria y termina la ejecución. Si un fantasma se come al comecocos se muestra un mensaje 
     * de derrota y se detiene la ejecución.
     */
    @Override
    public void run() {
        try{
            while(!finJuego){
                panel.repaint();
                synchronized(this){
                    while(enPausa)
                        wait();
                }
                int puntos=comecocos.mover();
                puntuacion+=puntos;
                frame.puntuacion(puntuacion);
                if(puntos== Map.BIG_COCONUT_POINTS){
                    modoDios=10000/deltaT;
                }
                
                if(modoDios!=-1){
                    if(modoDios==0)
                        panel.modo(Modo.NORMAL);
                    else if(modoDios==10000/deltaT)
                        panel.modo(Modo.DIOS);
                     modoDios--;
                }
                
                for(Fantasma fantasma:fantasmas){
                    int resultado=fantasma.mover(modoDios!=-1);
                    if(resultado==Fantasma.CHOQUE_COMECOCOS){
                        if(modoDios!=-1){
                            fantasma.reiniciar();
                        }
                        else{
                            JOptionPane.showMessageDialog(panel, "HAS PERDIDO");
                            finJuego=true;
                            break;
                        }
                    }
                }
                
                if(puntuacion==maxPuntos){
                    JOptionPane.showMessageDialog(panel, "¡HAS GANADO!");
                    finJuego=true;
                }
                
                Thread.sleep(deltaT);
            }
        }
        catch(InterruptedException ex){
            System.out.println("Hilo interrumpido.");
        }
    }
        
    
}
