import { IInstructor, NewInstructor } from './instructor.model';

export const sampleWithRequiredData: IInstructor = {
  id: 'e3e2faa7-4e20-447d-8f58-4c9a105ed62d',
  name: 'Automotive recontextualize',
  email: 'Nyah.Brown@gmail.com',
};

export const sampleWithPartialData: IInstructor = {
  id: '12ea52c3-97c4-4e07-9ed9-9c77c8472f5e',
  name: 'collaboration card',
  email: 'August99@gmail.com',
};

export const sampleWithFullData: IInstructor = {
  id: 'ab69ed6b-8a4a-43bb-83fa-e5ef3cae70b8',
  name: 'Metal Fresh enable',
  email: 'Noah_Runte48@yahoo.com',
};

export const sampleWithNewData: NewInstructor = {
  name: 'one-to-one',
  email: 'Leatha76@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
