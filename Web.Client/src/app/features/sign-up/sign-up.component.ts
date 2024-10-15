import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewInit,
  OnInit,
} from '@angular/core';
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
        }),
      },
      { validators: passwordMatchValidator() }
    );
  }

  constructor(private router: Router) {}

  onSubmit(): void {
    this.formSubmitted = true;
    if (this.signUpForm.valid) {
      console.log('Form Submitted', this.signUpForm.value);
      this.router.navigate(['/profile-setup']);
    } else {
      console.log('Form is invalid');
    }
  }
}
