import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// custom validator for minimum date on datepicker text
export function minDateValidator(minDate: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const selectedDate = new Date(control.value);
    const min = new Date(minDate);
    if (selectedDate < min) {
      return { minDate: { valid: false, requiredMin: minDate } }; // validation failed + includes the required minimum date for reference
    }
    return null; // validation passed
  };
}

// custom validator for maximum date on datepicker text
export function maxDateValidator(maxDate: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const selectedDate = new Date(control.value);
    const max = new Date(maxDate);
    if (selectedDate > max) {
      return { maxDate: { valid: false, requiredMax: maxDate } }; // validation failed + includes the required maximum date for reference
    }
    return null; // validation passed
  };
}