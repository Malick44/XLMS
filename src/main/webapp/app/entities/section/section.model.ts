export interface ISection {
  id: string;
  title?: string | null;
  description?: string | null;
  courseId?: string | null;
  servicePath?: string | null;
}

export type NewSection = Omit<ISection, 'id'> & { id: null };
