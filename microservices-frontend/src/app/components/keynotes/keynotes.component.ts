import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { KeynoteService } from '../../services/keynote.service';
import { Keynote } from '../../models/keynote.interface';

@Component({
  selector: 'app-keynotes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './keynotes.component.html',
  styleUrl: './keynotes.component.css'
})
export class KeynotesComponent implements OnInit {
  keynotes: Keynote[] = [];
  loading = false;
  error: string | null = null;
  showModal = false;
  isEditMode = false;
  currentKeynote: Keynote = this.getEmptyKeynote();

  constructor(private keynoteService: KeynoteService) {}

  ngOnInit(): void {
    this.loadKeynotes();
  }

  loadKeynotes(): void {
    this.loading = true;
    this.error = null;
    this.keynoteService.getAllKeynotes().subscribe({
      next: (keynotes) => {
        this.keynotes = keynotes;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load keynotes';
        this.loading = false;
        console.error('Error loading keynotes:', err);
      }
    });
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.currentKeynote = this.getEmptyKeynote();
    this.showModal = true;
  }

  openEditModal(keynote: Keynote): void {
    this.isEditMode = true;
    this.currentKeynote = { ...keynote };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentKeynote = this.getEmptyKeynote();
  }

  saveKeynote(): void {
    if (this.isEditMode && this.currentKeynote.id) {
      // Update existing keynote
      this.keynoteService.updateKeynote(this.currentKeynote.id, this.currentKeynote).subscribe({
        next: () => {
          this.loadKeynotes();
          this.closeModal();
        },
        error: (err) => {
          this.error = 'Failed to update keynote';
          console.error('Error updating keynote:', err);
        }
      });
    } else {
      // Create new keynote - remove id field
      const keynoteData = {
        nom: this.currentKeynote.nom,
        prenom: this.currentKeynote.prenom,
        email: this.currentKeynote.email,
        fonction: this.currentKeynote.fonction
      };
      this.keynoteService.createKeynote(keynoteData as Keynote).subscribe({
        next: () => {
          this.loadKeynotes();
          this.closeModal();
        },
        error: (err) => {
          this.error = 'Failed to create keynote';
          console.error('Error creating keynote:', err);
        }
      });
    }
  }

  deleteKeynote(id: number): void {
    if (confirm('Are you sure you want to delete this keynote?')) {
      this.keynoteService.deleteKeynote(id).subscribe({
        next: () => {
          this.loadKeynotes();
        },
        error: (err) => {
          this.error = 'Failed to delete keynote';
          console.error('Error deleting keynote:', err);
        }
      });
    }
  }

  private getEmptyKeynote(): Keynote {
    return {
      id: 0,
      nom: '',
      prenom: '',
      email: '',
      fonction: ''
    };
  }
}

