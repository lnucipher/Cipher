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
  isSubmitting = false; // Prevent duplicate submissions
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
      { validators: passwordMatchValidator() }
    );
  }

  onSubmit(): void {
    this.formSubmitted = true;
    this.usernameError = null;

    if (this.signUpForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;

      const { username, password } = this.signUpForm.value;

      this.userService.checkUsername(username).subscribe({
        next: (response) => {
          if (response.available) {
            // Pass signup data and navigate ->
            this.userService.setFormData1({ username, password });
            this.router.navigate(['/profile-setup']);
          } else {
            // username is not available
            this.usernameError =
              'Username is already taken. Please choose another.';
            this.isSubmitting = false;
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
