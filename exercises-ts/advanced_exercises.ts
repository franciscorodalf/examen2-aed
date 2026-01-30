
// ==========================================
// EJERCICIO 3: Promesas y Async/Await
// ==========================================

// 1. Función que simula una API con Promesa
function simularApi(): Promise<string> {
    return new Promise((resolve, reject) => {
        console.log("Cargando datos...");
        setTimeout(() => {
            const exito = true; // Cambiar a false para probar error
            if (exito) {
                resolve("¡Datos recibidos del servidor!");
            } else {
                reject("Error 500: Fallo en el servidor");
            }
        }, 1500); // 1.5 segundos de espera
    });
}

// 2. Función asíncrona para consumir la promesa
async function obtenerDatos() {
    try {
        const respuesta = await simularApi();
        console.log("ÉXITO:", respuesta);
    } catch (error) {
        console.error("ERROR CAPTURADO:", error);
    } finally {
        console.log("Petición finalizada.");
    }
}

// Ejecutar:
obtenerDatos();


// ==========================================
// EJERCICIO 4: Clases y Genéricos
// ==========================================

// 1. Clase genérica Caja<T>
class Caja<T> {
    private contenido: T | null = null;

    constructor(valorInicial?: T) {
        if (valorInicial) this.contenido = valorInicial;
    }

    guardar(valor: T): void {
        this.contenido = valor;
        console.log(`Guardado en caja: ${valor}`);
    }

    obtener(): T | null {
        return this.contenido;
    }
}

// 2. Instancias
console.log("\n--- Caja de Números ---");
const cajaNumeros = new Caja<number>();
cajaNumeros.guardar(100);
console.log("Obtenido:", cajaNumeros.obtener()); // 100

console.log("\n--- Caja de Texto ---");
const cajaTexto = new Caja<string>("Hola Mundo");
console.log("Obtenido:", cajaTexto.obtener()); // Hola Mundo
cajaTexto.guardar("Adiós");
