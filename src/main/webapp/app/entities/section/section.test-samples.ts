import { ISection, NewSection } from './section.model';

export const sampleWithRequiredData: ISection = {
  id: '1354c4aa-5cf9-4984-a680-155713777d77',
};

export const sampleWithPartialData: ISection = {
  id: 'c4134fe4-a316-4c82-b7ee-ba70918f3c3b',
  title: 'Japan',
};

export const sampleWithFullData: ISection = {
  id: '8d7422e3-bf49-4655-b9a1-625670cd787d',
  title: 'GB Metal',
  description: 'Implementation Cheese Ports',
  courseId: 'Kuna',
  servicePath: 'cyan SDD Wallis',
};

export const sampleWithNewData: NewSection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
