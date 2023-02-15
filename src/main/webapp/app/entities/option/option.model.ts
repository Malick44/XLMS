export interface IOption {
  id: string;
  text?: string | null;
  questionId?: string | null;
  correct?: boolean | null;
  assessmentId?: string | null;
  assignmentId?: string | null;
  isSelected?: boolean | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
