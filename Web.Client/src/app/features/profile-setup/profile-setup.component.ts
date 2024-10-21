import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
selector: 'app-profile-setup',
standalone: true,
templateUrl:'./profile-setup.component.html',
styleUrl: './profile-setup.component.css',
imports: [RouterLink]
})

export class ProfileSetupComponent {
    imgFiles: File[] = []; // To store the image file

    @ViewChild('addSingleImg') imgInputHelper!: ElementRef<HTMLInputElement>;
    @ViewChild('addImgLabel') imgInputHelperLabel!: ElementRef<HTMLLabelElement>;
    @ViewChild('imgContainer') imgContainer!: ElementRef<HTMLDivElement>;
  
    private loadedImgElement!: HTMLImageElement;
  
    constructor(private renderer: Renderer2) {}
  
    addImgHandler(event: Event): void {
      const inputElement = this.imgInputHelper.nativeElement;
      const file = inputElement.files?.[0];
      if (!file) return;
  
      // Generate img preview using FileReader
      const reader = new FileReader();
      reader.onload = () => {
        // If an image already exists, replace it
        if (this.loadedImgElement) {
          this.renderer.removeChild(this.imgContainer.nativeElement, this.loadedImgElement);
        }
  
        this.loadedImgElement = this.renderer.createElement('img');
        this.renderer.setAttribute(this.loadedImgElement, 'src', reader.result as string);
        this.renderer.setStyle(this.loadedImgElement, 'cursor', 'pointer');
  
        // Add click event listener to remove the image when clicked
        this.renderer.listen(this.loadedImgElement, 'click', () => this.removeImage());
  
        this.renderer.insertBefore(this.imgContainer.nativeElement, this.loadedImgElement, this.imgInputHelperLabel.nativeElement);
      };
      reader.readAsDataURL(file);
  
      // Store the image file
      this.imgFiles = [file]; // Keep only the current file
  
      // Disable input after first upload
      this.imgInputHelper.nativeElement.disabled = true;
      this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'display', 'none'); // Hide label
    }
  
    // Handler to remove the current image and allow uploading a new one
    removeImage(): void {
        if (this.loadedImgElement) {
          // Remove the image from the container
          this.renderer.removeChild(this.imgContainer.nativeElement, this.loadedImgElement);
          this.loadedImgElement = null!;
      
          // Re-enable the file input and show the label
          this.imgInputHelper.nativeElement.disabled = false;
      
          // Reset display and styles for the label
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'display', 'flex');
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'justify-content', 'center');
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'align-items', 'center');
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'width', '10vh');
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'height', '10vh');
          this.renderer.setStyle(this.imgInputHelperLabel.nativeElement, 'display', 'flex'); 

      
          this.imgInputHelper.nativeElement.value = ''; // Clear the file input
          this.imgFiles = []; // Clear the stored file
        }
      }
}
