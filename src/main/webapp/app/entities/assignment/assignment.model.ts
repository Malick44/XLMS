import dayjs from 'dayjs/esm';

export interface IAssignment {
  id: string;
  title?: string | null;
  courseId?: string | null;
  studentId?: string | null;
  examDate?: dayjs.Dayjs | null;
  timeLimit?: number | null;
  score?: number | null;
}

export type NewAssignment = Omit<IAssignment, 'id'> & { id: null };
