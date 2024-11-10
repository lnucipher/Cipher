import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, map } from 'rxjs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

interface Contact {
  name: string;
  message: string;
}

@Component({
  selector: 'app-contacts-bar',
  templateUrl: './contacts-bar.component.html',
  styleUrl: './contacts-bar.component.css',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
})
export class ContactsBarComponent implements OnInit {
  searchQuery = '';
  contacts: Contact[] = [];

  ngOnInit() {
  }

  onSearch() {
  }

}
