export interface ICourse {
  id: string;
  title?: string | null;
  authorId?: string | null;
  authorName?: string | null;
  description?: string | null;
  category?: string | null;
  subCategory?: string | null;
  level?: string | null;
  language?: string | null;
  duration?: string | null;
  price?: string | null;
  rating?: string | null;
  ratingCount?: string | null;
  thumbnail?: string | null;
  url?: string | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
