import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeynoteService } from '../../services/keynote.service';
import { Keynote } from '../../models/keynote.interface';

@Component({
  selector: 'app-keynotes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './keynotes.component.html',
  styleUrl: './keynotes.component.css'
})
export class KeynotesComponent implements OnInit {
  keynotes: Keynote[] = [];
  loading = false;
  error: string | null = null;

  constructor(private keynoteService: KeynoteService) {}

  ngOnInit(): void {
    this.loadKeynotes();
  }

  loadKeynotes(): void {
    this.loading = true;
    this.error = null;
    this.keynoteService.getAllKeynotes().subscribe({
      next: (response) => {
        this.keynotes = response._embedded?.keynotes || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load keynotes';
        this.loading = false;
        console.error('Error loading keynotes:', err);
      }
    });
  }
}

