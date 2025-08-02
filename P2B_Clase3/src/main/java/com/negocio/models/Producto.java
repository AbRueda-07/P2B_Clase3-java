package com.negocio.models;

public class Producto {
    private final int id;  // Hacemos id final ya que no debería cambiar después de la creación
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        validarParametros(id, nombre, precio, stock);

        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    private void validarParametros(int id, String nombre, double precio, int stock) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    // Setters con validaciones
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        this.precio = precio;
    }

    protected void setStock(int stock) {  // Hacemos protected para controlar acceso
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    // Métodos de negocio mejorados
    public void reducirStock(int cantidad) {
        validarCantidad(cantidad, "reducir");
        if (!hayStockSuficiente(cantidad)) {
            throw new IllegalStateException(String.format(
                    "No hay suficiente stock disponible (Stock actual: %d, Cantidad solicitada: %d)",
                    stock, cantidad));
        }
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        validarCantidad(cantidad, "aumentar");
        this.stock += cantidad;
    }

    public boolean hayStock(int cantidad) {
        return hayStockSuficiente(cantidad);
    }

    // Métodos privados de apoyo
    private boolean hayStockSuficiente(int cantidad) {
        return stock >= cantidad;
    }

    private void validarCantidad(int cantidad, String operacion) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    String.format("La cantidad a %s debe ser positiva", operacion));
        }
    }

    // Representación mejorada del objeto
    @Override
    public String toString() {
        return String.format(
                "Producto [ID: %d, Nombre: %s, Precio: $%.2f, Stock: %d unidades]",
                id, nombre, precio, stock);
    }

    // Método equals y hashCode para comparación de productos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    //mejoras
    private int ventas;

    public int getVentas() {
        return ventas;
    }

    public void agregarVenta(int cantidad) {
        this.ventas += cantidad;
    }

}