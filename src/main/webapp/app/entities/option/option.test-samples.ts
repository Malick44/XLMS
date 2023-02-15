import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: 'b7f1c2eb-d212-4ec2-a20f-082e5543b022',
  text: 'discrete',
  questionId: 'Taiwan',
  correct: false,
};

export const sampleWithPartialData: IOption = {
  id: '72c05c23-9d2b-4f08-b4d1-ac8e96b67b4f',
  text: 'Harbor Implemented Wooden',
  questionId: 'Rubber Junctions',
  correct: true,
  assessmentId: 'Avon',
  assignmentId: 'global Operative',
};

export const sampleWithFullData: IOption = {
  id: '20d54223-c958-42a2-9555-e1c4d57d7bc5',
  text: 'Shoes',
  questionId: 'Colombian index Granite',
  correct: true,
  assessmentId: 'web-enabled software',
  assignmentId: 'incentivize Handmade Guatemala',
  isSelected: false,
};

export const sampleWithNewData: NewOption = {
  text: 'cross-platform',
  questionId: 'black',
  correct: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
