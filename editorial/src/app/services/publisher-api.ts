import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { NewPublisher, Publisher } from '../models/publisher.model';
import { ApiPublisher, fromApiPublisher, toApiNewPublisher } from './publisher-mapper';

@Injectable({ providedIn: 'root' })
export class PublisherApiService {
  private baseUrl = `${environment.apiBaseUrl}/publisher`;

  constructor(private http: HttpClient) { }

  list(): Observable<Publisher[]> {
    return this.http.get<ApiPublisher[]>(this.baseUrl).pipe(
      map(arr => arr.map(fromApiPublisher)),
      catchError(() => throwError(() => new Error('No se pudo conectar con la API de publisher.')))
    );
  }

  getById(id: number): Observable<Publisher> {
    return this.http.get<ApiPublisher>(`${this.baseUrl}/${id}`).pipe(
      map(fromApiPublisher),
      catchError(() => throwError(() => new Error('No se pudo obtener el publisher.')))
    );
  }

  create(data: NewPublisher): Observable<Publisher> {
    return this.http.post<ApiPublisher>(this.baseUrl, toApiNewPublisher(data)).pipe(
      map(fromApiPublisher),
      catchError(() => throwError(() => new Error('No se pudo crear el publisher.')))
    );
  }

  update(id: number, data: NewPublisher): Observable<Publisher> {
    return this.http.patch<ApiPublisher>(`${this.baseUrl}/${id}`, toApiNewPublisher(data)).pipe(
      map(fromApiPublisher),
      catchError(() => throwError(() => new Error('No se pudo actualizar el publisher.')))
    );
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      catchError(() => throwError(() => new Error('No se pudo eliminar el publisher.')))
    );
  }
}
