/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public class MonstruoNormal extends CartaMonstruo {
    
    public MonstruoNormal(String nombre, int ataque, int defensa, String posicion){
    
        configurarDatos(nombre, ataque, defensa, posicion);
    
    }
    
    @Override
    public int calcularPoder(){
    return ataque;
    }
}
