import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../auth/services/user.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ProfileSetupGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}


  //protects the profile-setup route so users cannot fill their profile without password/username(signup stage)
  canActivate() {
    return this.userService.isSignUpComplete.pipe(
      map((isComplete) => {//returns true/false
        if (isComplete) {
          return true; //allow user to continue
        } else {
          this.router.navigate(['/signup']); //stay on signup page
          return false;
        }
      })
    );
  }
}
