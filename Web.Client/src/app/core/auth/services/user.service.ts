import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, throwError, of } from 'rxjs';

import { JwtService } from './jwt.service';
import {
  map,
  distinctUntilChanged,
  tap,
  shareReplay,
  catchError,
} from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { User } from '../user.model';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class UserService {
  private currentUserSubject = new BehaviorSubject<User | null>(null); // Holds the current user state
  public currentUser = this.currentUserSubject // observable for the current user, emits only on changes
    .asObservable()
    .pipe(distinctUntilChanged());

  // checks if a user is authenticated based on currentUser state
  public isAuthenticated = this.currentUser.pipe(map((user) => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly jwtService: JwtService,
    private readonly router: Router
  ) {}

  login(requestBody: {
    username: string;
    password: string;
  }): Observable<User> {
    const formData = new FormData();

    formData.append('requestBody', JSON.stringify(requestBody));

    return this.http
      .post<User>('api/auth/signin', formData) // sends login request
      .pipe(
        tap((user) => this.setAuth(user)) // set authentication with the response user
      );
  }

  checkUsername(username: string): Observable<{ value: boolean }> {
    const encodedUsername = encodeURIComponent(username); // handle special characters
    return this.http.get<{ value: boolean }>(
      `api/auth/isUserExist?username=${encodedUsername}`
    );
  }

  register(userData: {
    username: string;
    password: string;
    name: string;
    birthDate: string;
    bio: string;
    avatarFile: File | null;
  }): Observable<{ user: User }> {
    const formData = new FormData(); // formData for multipart request

    // prepares user data for submission
    const request = {
      username: userData.username,
      password: userData.password,
      name: userData.name,
      birthDate: userData.birthDate,
      bio: userData.bio,
    };
    formData.append('requestBody', JSON.stringify(request)); // append user details

    // append avatar file or default string
    if (userData.avatarFile) {
      formData.append('avatarImg', userData.avatarFile); // append avatarImg under 'avatarImg'
    } else {
      formData.append('avatarImg', ''); // use string if no file
    }

    return this.http.post<{ user: User }>('api/auth/signup', formData).pipe(
      tap((user) => this.setAuth(user)) // Pass the user directly to setAuth
    );
  }

  logout(): void {
    this.purgeAuth(); // clear authentication data (token and user info)
    void this.router.navigate(['/signin']); // navigate to the home page(without auth basically navigate to login)
  }

  getCurrentUser(): Observable<User | null> {
    const token = this.jwtService.getToken();

    if (token) {
      console.log('User is authenticated');
      return this.currentUser.pipe(shareReplay(1));
    } else {
      this.purgeAuth();
      return of(null).pipe(shareReplay(1));
    }
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>('/user', { user }).pipe(
      tap(({ user }) => this.currentUserSubject.next(user)) // update the current user state with the response user
    );
  }

  setAuth(user: any): void {
    console.log('Received user:', user); // Log the user object
    this.jwtService.saveToken(user.token);

    // update the currentUserSubject with the correct user information
    this.currentUserSubject.next({
      id: user.id,
      username: user.username,
      token: user.token,
      name: user.name,
      bio: user.bio,
      birthDate: user.birthDate,
      avatarUrl: user.avatarUrl,
      status: user.status || 'offline',
      lastSeen: user.lastSeen ? new Date(user.lastSeen) : new Date(),
    });
    console.log(this.currentUserSubject);
  }

  purgeAuth(): void {
    this.jwtService.destroyToken(); // remove the token from localStorage
    this.currentUserSubject.next(null); // set the current user to null, effectively logging out
  }

  //passing the data from the signUp to profile-setup
  private formData1: any = {}; //temporarily store the data passed from the sign-up form
  setFormData1(data: any) {
    this.formData1 = data; // save sign-up form data
  }

  getFormData1() {
    return this.formData1; // retrieve stored sign-up data
  }

  //to check the signUp form completion for route guarding purpose
  private signUpCompleteSubject = new BehaviorSubject<boolean>(false);
  public isSignUpComplete = this.signUpCompleteSubject.asObservable(); // observable for sign-up completion
  markSignUpComplete() {
    this.signUpCompleteSubject.next(true); // mark sign-up as complete
  }
}
