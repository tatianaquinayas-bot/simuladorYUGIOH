/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 57323
 */
public class RegistroCombate {
    
        public String atacante;
        public String defensor;
        public int danio;
        public String resultado;
        public String tipoAtaque;
        
        public String generarReporte(){
        
            return "Tipo de Ataque: " + tipoAtaque + "\nAtacante: " + atacante
                    + "\nDefensor: " + defensor + "\nDaño: " + danio +"\nResultado: " + resultado;}}
