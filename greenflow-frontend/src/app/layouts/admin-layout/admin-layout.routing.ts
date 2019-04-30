import { Routes } from '@angular/router';

import { HomeComponent } from '../../home/home.component';
import { UserComponent } from '../../user/user.component';
import { HistoryComponent } from '../../history/history.component';
import { TypographyComponent } from '../../typography/typography.component';
import { CategoryComponent } from '../../category/category.component';
import { IconsComponent } from '../../icons/icons.component';
import { MapsComponent } from '../../maps/maps.component';
import { NotificationsComponent } from '../../notifications/notifications.component';
import { UpgradeComponent } from '../../upgrade/upgrade.component';
import { TransactionComponent } from 'src/app/transaction/transaction.component';
import { BudgetComponent } from '../../budget/budget.component'
import { EmaileventsComponent } from 'src/app/emailevents/emailevents.component';

export const AdminLayoutRoutes: Routes = [
    { path: 'dashboard',      component: HomeComponent },
    { path: 'user',           component: UserComponent },
    { path: 'transaction',    component: TransactionComponent },
    { path: 'category',       component: CategoryComponent },
    { path: 'history',        component: HistoryComponent },
    { path: 'budget',         component: BudgetComponent },
    { path: 'emailevents',    component: EmaileventsComponent},
    { path: 'typography',     component: TypographyComponent },
    { path: 'icons',          component: IconsComponent },
    { path: 'maps',           component: MapsComponent },
    { path: 'notifications',  component: NotificationsComponent },
    { path: 'upgrade',        component: UpgradeComponent }
];
