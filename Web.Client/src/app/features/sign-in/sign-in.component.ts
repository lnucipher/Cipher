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
  public signInForm!: FormGroup;
  formSubmitted = false;
  isSubmitting = false;
  loginFailed = false;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.signInForm = new FormGroup({
      username: new FormControl('', {
        validators: [Validators.required, Validators.minLength(5)],
        updateOn: 'change',
      }),
      password: new FormControl('', {
        validators: [Validators.required],
        updateOn: 'change',
      }),
    });
  }

  onSubmit(): void {
    this.formSubmitted = true;
    this.loginFailed = false;

    if (this.signInForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      const { username, password } = this.signInForm.value;

      this.userService.login({ username, password }).subscribe({
        next: (user) => {
          this.router.navigate(['/home']); // redirect to the home after login
        },
        error: (err) => {
          // handle errors during login
          console.error('Login failed', err);
          this.isSubmitting = false;
          this.loginFailed = true;
        },
      });
    } else {
      console.log('Form is invalid');
    }
  }
}
