import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // <-- Add FormsModule and ReactiveFormsModule
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'; // <-- Add HttpClientModule
import { RouterModule } from '@angular/router'; // <-- Add RouterModule 
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // <-- Required for Angular Material dialogs
import { MatDialogModule } from '@angular/material/dialog'; // <-- Import Angular Material Dialog Module

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegistrationComponent } from './components/auth/registration/registration.component';
import { ReservationTableComponent } from './components/reservation-table/reservation-table.component';
import { FilterComponent } from './components/reservation-table/filter/filter.component';
import { CrudButtonsComponent } from './components/reservation-table/crud-buttons/crud-buttons.component';
 
import { FeedbackComponent } from './components/feedback/feedback.component';
import { ReservationAdminComponent } from './components/reservation-admin/reservation-admin.component'; 
import { PendingReservationsComponent } from './components/pending-reservations/pending-reservations.component';
import { PengingTableComponent } from './components/pending-reservations/penging-table/penging-table.component';
import { ConfirmationDialogComponent } from './components/pending-reservations/confirmation-dialog/confirmation-dialog.component';
import { DeleteConfirmationDialogComponent } from './components/reservation-table/delete-confirmation-dialog/delete-confirmation-dialog.component';
import { EditReservationDialogComponent } from './components/reservation-table/edit-reservation-dialog/edit-reservation-dialog.component';
import { SafeUrlPipe } from './components/settings/SafeurlPipe'; // Adjust the path as needed

import { AuthInterceptor } from './services/auth/AuthInterceptor';
import { NavbarComponent } from './navbar/navbar.component';
import { ProfilePageComponent } from './components/profile/profile-page/profile-page.component';
import { BlockedAccountsComponent } from './components/profile/blocked-accounts/blocked-accounts.component';
import { AssignRolesComponent } from './components/profile/assign-roles/assign-roles.component';
import { ChangePasswordComponent } from './components/profile/change-password/change-password.component';
import { EditProfileComponent } from './components/profile/edit-profile/edit-profile.component';
import { SveTestComponent } from './components/profile/sve-test/sve-test.component';
import { FeedbackAdminComponent } from './components/feedback/feedback-admin/feedback-admin.component';
import { MessageSettingsComponent } from './components/settings/message-settings/message-settings.component';
import { PageSettingsComponent } from './components/settings/page-settings/page-settings.component';
import { ContactInfoComponent } from './components/settings/contact-info/contact-info.component';
import { ImageSettingsComponent } from './components/settings/image-settings/image-settings.component';
import { SettingsComponent } from './components/settings/settings.component';
 

@NgModule({
  declarations: [
    AppComponent,
    SafeUrlPipe ,
    LoginComponent,
    RegistrationComponent,
    ReservationTableComponent,
    FilterComponent,
    CrudButtonsComponent,
    SettingsComponent,
    FeedbackComponent,
    ReservationAdminComponent, 
    PendingReservationsComponent,
    PengingTableComponent,
    ConfirmationDialogComponent,
    DeleteConfirmationDialogComponent,
    EditReservationDialogComponent,
    NavbarComponent,
    ProfilePageComponent,
    BlockedAccountsComponent,
    AssignRolesComponent,
    ChangePasswordComponent,
    EditProfileComponent,
    SveTestComponent,
    FeedbackAdminComponent,
    MessageSettingsComponent,
    PageSettingsComponent,
    ContactInfoComponent,
    ImageSettingsComponent, 
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,                // <-- Add FormsModule
    ReactiveFormsModule,        // <-- Add ReactiveFormsModule
    HttpClientModule,           // <-- Add HttpClientModule for making HTTP requests
    RouterModule,               // <-- Add RouterModule for routing support
    BrowserAnimationsModule,    // <-- Required for Angular Material animations
    MatDialogModule,            // <-- Add MatDialogModule for Material Dialog functionality
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }  // <-- Provide AuthInterceptor
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
