
package com.mycompany.metamorphosis.model;

/**
 *
 * @author eu 
 */
public class Receita {

    private String item1;
    private String item2;
    private String resultado;

    public Receita(String item1, String item2, String resultado) {
        this.item1 = item1;
        this.item2 = item2;
        this.resultado = resultado;
    }

    public boolean combina(String a, String b) {
        return (item1.equals(a) && item2.equals(b))
            || (item1.equals(b) && item2.equals(a));
    }

    public String getResultado() {
        return resultado;
    }
    
}
