import { Role, Task } from "../models";

/**
 * E07 – Map / Set
 */

/**
 * @param tareas Lista de tareas
 * @returns Map id -> tarea
 */
export function indexTasksById(tareas: Task[]): Map<string, Task> {
  const mapa = new Map<string, Task>();
  for (const tarea of tareas) {
    mapa.set(tarea.id, tarea);
  }
  return mapa;
}

/**
 * @param roles Roles (con duplicados)
 * @returns Set roles unicos
 */
export function uniqueRoles(roles: Role[]): Set<Role> {
  return new Set(roles);
}

/**
 * @param sesiones Sesiones activas
 * @param token Token sesion
 * @param ahora Hora actual
 * @returns Fecha previa o undefined
 */
export function touchSession(
  sesiones: Map<string, Date>,
  token: string,
  ahora: Date
): Date | undefined {
  const previo = sesiones.get(token);
  sesiones.set(token, ahora);
  return previo;
}
