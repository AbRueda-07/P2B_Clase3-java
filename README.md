                                      # P2B_Clase3-java
ARANZA BRIGITTE RUEDA ALVARADO 

Arlin Guisel Castillo Cermeño  
1. Base de Datos y Persistencia
Mejora #1 
 Ubicación: InventarioService
Descripción del cambio:
Se actualizó el método agregarProducto para que, antes de insertar un nuevo producto, verifique si ya existe otro con el mismo nombre (ignorando mayúsculas/minúsculas). En caso afirmativo, se lanza una excepción para evitar la duplicación.
Justificación:
Esta mejora asegura la unicidad de los nombres de productos dentro del sistema. Evita errores de duplicidad en el inventario, confusión al realizar ventas o consultas, y mejora la integridad de los datos persistidos.
Mejora #2
 Ubicación: Pedido – models
Descripción del cambio:
Se actualizó la clase Pedido para incluir un nuevo atributo LocalDateTime fechaHora. Este atributo registra automáticamente la fecha y hora exacta en que se crea un nuevo pedido. El formato de la fecha es ISO-8601, por ejemplo: “2025-08-02T10:30:15.582857700.”
Justificación:
Esta mejora permite mantener un registro preciso del momento exacto en que cada pedido fue generado, lo cual facilita auditorías, reportes de ventas por fecha y mejora el control histórico de los pedidos al momento de que se quieran revisar.
4. Interfaz de Usuario y Usabilidad
Mejora #1
Ubicación: InventarioService – Services
 Descripción del cambio:
Se implementó un nuevo método mostrarResumenInventario() en la clase InventarioService, que despliega un listado de los productos con su nombre, stock actual y cantidad total de ventas realizadas. Además, se modificó el método venderProducto para que actualice correctamente el conteo de ventas cada vez que se realiza una venta.
También se agregó en el menú principal la opción de mostrar Resumen del inventario, para saber sobre los registros y que estos se muestren en orden.
Justificación:
Esta mejora proporciona un control visual rápido y claro sobre el estado del inventario y el desempeño de las ventas. Facilita la toma de decisiones sobre reposición de productos y análisis de rotación, mejorando la gestión del inventario.
Mejora #2
Ubicación: Clase Main.java – menú principal - método que elimine productos 
Descripción del cambio: Se agregó una confirmación al usuario antes de eliminar un producto del inventario. Y también se agregó una opción más al menú principal.
Justificación: Previene eliminaciones accidentales y permite al usuario confirmar que realmente desea borrar el producto. O por si el usuario simplemente desea eliminar dicho producto.
