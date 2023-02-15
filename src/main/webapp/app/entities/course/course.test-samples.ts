import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: '98c9991d-c9da-411e-a53c-64ed275db9c4',
  title: 'Lake',
  authorId: 'Borders',
};

export const sampleWithPartialData: ICourse = {
  id: 'acb4f8a0-9b63-4de5-9584-b16b949f8c36',
  title: 'mindshare calculating haptic',
  authorId: 'bluetooth Handmade violet',
  description: 'Generic Car secured',
  category: 'Legacy',
  subCategory: 'Frozen Lempira',
  level: 'Circles Delaware contextually-based',
  language: 'Response Shore Investment',
  price: 'auxiliary',
  rating: 'calculate target',
  url: 'http://judy.name',
};

export const sampleWithFullData: ICourse = {
  id: '2efe3e5f-3156-4334-87bb-1a022eb5810f',
  title: 'Rhode Sierra Internal',
  authorId: 'capacitor pink architect',
  authorName: 'Plastic',
  description: 'African Mill mobile',
  category: 'Aruba Mouse Response',
  subCategory: 'Chicken paradigms',
  level: 'Grocery',
  language: 'Loan',
  duration: '1080p withdrawal Buckinghamshire',
  price: 'front-end calculating',
  rating: 'overriding sensor',
  ratingCount: 'Concrete',
  thumbnail: 'Executive',
  url: 'https://rae.name',
};

export const sampleWithNewData: NewCourse = {
  title: 'circuit parsing',
  authorId: 'deposit Manager',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
