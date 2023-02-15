import dayjs from 'dayjs/esm';

export interface IAssessment {
  id: string;
  title?: string | null;
  courseId?: string | null;
  sectionId?: string | null;
  studentId?: string | null;
  examDate?: dayjs.Dayjs | null;
  timeLimit?: number | null;
  score?: number | null;
}

export type NewAssessment = Omit<IAssessment, 'id'> & { id: null };
