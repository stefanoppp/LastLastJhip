import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'serverApiApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'dispositivo',
    data: { pageTitle: 'serverApiApp.dispositivo.home.title' },
    loadChildren: () => import('./dispositivo/dispositivo.routes'),
  },
  {
    path: 'caracteristica',
    data: { pageTitle: 'serverApiApp.caracteristica.home.title' },
    loadChildren: () => import('./caracteristica/caracteristica.routes'),
  },
  {
    path: 'personalizacion',
    data: { pageTitle: 'serverApiApp.personalizacion.home.title' },
    loadChildren: () => import('./personalizacion/personalizacion.routes'),
  },
  {
    path: 'opcion',
    data: { pageTitle: 'serverApiApp.opcion.home.title' },
    loadChildren: () => import('./opcion/opcion.routes'),
  },
  {
    path: 'adicional',
    data: { pageTitle: 'serverApiApp.adicional.home.title' },
    loadChildren: () => import('./adicional/adicional.routes'),
  },
  {
    path: 'venta',
    data: { pageTitle: 'serverApiApp.venta.home.title' },
    loadChildren: () => import('./venta/venta.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
