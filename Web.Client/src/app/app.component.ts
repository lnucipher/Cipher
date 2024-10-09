import { Component } from '@angular/core';
import {RouterLink, RouterOutlet } from '@angular/router';
import { SignInComponent } from './features/sign-in/sign-in.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,SignInComponent,RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'Cipher';
}
