import { Keynote } from './keynote.interface';
import { Review } from './review.interface';

export enum TypeConference {
  ACADEMIQUE = 'ACADEMIQUE',
  COMMERCIALE = 'COMMERCIALE'
}

export interface Conference {
  id: number;
  titre: string;
  type: TypeConference;
  date: Date;
  duree: number;
  nombreInscrits: number;
  score: number;
  keynoteId: number;
  keynote?: Keynote;
  reviews?: Review[];
}

