package com.negocio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemPedido> items;
    private LocalDateTime fecha;
    private double subtotal;  // Nuevo campo para el total sin descuento
    private double descuento; // Porcentaje de descuento (0-100)
    private double total;

    public Pedido(int id, Cliente cliente) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        this.id = id;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.subtotal = 0.0;
        this.descuento = 0.0;
        this.total = 0.0;
    }

    public void agregarItem(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (!producto.hayStock(cantidad)) {
            throw new IllegalStateException("No hay suficiente stock del producto");
        }

        items.add(new ItemPedido(producto, cantidad));
        producto.reducirStock(cantidad);
        calcularSubtotal(); // Calcula el subtotal primero
        aplicarDescuento(); // Luego aplica el descuento si existe
    }

    private void calcularSubtotal() {
        subtotal = 0.0;
        for (ItemPedido item : items) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
    }

    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }

        this.descuento = porcentaje;
        aplicarDescuento(); // Aplica el descuento
        return this.total;
    }

    private void aplicarDescuento() {
        if (descuento > 0) {
            double montoDescuento = subtotal * (descuento / 100);
            this.total = subtotal - montoDescuento;
        } else {
            this.total = subtotal;
        }
    }

    // Método para remover descuento
    public void removerDescuento() {
        this.descuento = 0.0;
        this.total = subtotal;
    }

    // Getters adicionales
    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getMontoDescuento() {
        return subtotal * (descuento / 100);
    }

    // Getters originales
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItems() {
        return new ArrayList<>(items);
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", items=" + items +
                ", fecha=" + fecha +
                ", subtotal=" + String.format("%.2f", subtotal) +
                ", descuento=" + String.format("%.2f%%", descuento) +
                ", total=" + String.format("%.2f", total) +
                '}';
    }
}

class ItemPedido {
    private Producto producto;
    private int cantidad;

    public ItemPedido(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return cantidad + " x " + producto.getNombre() + " (" + producto.getPrecio() + " c/u)";
    }
}