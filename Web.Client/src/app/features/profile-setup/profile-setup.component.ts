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
  imgFiles: File[] = []; // To store the image file
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

  //set maxdate for birthDate for today
  public get todayMaxDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  ngOnInit(): void {
    this.initializeForms(); // Added ngOnInit method
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

  //image handling
  addImgHandler(event: Event): void {
    const inputElement = this.imgInputHelper.nativeElement;
    const file = inputElement.files?.[0];
    if (!file) return;

    // generate img preview using FileReader
    const reader = new FileReader();
    reader.onload = () => {
      // if an image already exists, replace it
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

      // add click event listener to remove the image when clicked
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

    // store the image file
    this.imgFiles = [file]; // keep only the current file

    // disable input after first upload
    this.imgInputHelper.nativeElement.disabled = true;
    this.renderer.setStyle(
      this.imgInputHelperLabel.nativeElement,
      'display',
      'none'
    ); // hide label
  }

  // handler to remove the current image and allow uploading a new one
  removeImage(): void {
    if (this.loadedImgElement) {
      // remove the image from the container
      this.renderer.removeChild(
        this.imgContainer.nativeElement,
        this.loadedImgElement
      );
      this.loadedImgElement = null!;

      // re-enable the file input and show the label
      this.imgInputHelper.nativeElement.disabled = false;

      // reset display and styles for the label
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

      this.imgInputHelper.nativeElement.value = ''; // clear the file input
      this.imgFiles = []; // clear the stored file
    }
  }

  private transformDateToMMDDYYYY(date: string): string {
    const [year, month, day] = date.split('-');
    return `${month}-${day}-${year}`;
  }

  onSubmit(): void {
    this.formSubmitted = true; // mark the form as submitted

    // check if the form is valid
    if (this.profileForm.valid) {
      // retrieve the signup data from UserService (collected earlier)
      const signUpData = this.userService.getFormData1();

      // retrieve the profile form data
      const profileData = this.profileForm.value;

      // transform the birthDate to mm-dd-yyyy format
      const birthDate = profileData.birthDate;
      const formattedBirthDate = this.transformDateToMMDDYYYY(birthDate);
      // prepare the complete data by merging signup and profile data
      const completeData = {
        ...signUpData, // username and password from the earlier form step
        name: profileData.name,
        bio: profileData.bio,
        birthDate: formattedBirthDate,
        avatarFile: this.imgFiles.length
          ? this.imgFiles[0]
          : 'default string value', // ensure we're sending File
      };

      // call the register method to send the data to the server
      this.userService.register(completeData).subscribe({
        next: () => {
          console.log('User registered successfully');
          this.router.navigate(['/home']); //request is successful, navigate to the home page
        },
        error: (err) => {
          console.error('Registration failed', err); //there's an error,log it
        },
      });
    }
  }
}
