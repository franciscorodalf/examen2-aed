import { Injectable } from '@angular/core';
import { NewPublisher, Publisher } from '../models/publisher.model';

@Injectable({ providedIn: 'root' })
export class TasksService {
  private publisher: Publisher[] = [
    { id: 1, name: 'Paco', city: 'Islas Canarias' },
    { id: 2, name: 'Pedro', city: "San Francisco" },
  ];
  private nextId = 3;

  list(): Publisher[] {
    return this.publisher;
  }

  add(data: NewPublisher): Publisher {
    const created: Publisher = { id: this.nextId++, ...data };
    this.publisher = [created, ...this.publisher];
    return created;
  }

  remove(id: number): void {
    this.publisher = this.publisher.filter(t => t.id !== id);
  }
}
export type { Publisher };

