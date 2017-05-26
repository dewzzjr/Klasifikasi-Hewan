/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author Dewangga
 */
public enum ClassType {
    MAMMAL(1, "Mamalia"),
    BIRD(2, "Burung"),
    REPTILE(3, "Reptil"),
    FISH(4, "Ikan"),
    AMPHIBIAN(5, "Amfibi"),
    BUG(6, "Serangga"),
    INVERTEBRATE(7, "Invertebrata"),
    NULL(0, "");

    private final int code;
    private final String name;

    ClassType(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }
}
