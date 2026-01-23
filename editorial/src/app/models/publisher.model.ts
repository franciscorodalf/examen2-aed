export interface Publisher {
  id: number;
  name: string;
  city: string;
}

export type NewPublisher = Omit<Publisher, 'id'>;
