import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, throwError } from 'rxjs';

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
    private readonly http: HttpClient, // HTTP client for making requests
    private readonly jwtService: JwtService, // service for managing JWT
    private readonly router: Router // router for navigation
  ) {}

  //login method
  login(credentials: {
    username: string;
    password: string;
  }): Observable<{ user: User }> {
    return this.http
      .post<{ user: User }>('api/auth/signin', { user: credentials }) // sends login request
      .pipe(
        tap(({ user }) => this.setAuth(user)) // set authentication with the response user
      );
  }

  // checks if a username is unique
  checkUsername(username: string): Observable<{ available: boolean }> {
    const encodedUsername = encodeURIComponent(username); // Handle special characters
    return this.http.get<{ available: boolean }>(
      `api/auth/isUserExist?username=${encodedUsername}`
    );
  }

  // registers a new user
  register(userData: {
    username: string;
    password: string;
    name: string;
    birthDate: string;
    bio: string;
    avatarFile: File | null; // optional avatar file
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
      formData.append('avatarImg', 'default-avatar'); // use default string if no file
    }

    return this.http.post<{ user: User }>('api/auth/signup', formData).pipe(
      tap(({ user }) => {
        this.setAuth(user); // save the user data including the token
      })
    );
  }

  logout(): void {
    this.purgeAuth(); // clear authentication data (token and user info)
    void this.router.navigate(['/']); // navigate to the home page(without auth basically navigate to login)
  }

  getCurrentUser(): Observable<{ user: User }> {
    return this.http.get<{ user: User }>('/user').pipe(
      tap({
        next: ({ user }) => this.setAuth(user), // set the authentication state with the current user
        error: () => this.purgeAuth(), // clear authentication if error occurs
      }),
      shareReplay(1)
    );
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>('/user', { user }).pipe(
      tap(({ user }) => {
        this.currentUserSubject.next(user); // update the current user state with the response user
      })
    );
  }

  setAuth(user: User): void {
    this.jwtService.saveToken(user.token); // save the token
    this.currentUserSubject.next({
      id: user.id,
      username: user.username,
      passwordHash: user.passwordHash || '', // ??maybe fix later
      token: user.token,
      name: user.name,
      bio: user.bio,
      avatarUrl: user.avatarUrl,
      status: user.status || 'offline', // offline as default status
      lastSeen: new Date(), // default value for lastSeen??
    });
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
