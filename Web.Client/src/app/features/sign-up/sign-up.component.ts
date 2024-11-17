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
  public signUpForm!: FormGroup;
  formSubmitted = false;
  isSubmitting = false;
  usernameError: string | null = null;

  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.signUpForm = new FormGroup(
      {
        username: new FormControl('', {
          validators: [Validators.required, Validators.minLength(5)],
          updateOn: 'change',
        }),
        password: new FormControl('', {
          validators: [Validators.required, Validators.pattern(passwordRegex)],
          updateOn: 'change',
        }),
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
      this.isSubmitting = true;

      const { username, password } = this.signUpForm.value;

      this.userService.checkUsername(username).subscribe({
        next: (response) => {
          if (response.value) {
            this.usernameError = 'Username is already taken.';
            this.isSubmitting = false;
          } else if (!response.value) {
            // Pass signup data and navigate to profile setup
            this.userService.setFormData1({ username, password });
            this.userService.markSignUpComplete(); // Mark the sign-up as complete
            this.router.navigate(['/profileSetup']);
          }
        },
        error: (err) => {
          console.error('Error checking username', err);
          this.isSubmitting = false;
        },
      });
    }
  }
}
