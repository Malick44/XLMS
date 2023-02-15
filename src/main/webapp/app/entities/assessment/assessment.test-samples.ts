import dayjs from 'dayjs/esm';

import { IAssessment, NewAssessment } from './assessment.model';

export const sampleWithRequiredData: IAssessment = {
  id: 'd26b2791-fe8e-4355-b9ad-e97c8fbd9085',
};

export const sampleWithPartialData: IAssessment = {
  id: '29747222-98c8-4aa1-b657-cd5e01c99543',
  title: 'Plastic',
  sectionId: '24/7 Implementation system',
  timeLimit: 30730,
  score: 92521,
};

export const sampleWithFullData: IAssessment = {
  id: 'd999f5d6-c780-48c6-a3a1-b6a7aef013f7',
  title: 'Concrete leverage Global',
  courseId: 'Circles',
  sectionId: 'Metal',
  studentId: 'Operations',
  examDate: dayjs('2023-02-14'),
  timeLimit: 61158,
  score: 37640,
};

export const sampleWithNewData: NewAssessment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
