
# Soluciones

## Ejercicio 1
```typescript
interface Usuario {
    id: number;
    nombre: string;
    email: string;
    activo?: boolean;
}

const usuario1: Usuario = {
    id: 1,
    nombre: "Juan",
    email: "juan@example.com",
    activo: true
};

function imprimirUsuario(user: Usuario): string {
    return `${user.id} - ${user.nombre}`;
}
```

## Ejercicio 2
```typescript
const electronica = productos.filter(p => p.categoria === "Electrónica");
const nombres = productos.map(p => p.nombre);
const total = productos.reduce((acc, p) => acc + p.precio, 0);
```

## Ejercicio 3
```typescript
function simularApi(): Promise<string> {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            Math.random() > 0.5 ? resolve("Datos recibidos") : reject("Error de conexión");
        }, 2000);
    });
}

async function obtenerDatos() {
    try {
        const datos = await simularApi();
        console.log("Éxito:", datos);
    } catch (error) {
        console.error("Error:", error);
    }
}
```

## Ejercicio 4
```typescript
class Caja<T> {
    private contenido: T | null = null;

    guardar(valor: T) {
        this.contenido = valor;
    }

    obtener(): T | null {
        return this.contenido;
    }
}

const cajaNumeros = new Caja<number>();
cajaNumeros.guardar(100);

const cajaTexto = new Caja<string>();
cajaTexto.guardar("Hola");
```
