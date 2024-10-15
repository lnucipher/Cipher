import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewInit,
  OnInit,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { passwordRegex } from '../../core/validators/regex';
import { CommonModule } from '@angular/common';

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

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.signInForm = new FormGroup({
      username: new FormControl('', {
        validators: [
          Validators.required,
          Validators.minLength(5),
        ],
        updateOn: 'change',
      }),
      password: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(passwordRegex),
        ],
        updateOn: 'change',
      }),
    });
  }

  onSubmit(): void {
    this.formSubmitted = true;
    if (this.signInForm.valid) {
      console.log('Form Submitted', this.signInForm.value);
    } else {
      console.log('Form is invalid');
    }
  }
}
