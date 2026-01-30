
// ==========================================
// EJERCICIO 1: Tipos e Interfaces
// ==========================================

// 1. Define una interfaz 'Usuario'
interface Usuario {
    id: number;
    nombre: string;
    email: string;
    activo?: boolean; // Opcional
}

// 2. Crea un objeto 'usuario1'
const usuario1: Usuario = {
    id: 1,
    nombre: "Francisco",
    email: "fran@example.com",
    activo: true
};

// 3. Crea una función 'imprimirUsuario'
function imprimirUsuario(user: Usuario): string {
    return `${user.id} - ${user.nombre}`;
}

// Prueba:
console.log("Ejercicio 1:", imprimirUsuario(usuario1));


// ==========================================
// EJERCICIO 2: Manipulación de Arrays
// ==========================================
const productos = [
    { id: 1, nombre: "Laptop", precio: 1000, categoria: "Electrónica" },
    { id: 2, nombre: "Mouse", precio: 25, categoria: "Electrónica" },
    { id: 3, nombre: "Teclado", precio: 50, categoria: "Electrónica" },
    { id: 4, nombre: "Silla", precio: 150, categoria: "Muebles" },
    { id: 5, nombre: "Escritorio", precio: 300, categoria: "Muebles" }
];

// 1. Filter: Solo Electrónica
const electronica = productos.filter(p => p.categoria === "Electrónica");
console.log("Solo Electrónica:", electronica);

// 2. Map: Solo nombres
const nombres = productos.map(p => p.nombre);
console.log("Nombres:", nombres);

// 3. Reduce: Precio total
const total = productos.reduce((acumulado, actual) => acumulado + actual.precio, 0);
console.log("Precio Total:", total);
