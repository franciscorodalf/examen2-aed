
/**
 * Funcion que elimina espacios en blanco al principio y al final
 * @param cabecera La cadena de texto
 * @returns La cadena normalizada
 */
export function normalizeBearer(cabecera: string): string {
  return cabecera.replace(/\s+/g, " ").trim();
}

/** 
 * @param valor El numero a limitar
 * @returns 0 si < 0, 1 si > 1, o valor
 * @throws Error Si no es finito
 */
export function clamp01(valor: number): number {
  if (!Number.isFinite(valor)) {
    throw new Error("El valor debe ser un numero finito");
  }
  return Math.max(0, Math.min(1, valor));
}

/**
 * Convierte un valor a boolean
 * @param valor El valor a convertir
 * @returns false si es null o undefined
 */
export function safeBool(valor: boolean | null | undefined): boolean {
  return valor ?? false;
}
