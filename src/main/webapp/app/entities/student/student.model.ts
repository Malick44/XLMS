export interface IStudent {
  id: string;
  name?: string | null;
  email?: string | null;
}

export type NewStudent = Omit<IStudent, 'id'> & { id: null };
