BACKEND:

Arreglar plantilla de envio de correos:
Cuando se elimina un proveedor, obtener todos los productos y compras relacionados, y para cada uno, eliminar el proveedor del arreglo de proveedores.
Cuando se elimina un cliente, obtener las ventas y desvincular
Hacer metodo desvincular proveedor de producto (desde el producto)
ESTADISTICAS:

Entidad:
    String nombre
    String cant1
    String cant2
    String cant3


1) Cantidad de productos por categoria
    INPUTS:
    OUTPUT: nombre (Nombre de la categoria), cant1 (Cantidad de productos de dicha categoria)

2) Cantidad de productos vendidos por categoria desde x fecha hasta x fecha
   INPUTS: fecha1, fecha2
   OUTPUT: nombre (Nombre de la categoria), cant (Cantidad de productos vendidos)

3) Ganancia  desde x fecha a x fecha
   INPUTS: fecha1, fecha2
   OUTPUT: cant1 (Ganacia, cant2 - cant3), cant2 (total ganado), cant3 (total perdido)

4) Top 5 Productos mas vendido desde x fecha a tal fecha 
   INPUTS: fecha1, fecha2
   OUTPUT: nombre (Nombre del producto), cant1 (cantidad de unidades vendidas), cant2 (total generado por esas ventas)

5) Top 5 Productos mas comprados desde x fecha hasta x fecha 
   INPUTS: fecha1, fecha2
   OUTPUT: nombre (Nombre del producto), cant1 (cantidad de unidades compradas), cant2 (total perdido por esas compras)

6) Del total de ventas, cuantas fueron a clientes finales (null) y cuantas a clientes comunes desde x fecha hasta x fecha 
   INPUTS: fecha1, fecha2
   OUTPUT: cant1 (Cantidad a clientes registrados), cant2 (Cantidad a clientes finales)

7) Total de compras a tal proveedor desde x fecha hasta x fecha 
   INPUTS: fecha1, fecha2
   OU
8) INTPUT: nombre (Nombre del proveedor), cant1 (Cantidad de compras a ese proveedor), cant2(Total gastado en dichas compras)

8) Top 5 Clientes con mayor gasto desde x fecha hasta x fecha -- nombre, cant
   INPUTS: fecha1, fecha2
   OUTPUT: nombre (Nombre del cliente), cant1 (Cantidad gastado),

9) Total  cantidad de ventas (con total ingresos) ultimos 7 dias -- nombre, cant, cant2
   INPUTS: fecha1, fecha2
   OUTPUT: nombre (Dia), cant1 (Total de ventas del dia), cant2 (Total generado por ventas ese dia)

