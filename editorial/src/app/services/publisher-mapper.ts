import { Publisher, NewPublisher } from '../models/publisher.model';

export interface ApiPublisher {
    id: number;
    name: string;
    city: string;
}

export type ApiNewPublisher = Omit<ApiPublisher, 'id'>;

export function fromApiPublisher(a: ApiPublisher): Publisher {
    return {
        id: a.id,
        name: a.name,
        city: a.city,
    };
}

export function toApiNewPublisher(t: NewPublisher): ApiNewPublisher {
    return {
        name: t.name,
        city: t.city,
    };
}

