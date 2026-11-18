/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public class MounstroEfecto extends CartaMonstruo {
    
    private String efecto;
    private int bonificacion;
    
    public void configurarEfecto(String efecto, int bonificacion){
    
        if (bonificacion < 0){
            throw new IllegalArgumentException(
                "La bonificación no puede ser negativa."
            );
        }
        
        this.efecto = efecto;
        this.bonificacion = bonificacion;
        }
    
    public String consultarEfecto(){
        return efecto;
    }
    
    @Override
    public int calcularPoder(){
        return ataque + bonificacion;
    }
    
    
    
}
