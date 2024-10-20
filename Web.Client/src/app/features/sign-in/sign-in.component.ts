import { Component, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { passwordRegex } from '../../core/validators/regex';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/auth/services/user.service';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.css',
  imports: [RouterLink, ReactiveFormsModule, CommonModule],
})
export class SignInComponent implements OnInit {
  public signInForm!: FormGroup; // holds the form object
  formSubmitted = false; // track if the form has been submitted
  isSubmitting = false; // prevent duplicate submissions

  constructor(private userService: UserService, private router: Router) {} // inject UserService and Router

  ngOnInit(): void {
    this.initializeForm(); // initialize the form when the component loads
  }

  private initializeForm(): void {
    this.signInForm = new FormGroup({
      // username form control with required and minLength validators
      username: new FormControl('', {
        validators: [Validators.required, Validators.minLength(5)],
        updateOn: 'change', // update the value and validation status when the user types
      }),
      // password form control with required and pattern validators
      password: new FormControl('', {
        validators: [Validators.required, Validators.pattern(passwordRegex)],
        updateOn: 'change', 
      }),
    });
  }

  onSubmit(): void {
    this.formSubmitted = true; // mark the form as submitted

    if (this.signInForm.valid && !this.isSubmitting) {// check if the form is valid and prevent duplicate submissions
      this.isSubmitting = true; // set to true to prevent multiple submissions
      const { username, password } = this.signInForm.value; // extract the username and password from the form

      this.userService.login({ username, password }).subscribe({
        next: ({ user }) => { // when login is successful, the user object is returned
          console.log('Login successful', user); // log the success
          this.router.navigate(['/homr']); // redirect to the home after login
        },
        error: (err) => {
          // handle errors during login
          console.error('Login failed', err); // log the error
          this.isSubmitting = false; // allow the user to try again
        },
      });
    } else {
      console.log('Form is invalid'); // if the form is invalid, show a log message
    }
  }
}
