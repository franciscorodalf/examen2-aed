import { JwtPayload, Role } from "../models";

/**
 * E05 – Union + type guards (unknown) + JWT
 */

/**
 * @param id Identificador (string/number)
 * @returns ID normalizado
 * @throws Error Si ID vacio
 */
export function normalizeId(id: string | number): string {
  let normalizado = typeof id === "number" ? id.toString() : id.trim();
  if (!normalizado) {
    throw new Error("El ID no puede estar vacio");
  }
  return normalizado;
}

/**
 * @param valor Valor a comprobar
 * @returns true si es JwtPayload
 */
export function isJwtPayload(valor: unknown): valor is JwtPayload {
  if (!valor || typeof valor !== "object") return false;

  const p = valor as JwtPayload;

  return (
    typeof p.sub === "string" &&
    p.sub.length > 0 &&
    (p.role === Role.USER || p.role === Role.ADMIN) &&
    typeof p.exp === "number" &&
    Number.isFinite(p.exp) &&
    p.exp >= 0
  );
}

/**
 * @param datos Datos token
 * @param rol Rol requerido
 * @throws Error Si rol incorrecto
 */
export function requireRole(datos: JwtPayload, rol: Role): void {
  if (datos.role !== rol) {
    throw new Error(`Acceso denegado: se requiere rol ${rol}`);
  }
}
