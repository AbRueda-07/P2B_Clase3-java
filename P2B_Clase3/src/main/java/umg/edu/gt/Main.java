package umg.edu.gt;

import com.negocio.models.Cliente;
import com.negocio.models.Pedido;
import com.negocio.models.Producto;
import com.negocio.services.InventarioService;
import com.negocio.services.PedidoService;
import com.negocio.db.DatabaseManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static InventarioService inventarioService;
    private static PedidoService pedidoService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=== FOODNET - Simulador de Negocio de Comida Rápida ===");
        // Inicializar servicios
        inventarioService = new InventarioService();
        pedidoService = new PedidoService(inventarioService);
        scanner = new Scanner(System.in);

        // Menú principal
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    mostrarInventario();
                    break;
                case 2:
                    crearNuevoPedido();
                    break;
                case 3:
                    agregarProductoAPedido();
                    break;
                case 4:
                    mostrarPedidos();
                    break;
                case 5:
                    mostrarIngresos();
                    break;
                case 6:
                    aplicarDescuentoAPedido();
                    break;
                case 7:
                    mostrarResumenInventario();
                case 8:
                    eliminarProducto();
                    break;
                case 9:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }

        DatabaseManager.cerrarConexion();
        scanner.close();
        System.out.println("¡Gracias por usar FoodNet!");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Ver inventario");
        System.out.println("2. Crear nuevo pedido");
        System.out.println("3. Agregar producto a pedido");
        System.out.println("4. Ver pedidos");
        System.out.println("5. Ver ingresos totales");
        System.out.println("6. Aplicar descuento a pedido");
        System.out.println("7. Mostrar resumen de inventario");
        System.out.println("8. Eliminar producto");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerEntero() {
        int opcion = -1;
        try {
            opcion = scanner.nextInt();
        } catch (InputMismatchException e) {
            // Limpiar buffer para evitar bucle infinito
            scanner.nextLine();
        }
        return opcion;
    }

    private static void mostrarInventario() {
        System.out.println("\n--- INVENTARIO ---");
        for (Producto producto : inventarioService.obtenerProductosDisponibles()) {
            System.out.println(producto);
        }
    }

    private static void crearNuevoPedido() {
        scanner.nextLine(); // Limpia buffer pendiente (si vienes de nextInt o similar)
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Teléfono del cliente: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(1, nombre, telefono);
        Pedido pedido = pedidoService.crearPedido(cliente);

        System.out.println("Pedido creado con ID: " + pedido.getId());
    }


    private static void agregarProductoAPedido() {
        System.out.print("ID del pedido: ");
        int pedidoId = leerEntero();
        System.out.print("ID del producto: ");
        int productoId = leerEntero();
        System.out.print("Cantidad: ");
        int cantidad = leerEntero();
        scanner.nextLine(); // Limpiar buffer

        if (pedidoService.agregarProductoAPedido(pedidoId, productoId, cantidad)) {
            System.out.println("Producto agregado exitosamente");
        } else {
            System.out.println("Error al agregar producto");
        }
    }

    private static void mostrarPedidos() {
        System.out.println("\n--- PEDIDOS ---");
        pedidoService.mostrarPedidos();
    }

    private static void mostrarIngresos() {
        double ingresos = pedidoService.calcularIngresosTotales();
        System.out.printf("Ingresos totales: Q%.2f%n", ingresos);
    }

    private static void aplicarDescuentoAPedido() {
        System.out.println("\n--- APLICAR DESCUENTO A PEDIDO ---");
        System.out.print("Ingrese el ID del pedido: ");
        int idPedido = leerEntero();

        Pedido pedido = pedidoService.buscarPedidoPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido no encontrado");
            return;
        }

        System.out.print("Ingrese el porcentaje de descuento (0-100): ");
        double porcentaje = -1;
        try {
            porcentaje = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida para el porcentaje.");
            scanner.nextLine();
            return;
        }

        try {
            double nuevoTotal = pedido.aplicarDescuento(porcentaje);
            System.out.println("\nDescuento aplicado correctamente");
            System.out.printf("Nuevo total del pedido #%d: Q%.2f (%.1f%% de descuento)%n",
                    pedido.getId(), nuevoTotal, porcentaje);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void mostrarResumenInventario() {
        System.out.println("\n--- RESUMEN DE INVENTARIO ---");
        inventarioService.mostrarResumenInventario();
    }

    private static void eliminarProducto() {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int id = leerEntero();
        Producto producto = inventarioService.buscarProductoPorId(id);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.printf("¿Está seguro que desea eliminar el producto \"%s\"? (s/n): ", producto.getNombre());
        String confirmacion = scanner.nextLine().trim().toLowerCase();

        if (confirmacion.equals("s")) {
            inventarioService.eliminarProductoPorId(id);
            System.out.println("Producto eliminado exitosamente.");
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

}
