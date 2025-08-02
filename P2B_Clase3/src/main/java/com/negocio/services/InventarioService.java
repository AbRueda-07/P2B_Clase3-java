package com.negocio.services;

import com.negocio.models.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventarioService {
    private final List<Producto> productos;

    public InventarioService() {
        this.productos = new ArrayList<>();
        inicializarProductos();
    }

    private void inicializarProductos() {
        agregarProducto(new Producto(1, "Hamburguesa", 15.50, 20));
        agregarProducto(new Producto(2, "Pizza", 25.00, 15));
        agregarProducto(new Producto(3, "Tacos", 8.75, 30));
        agregarProducto(new Producto(4, "Refresco", 3.50, 50));
    }

    public Producto buscarProductoPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }

        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public boolean venderProducto(int id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            return false;
        }

        try {
            producto.reducirStock(cantidad);
            producto.agregarVenta(cantidad); // Registrar la venta
            System.out.printf("Venta realizada: %d unidades de %s%n", cantidad, producto.getNombre());
            return true;
        } catch (IllegalStateException e) {
            System.out.printf("No se pudo realizar la venta: %s%n", e.getMessage());
            return false;
        }
    }

    public List<Producto> obtenerProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getStock() > 0) {
                disponibles.add(producto);
            }
        }
        return Collections.unmodifiableList(disponibles);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return Collections.unmodifiableList(productos);
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (buscarProductoPorId(producto.getId()) != null) {
            throw new IllegalStateException("Ya existe un producto con este ID");
        }

        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(producto.getNombre())) {
                throw new IllegalStateException("Ya existe un producto con este nombre: " + producto.getNombre());
            }
        }

        productos.add(producto);
    }

    public void reponerStock(int id, int cantidad) {
        Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        producto.aumentarStock(cantidad);
    }

    // Ahora el método mostrarResumenInventario NO es estático
    public void mostrarResumenInventario() {
        System.out.println("📦 Resumen de Inventario:");
        System.out.printf("%-20s %-10s %-10s%n", "Producto", "Stock", "Ventas");
        System.out.println("---------------------------------------------");
        for (Producto producto : obtenerTodosLosProductos()) {
            System.out.printf("%-20s %-10d %-10d%n",
                    producto.getNombre(),
                    producto.getStock(),
                    producto.getVentas());
        }
    }
    public void eliminarProductoPorId(int id) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null) {
            productos.remove(producto);
        } else {
            throw new IllegalArgumentException("Producto no encontrado");
        }
    }

}
