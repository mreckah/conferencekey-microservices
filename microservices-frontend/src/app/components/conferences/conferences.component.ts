import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConferenceService } from '../../services/conference.service';
import { KeynoteService } from '../../services/keynote.service';
import { Conference, TypeConference } from '../../models/conference.interface';
import { Keynote } from '../../models/keynote.interface';

@Component({
  selector: 'app-conferences',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './conferences.component.html',
  styleUrl: './conferences.component.css'
})
export class ConferencesComponent implements OnInit {
  conferences: Conference[] = [];
  keynotes: Keynote[] = [];
  loading = false;
  error: string | null = null;
  showModal = false;
  isEditMode = false;
  currentConference: Conference = this.getEmptyConference();
  conferenceDate: string = '';

  constructor(
    private conferenceService: ConferenceService,
    private keynoteService: KeynoteService
  ) {}

  ngOnInit(): void {
    this.loadConferences();
    this.loadKeynotes();
  }

  loadConferences(): void {
    this.loading = true;
    this.error = null;
    this.conferenceService.getAllConferences().subscribe({
      next: (conferences) => {
        this.conferences = conferences;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load conferences';
        this.loading = false;
        console.error('Error loading conferences:', err);
      }
    });
  }

  loadKeynotes(): void {
    this.keynoteService.getAllKeynotes().subscribe({
      next: (keynotes) => {
        this.keynotes = keynotes;
      },
      error: (err) => {
        console.error('Error loading keynotes:', err);
      }
    });
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.currentConference = this.getEmptyConference();
    this.conferenceDate = '';
    this.showModal = true;
  }

  openEditModal(conference: Conference): void {
    this.isEditMode = true;
    this.currentConference = { ...conference };
    // Convert Date to datetime-local format (YYYY-MM-DDTHH:mm)
    const date = new Date(conference.date);
    this.conferenceDate = date.toISOString().slice(0, 16);
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentConference = this.getEmptyConference();
  }

  saveConference(): void {
    // Convert string date to Date object
    const conferenceDate = this.conferenceDate ? new Date(this.conferenceDate) : new Date();

    if (this.isEditMode && this.currentConference.id) {
      // Update existing conference
      const updateData = {
        ...this.currentConference,
        date: conferenceDate
      };
      this.conferenceService.updateConference(this.currentConference.id, updateData).subscribe({
        next: () => {
          this.loadConferences();
          this.closeModal();
        },
        error: (err) => {
          this.error = 'Failed to update conference';
          console.error('Error updating conference:', err);
        }
      });
    } else {
      // Create new conference - remove id field
      const conferenceData = {
        titre: this.currentConference.titre,
        type: this.currentConference.type,
        date: conferenceDate,
        duree: this.currentConference.duree,
        nombreInscrits: this.currentConference.nombreInscrits,
        score: this.currentConference.score,
        keynoteId: this.currentConference.keynoteId
      };
      this.conferenceService.createConference(conferenceData as Conference).subscribe({
        next: () => {
          this.loadConferences();
          this.closeModal();
        },
        error: (err) => {
          this.error = 'Failed to create conference';
          console.error('Error creating conference:', err);
        }
      });
    }
  }

  deleteConference(id: number): void {
    if (confirm('Are you sure you want to delete this conference?')) {
      this.conferenceService.deleteConference(id).subscribe({
        next: () => {
          this.loadConferences();
        },
        error: (err) => {
          this.error = 'Failed to delete conference';
          console.error('Error deleting conference:', err);
        }
      });
    }
  }

  getStars(score: number): string {
    const fullStars = Math.floor(score);
    return '★'.repeat(fullStars) + '☆'.repeat(5 - fullStars);
  }

  private getEmptyConference(): Conference {
    return {
      id: 0,
      titre: '',
      type: TypeConference.ACADEMIQUE,
      date: new Date(),
      duree: 60,
      nombreInscrits: 0,
      score: 0,
      keynoteId: 0
    };
  }
}

