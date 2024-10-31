import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'ab2c7d29-1947-402d-9ff8-29dc7daf9144',
};

export const sampleWithPartialData: IAuthority = {
  name: '637a675d-76bc-4f7c-ae90-5ce1578680e2',
};

export const sampleWithFullData: IAuthority = {
  name: 'c3534735-b190-41a7-aaa1-d722e7ab4cb4',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
