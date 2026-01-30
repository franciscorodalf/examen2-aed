/**
 * E09 – Async/Promises
 */

/**
 * @param ms Milisegundos espera
 * @returns Promesa resuelta tras espera
 * @throws Error Si ms < 0 o infinito
 */
export async function delay(ms: number): Promise<void> {
  if (!Number.isFinite(ms) || ms < 0) {
    throw new Error("El tiempo debe ser un número finito positivo");
  }
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/**
 * @param fn Funcion asincrona
 * @param intentos Maximo intentos
 * @returns Resultado
 * @throws Ultimo error si falla todo
 */
export async function retry<T>(fn: () => Promise<T>, intentos: number): Promise<T> {
  let ultimoError: unknown;
  for (let i = 0; i < intentos; i++) {
    try {
      return await fn();
    } catch (err) {
      ultimoError = err;
    }
  }
  throw ultimoError;
}

/**
 * @param valores Promesas numericas
 * @returns Suma resultados
 * @throws Error Si resultado no finito
 */
export async function parallelSum(valores: Array<Promise<number>>): Promise<number> {
  const resultados = await Promise.all(valores);
  return resultados.reduce((acumulado, valor) => {
    if (!Number.isFinite(valor)) {
      throw new Error("Uno de los valores no es un número finito");
    }
    return acumulado + valor;
  }, 0);
}
