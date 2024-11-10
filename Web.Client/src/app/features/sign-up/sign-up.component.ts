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
import { passwordMatchValidator } from '../../core/validators/password-validator';
import { UserService } from '../../core/auth/services/user.service';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css',
  imports: [RouterLink, ReactiveFormsModule, CommonModule],
})
export class SignUpComponent implements OnInit {
  public signUpForm!: FormGroup; // holds the form object
  formSubmitted = false; // track if the form has been submitted
  isSubmitting = false; // prevent duplicate submissions
  usernameError: string | null = null;

  constructor(private router: Router, private userService: UserService) {} // inject UserService and Router

  ngOnInit(): void {
    this.initializeForm(); // initialize the form when the component loads
  }

  private initializeForm(): void {
    this.signUpForm = new FormGroup(
      {
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
        // confirm password form control with required
        confirmPassword: new FormControl('', {
          validators: [Validators.required],
          updateOn: 'change',
        }),
      },
      { validators: passwordMatchValidator() } //validates that the passwords match
    );
  }

  onSubmit(): void {
    this.formSubmitted = true; // mark the form as submitted
    this.usernameError = null; // resets the username error message

    if (this.signUpForm.valid && !this.isSubmitting) {
      // checks if the form is valid and if the form submission is not already in progress
      this.isSubmitting = true; // prevents duplicate form submissions by setting the flag to true

      const { username, password } = this.signUpForm.value; // extracts the 'username' and 'password' fields from the form values

      this.userService.checkUsername(username).subscribe({
        // calls the checkUsername method in the UserService to check if the username is available
        next: (response) => {
          if (response.value) {
            // Username is already taken (true means taken now)
            this.usernameError = 'Username is already taken.';
            this.isSubmitting = false; // Allow form submission again by resetting the flag
          } else if (!response.value){
            // Username is available
            // Pass signup data and navigate to profile setup
            console.log('real');
            this.userService.setFormData1({ username, password });
            this.userService.markSignUpComplete(); // Mark the sign-up as complete
            this.router.navigate(['/profileSetup']);
          }
        },
        error: (err) => {
          // if there is an error during the username check
          console.error('Error checking username', err); // logs the error to the console for debugging
          this.isSubmitting = false; // allow form submission again by resetting the flag
        },
      });
    }
  }
}
