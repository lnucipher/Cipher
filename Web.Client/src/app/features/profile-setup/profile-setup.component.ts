import {
  Component,
  ElementRef,
  Renderer2,
  ViewChild,
  OnInit,
} from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/auth/services/user.service';
import {
  minDateValidator,
  maxDateValidator,
} from '../../core/validators/date-validators';

@Component({
  selector: 'app-profile-setup',
  standalone: true,
  templateUrl: './profile-setup.component.html',
  styleUrl: './profile-setup.component.css',
  imports: [RouterLink, ReactiveFormsModule, CommonModule],
})
export class ProfileSetupComponent implements OnInit {
  public profileForm!: FormGroup;
  imgFiles: File[] = [];
  formSubmitted = false;

  @ViewChild('addSingleImg') imgInputHelper!: ElementRef<HTMLInputElement>;
  @ViewChild('addImgLabel') imgInputHelperLabel!: ElementRef<HTMLLabelElement>;
  @ViewChild('imgContainer') imgContainer!: ElementRef<HTMLDivElement>;

  private loadedImgElement!: HTMLImageElement;

  constructor(
    private renderer: Renderer2,
    private userService: UserService,
    private router: Router
  ) {}

  public get todayMaxDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  ngOnInit(): void {
    this.initializeForms();
  }

  private initializeForms(): void {
    this.profileForm = new FormGroup({
      image: new FormControl(null),

      name: new FormControl('', {
        validators: [Validators.required],
      }),

      bio: new FormControl(''),

      birthDate: new FormControl('', {
        validators: [
          minDateValidator('1900-01-01'),
          maxDateValidator(this.todayMaxDate),
        ],
      }),
    });
  }


  addImgHandler(event: Event): void {
    const inputElement = this.imgInputHelper.nativeElement;
    const file = inputElement.files?.[0];
    if (!file) return;


    const reader = new FileReader();
    reader.onload = () => {
      if (this.loadedImgElement) {
        this.renderer.removeChild(
          this.imgContainer.nativeElement,
          this.loadedImgElement
        );
      }

      this.loadedImgElement = this.renderer.createElement('img');
      this.renderer.setAttribute(
        this.loadedImgElement,
        'src',
        reader.result as string
      );
      this.renderer.setStyle(this.loadedImgElement, 'cursor', 'pointer');

      this.renderer.listen(this.loadedImgElement, 'click', () =>
        this.removeImage()
      );

      this.renderer.insertBefore(
        this.imgContainer.nativeElement,
        this.loadedImgElement,
        this.imgInputHelperLabel.nativeElement
      );
    };
    reader.readAsDataURL(file);


    this.imgFiles = [file];


    this.imgInputHelper.nativeElement.disabled = true;
    this.renderer.setStyle(
      this.imgInputHelperLabel.nativeElement,
      'display',
      'none'
    );
  }


  removeImage(): void {
    if (this.loadedImgElement) {
      this.renderer.removeChild(
        this.imgContainer.nativeElement,
        this.loadedImgElement
      );
      this.loadedImgElement = null!;

      this.imgInputHelper.nativeElement.disabled = false;

      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'display',
        'flex'
      );
      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'justify-content',
        'center'
      );
      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'align-items',
        'center'
      );
      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'width',
        '10vh'
      );
      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'height',
        '10vh'
      );
      this.renderer.setStyle(
        this.imgInputHelperLabel.nativeElement,
        'display',
        'flex'
      );

      this.imgInputHelper.nativeElement.value = '';
      this.imgFiles = [];
    }
  }

  private transformDateToMMDDYYYY(date: string): string {
    const [year, month, day] = date.split('-');
    return `${month}-${day}-${year}`;
  }

  onSubmit(): void {
    this.formSubmitted = true;

    if (this.profileForm.valid) {
      const signUpData = this.userService.getFormData1();

      const profileData = this.profileForm.value;

      const birthDate = profileData.birthDate;
      const formattedBirthDate = birthDate ? this.transformDateToMMDDYYYY(birthDate) : '';
      const completeData = {
        ...signUpData,
        name: profileData.name,
        bio: profileData.bio,
        birthDate: formattedBirthDate,
        avatarFile: this.imgFiles.length
          ? this.imgFiles[0]
          : '',
      };

      this.userService.register(completeData).subscribe({
        next: ({ user }) => {
          console.log('User registered successfully');

          this.router.navigate(['/home']);
        },
        error: (err) => {
          console.error('Registration failed', err);
        },
      });
    }
  }
}
