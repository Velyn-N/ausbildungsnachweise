
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: 'myactivities', component: () => import('pages/MyActivities.vue') },
      { path: 'createactivity', component: () => import('pages/CreateActivity.vue') },
      { path: 'nachweise', component: () => import('pages/Nachweise.vue') }
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
