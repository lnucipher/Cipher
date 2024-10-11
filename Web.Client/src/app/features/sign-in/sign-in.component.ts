import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { changePlaceholderOnFocus } from '../../core/layout/input-hints.util';
@Component({
selector: 'app-sign-in',
standalone: true,
templateUrl:'./sign-in.component.html',
styleUrl: './sign-in.component.css',
imports: [RouterLink]
})

export class SignInComponent implements AfterViewInit {
    @ViewChild('usernameInput') usernameInputElement!: ElementRef;
    @ViewChild('passwordInput') passwordInputElement!: ElementRef;
  
    ngAfterViewInit() {
      // Change placeholder for the username field
      changePlaceholderOnFocus(this.usernameInputElement.nativeElement, 'your-username', 'Username');
  
      // Change placeholder for the password field
      changePlaceholderOnFocus(this.passwordInputElement.nativeElement, '8 characters min', 'Password');
    }
  }