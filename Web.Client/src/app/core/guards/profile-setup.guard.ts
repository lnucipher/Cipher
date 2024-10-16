import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../auth/services/user.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ProfileSetupGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}

  canActivate() {
    return this.userService.isSignUpComplete.pipe(
      map((isComplete) => {
        if (isComplete) {
          return true;
        } else {
          this.router.navigate(['/sign-up']);
          return false;
        }
      })
    );
  }
}
