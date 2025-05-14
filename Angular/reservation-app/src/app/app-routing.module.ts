import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegistrationComponent } from './components/auth/registration/registration.component';
import { ReservationTableComponent } from './components/reservation-table/reservation-table.component';
import { ReservationAdminComponent } from './components/reservation-admin/reservation-admin.component'; 
import { PendingReservationsComponent } from './components/pending-reservations/pending-reservations.component';
import { SettingsComponent } from './components/settings/settings.component';
import { FeedbackComponent } from './components/feedback/feedback.component'; 
import { ProfilePageComponent } from './components/profile/profile-page/profile-page.component'; 
import { BlockedAccountsComponent } from './components/profile/blocked-accounts/blocked-accounts.component';
import { AssignRolesComponent } from './components/profile/assign-roles/assign-roles.component';
import { ChangePasswordComponent } from './components/profile/change-password/change-password.component';
import { EditProfileComponent } from './components/profile/edit-profile/edit-profile.component'; 
import { FeedbackAdminComponent } from './components/feedback/feedback-admin/feedback-admin.component';
import { ReservationUserComponent } from './components/reservation-user/reservation-user.component';

const routes: Routes = [
  { path: ':companyName', component: ReservationUserComponent },
  { path: '', component: LoginComponent, pathMatch: 'full' },
  { path: 'register', component: RegistrationComponent },
  { path: 'reservations', component: ReservationTableComponent },
  { path: 'add-reservation', component: ReservationAdminComponent }, 
  { path: 'pending', component: PendingReservationsComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'feedback', component: FeedbackComponent },
  { path: 'feedback/admin', component: FeedbackAdminComponent },
  { path: 'profile', component: ProfilePageComponent },
  { path: 'blocked-accounts', component: BlockedAccountsComponent },
  { path: 'assign-roles', component: AssignRolesComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'edit-profile', component: EditProfileComponent },
  { path: 'sve', component: ProfilePageComponent },
  
 // { path: ':companyName', loadChildren: () => import('./components/reservation-user/reservation-user.component').then(m => m.ReservationUserComponent) }
 

];

@NgModule({
  //imports: [RouterModule.forRoot(routes, { useHash: true })], ovo je da se koristi #
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
