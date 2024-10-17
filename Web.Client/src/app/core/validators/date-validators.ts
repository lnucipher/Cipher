import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Custom Validator for Minimum Date
export function minDateValidator(minDate: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const selectedDate = new Date(control.value);
    const min = new Date(minDate);
    if (selectedDate < min) {
      return { minDate: { valid: false, requiredMin: minDate } }; // Validation failed
    }
    return null; // Validation passed
  };
}

// Custom Validator for Maximum Date
export function maxDateValidator(maxDate: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const selectedDate = new Date(control.value);
    const max = new Date(maxDate);
    if (selectedDate > max) {
      return { maxDate: { valid: false, requiredMax: maxDate } }; // Validation failed
    }
    return null; // Validation passed
  };
}
