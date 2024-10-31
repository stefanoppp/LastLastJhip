import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 3105,
  login: 'RiA@f1oP\\GDUKQjB',
};

export const sampleWithPartialData: IUser = {
  id: 9856,
  login: 'h4',
};

export const sampleWithFullData: IUser = {
  id: 22129,
  login: '_4kRLC@uNqx9u\\EgbMmj',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
