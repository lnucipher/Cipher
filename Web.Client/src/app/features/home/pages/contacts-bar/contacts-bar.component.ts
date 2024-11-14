import {
  Component,
  EventEmitter,
  OnInit,
  Output,
  ChangeDetectorRef,
} from '@angular/core';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, map } from 'rxjs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogModule,
  MatDialog,
} from '@angular/material/dialog';
import { User } from '../../../../core/auth/user.model';
import { UserService } from '../../../../core/auth/services/user.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import {
  PaginatedContacts,
  SearchedContacts,
} from '../../../../core/models/paginatedContacts.model';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-contacts-bar',
  templateUrl: './contacts-bar.component.html',
  styleUrl: './contacts-bar.component.css',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MatProgressSpinnerModule,
    MatDialogModule, // Add MatDialogModule here
    MatInputModule, // Add MatInputModule here
    MatButtonModule, // Add MatButtonModule here
  ],
})
export class ContactsBarComponent implements OnInit {
  contacts: User[] = [];
  pageSize: number = 9;
  page: number = 1;
  hasNextPage: boolean = true;
  hasPreviousPage: boolean = false;
  userId: string | undefined;
  searchQuery: string = '';

  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const storedUserId = localStorage.getItem('userId');

    if (storedUserId) {
      this.userId = storedUserId;
      console.log(this.userId);
      this.initializeContacts();
    } else {
      // Handle case where there is no userId in localStorage (e.g., user is not logged in)
      console.error('No userId found in localStorage');
    }
  }

  initializeContacts(): void {
    if (!this.userId) return;

    this.userService
      .getUserContacts(this.userId, this.pageSize, this.page)
      .subscribe((response: PaginatedContacts) => {
        if (response.items.length === 0) {
          console.log('Contact list is empty');
        } else {
          this.contacts = response.items;
          this.hasNextPage = response.hasNextPage;
          this.hasPreviousPage = response.hasPreviousPage;
        }
        this.cdr.detectChanges(); // Trigger change detection to update view
      });
    console.log(this.page);
  }

  loadContacts(): void {
    if (!this.hasNextPage || !this.userId) return;
    this.page += 1;

    this.userService
      .getUserContacts(this.userId, this.pageSize, this.page)
      .subscribe((response: PaginatedContacts) => {
        this.contacts = [...this.contacts, ...response.items]; // Concatenate new items
        this.hasNextPage = response.hasNextPage;
        this.hasPreviousPage = response.hasPreviousPage;
        this.cdr.detectChanges();
      });
    console.log(this.page);
  }

  loadPrevious(): void {
    if (this.page <= 1 || !this.hasPreviousPage || !this.userId) return;
    this.page -= 1;
    this.userService
      .getUserContacts(this.userId, this.pageSize, this.page - 1)
      .subscribe((response: PaginatedContacts) => {
        this.contacts = response.items;
        this.hasNextPage = response.hasNextPage;
        this.hasPreviousPage = response.hasPreviousPage;
        this.cdr.detectChanges();
      });
    console.log(this.page);
  }

  openChat(contact: User): void {
    this.userService.openChatWithUser(contact);
  }
}
