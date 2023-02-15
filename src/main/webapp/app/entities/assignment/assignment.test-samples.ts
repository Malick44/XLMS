import dayjs from 'dayjs/esm';

import { IAssignment, NewAssignment } from './assignment.model';

export const sampleWithRequiredData: IAssignment = {
  id: '7c8d53f5-eff5-447c-ae25-3ef3db33f497',
  courseId: 'Grocery monitor Account',
  studentId: 'drive',
  examDate: dayjs('2023-02-15'),
};

export const sampleWithPartialData: IAssignment = {
  id: '9c559870-dca4-4441-9e93-431e0203884d',
  courseId: 'Applications Strategist',
  studentId: 'XSS withdrawal bandwidth',
  examDate: dayjs('2023-02-14'),
  timeLimit: 61795,
  score: 89632,
};

export const sampleWithFullData: IAssignment = {
  id: 'a93e8f86-f570-49b7-bbee-cd01baa9bb24',
  title: 'Customizable synergy',
  courseId: 'Avon',
  studentId: 'pink Practical',
  examDate: dayjs('2023-02-14'),
  timeLimit: 12933,
  score: 48101,
};

export const sampleWithNewData: NewAssignment = {
  courseId: 'payment AI',
  studentId: 'Handcrafted',
  examDate: dayjs('2023-02-14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
