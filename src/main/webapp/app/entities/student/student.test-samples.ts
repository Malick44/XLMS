import { IStudent, NewStudent } from './student.model';

export const sampleWithRequiredData: IStudent = {
  id: '3da4cf75-e65a-41e5-9d05-1cda5b83a83c',
  name: 'deploy of',
  email: 'Paige.Barrows@hotmail.com',
};

export const sampleWithPartialData: IStudent = {
  id: '54a2608a-342d-424f-a2a9-404a6c7d7aa7',
  name: 'Unbranded Loti impactful',
  email: 'Arnulfo59@hotmail.com',
};

export const sampleWithFullData: IStudent = {
  id: '0c3e3594-0cb9-4c5d-9c1e-b7c3b3d3c23a',
  name: 'Frozen invoice Vision-oriented',
  email: 'Stan_Morissette71@yahoo.com',
};

export const sampleWithNewData: NewStudent = {
  name: 'Re-engineered',
  email: 'Gerald_Grady@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
