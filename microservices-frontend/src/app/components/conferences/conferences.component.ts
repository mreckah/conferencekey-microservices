import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConferenceService } from '../../services/conference.service';
import { Conference } from '../../models/conference.interface';

@Component({
  selector: 'app-conferences',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './conferences.component.html',
  styleUrl: './conferences.component.css'
})
export class ConferencesComponent implements OnInit {
  conferences: Conference[] = [];
  loading = false;
  error: string | null = null;

  constructor(private conferenceService: ConferenceService) {}

  ngOnInit(): void {
    this.loadConferences();
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

  getStars(score: number): string {
    const fullStars = Math.floor(score);
    return '★'.repeat(fullStars) + '☆'.repeat(5 - fullStars);
  }
}

