package com.negocio.services;

import com.negocio.models.Cliente;
import com.negocio.models.Pedido;
import com.negocio.models.Producto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoService {
    private final List<Pedido> pedidos;
    private final InventarioService inventarioService;
    private int contadorPedidos;

    public PedidoService(InventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.pedidos = new ArrayList<>();
        this.inventarioService = inventarioService;
        this.contadorPedidos = 1;
    }

    // ERROR 11 CORREGIDO: Incremento correcto del contador
    public Pedido crearPedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        Pedido pedido = new Pedido(contadorPedidos++, cliente); // Incrementa después de asignar
        pedidos.add(pedido);
        return pedido;
    }

    // ERROR 12 CORREGIDO: Lógica mejorada para agregar productos
    public boolean agregarProductoAPedido(int pedidoId, int productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        Pedido pedido = buscarPedidoPorId(pedidoId);
        if (pedido == null) {
            return false;
        }

        Producto producto = inventarioService.buscarProductoPorId(productoId);
        if (producto == null || !producto.hayStock(cantidad)) {
            return false;
        }

        // Intenta vender la cantidad completa en una sola operación
        if (inventarioService.venderProducto(productoId, cantidad)) {
            pedido.agregarItem(producto, cantidad); // Usa el método corregido de Pedido
            return true;
        }

        return false;
    }

    public Pedido buscarPedidoPorId(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        return null;
    }

    public double calcularIngresosTotales() {
        double ingresos = 0;
        for (Pedido pedido : pedidos) {
            ingresos += pedido.getTotal();
        }
        return ingresos;
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return Collections.unmodifiableList(pedidos); // Devuelve lista inmutable
    }

    public void mostrarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        } else {
            for (Pedido pedido : pedidos) {
                System.out.println(pedido);
            }
        }
    }
}