export interface IInstructor {
  id: string;
  name?: string | null;
  email?: string | null;
}

export type NewInstructor = Omit<IInstructor, 'id'> & { id: null };
