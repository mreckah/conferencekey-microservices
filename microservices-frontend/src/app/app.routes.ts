import { Routes } from '@angular/router';
import { KeynotesComponent } from './components/keynotes/keynotes.component';
import { ConferencesComponent } from './components/conferences/conferences.component';

export const routes: Routes = [
  { path: '', redirectTo: '/keynotes', pathMatch: 'full' },
  { path: 'keynotes', component: KeynotesComponent },
  { path: 'conferences', component: ConferencesComponent }
];
