package uteclab.despensaRincon.entities;

public class Estadistica {

    private String nombre = "Pepito";
    private float cant1 = -1;
    private float cant2 = -1;
    private float cant3 = -1;

    public Estadistica() {
    }

    public Estadistica(String nombre) {
        this.nombre = nombre;
    }

    public Estadistica(String nombre, float cant1) {
        this.nombre = nombre;
        this.cant1 = cant1;
    }

    public Estadistica(String nombre, float cant1, float cant2) {
        this.nombre = nombre;
        this.cant1 = cant1;
        this.cant2 = cant2;
    }


    public Estadistica(String nombre, float cant1, float cant2, float cant3) {
        this.nombre = nombre;
        this.cant1 = cant1;
        this.cant2 = cant2;
        this.cant3 = cant3;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCant1() {
        return cant1;
    }

    public void setCant1(float cant1) {
        this.cant1 = cant1;
    }

    public float getCant2() {
        return cant2;
    }

    public void setCant2(float cant2) {
        this.cant2 = cant2;
    }

    public float getCant3() {
        return cant3;
    }

    public void setCant3(float cant3) {
        this.cant3 = cant3;
    }
}
