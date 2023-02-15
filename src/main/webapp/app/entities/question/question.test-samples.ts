import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: '7e95d524-aea6-438c-8962-aac87478f875',
  sectionId: 'Lats Namibia',
};

export const sampleWithPartialData: IQuestion = {
  id: 'fabf579b-c663-49b8-b5af-d0869cf04d05',
  sectionId: 'Mauritania Investor Direct',
  courseId: 'overriding',
  text: 'Egyptian',
  assessmentId: 'parse Director',
};

export const sampleWithFullData: IQuestion = {
  id: '766f4830-a1e7-48ea-b6b2-fba6c5c9f9d9',
  sectionId: 'Chicken invoice',
  courseId: 'Frozen Integrated',
  text: 'Mexican',
  assignmentId: 'violet firewall heuristic',
  assessmentId: 'superstructure interface',
};

export const sampleWithNewData: NewQuestion = {
  sectionId: 'synthesizing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
