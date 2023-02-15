export interface IQuestion {
  id: string;
  sectionId?: string | null;
  courseId?: string | null;
  text?: string | null;
  assignmentId?: string | null;
  assessmentId?: string | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
