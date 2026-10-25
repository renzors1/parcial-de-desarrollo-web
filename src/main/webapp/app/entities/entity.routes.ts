import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'ejemplo01App.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'rol',
    title: 'ejemplo01App.rol.home.title',
    loadChildren: () => import('./rol/rol.routes'),
  },
  {
    path: 'usuario',
    title: 'ejemplo01App.usuario.home.title',
    loadChildren: () => import('./usuario/usuario.routes'),
  },
  {
    path: 'categoria',
    title: 'ejemplo01App.categoria.home.title',
    loadChildren: () => import('./categoria/categoria.routes'),
  },
  {
    path: 'editorial',
    title: 'ejemplo01App.editorial.home.title',
    loadChildren: () => import('./editorial/editorial.routes'),
  },
  {
    path: 'autor',
    title: 'ejemplo01App.autor.home.title',
    loadChildren: () => import('./autor/autor.routes'),
  },
  {
    path: 'libro',
    title: 'ejemplo01App.libro.home.title',
    loadChildren: () => import('./libro/libro.routes'),
  },
  {
    path: 'venta',
    title: 'ejemplo01App.venta.home.title',
    loadChildren: () => import('./venta/venta.routes'),
  },
  {
    path: 'detalle-venta',
    title: 'ejemplo01App.detalleVenta.home.title',
    loadChildren: () => import('./detalle-venta/detalle-venta.routes'),
  },
  {
    path: 'licencia-digital',
    title: 'ejemplo01App.licenciaDigital.home.title',
    loadChildren: () => import('./licencia-digital/licencia-digital.routes'),
  },
  {
    path: 'promocion',
    title: 'ejemplo01App.promocion.home.title',
    loadChildren: () => import('./promocion/promocion.routes'),
  },
  {
    path: 'calificacion-usuario',
    title: 'ejemplo01App.calificacionUsuario.home.title',
    loadChildren: () => import('./calificacion-usuario/calificacion-usuario.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
