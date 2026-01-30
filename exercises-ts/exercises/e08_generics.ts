/**
 * E08 – Genéricos
 */

/**
 * @param elementos Array elementos
 * @returns Primer elemento
 * @throws Error Si vacio
 */
export function first<T>(elementos: T[]): T {
  if (elementos.length === 0) {
    throw new Error("El array está vacío");
  }
  return elementos[0];
}

/**
 * @param elementos Array con duplicados
 * @returns Array sin duplicados
 */
export function unique<T>(elementos: T[]): T[] {
  return Array.from(new Set(elementos));
}

/**
 * @param elementos Array elementos
 * @param funcionClave Función clave
 * @returns Objeto agrupado
 */
export function groupBy<T, K extends string | number>(
  elementos: T[],
  funcionClave: (item: T) => K
): Record<K, T[]> {
  const resultado = {} as Record<K, T[]>;
  for (const elemento of elementos) {
    const clave = funcionClave(elemento);
    if (!resultado[clave]) {
      resultado[clave] = [];
    }
    resultado[clave].push(elemento);
  }
  return resultado;
}
