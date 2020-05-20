
const routes = [
  {
    path: '/',
    component: () => import('layouts/HomeLayout.vue'),
    children: [
      { path: '', component: () => import('pages/Index.vue') },
      { path: '/create', component: () => import('pages/Create.vue') },
      { path: '/join', component: () => import('pages/Join.vue') },
    ]
  },
  {
    path: '/game',
    component: () => import('layouts/GameLayout.vue'),
    children: [
      { path: '/game', component: () => import('pages/Game.vue') },
      { path: '/game/ladder', component: () => import('pages/Ladder.vue') },
      { path: '/game/inventory', component: () => import('pages/Inventory.vue') },

    ]
  },
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
