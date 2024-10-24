import {
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';

export function passwordMatchValidator(): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const password = formGroup.get('password')?.value; //retrieve values
    const confirmPassword = formGroup.get('confirmPassword')?.value; //retrieve values

    if (password && confirmPassword && password !== confirmPassword) {
      //checks if the values are truthy and compares them
      return { passwordMismatch: true }; //dont match
    }
    return null; //no errors
  };
}
