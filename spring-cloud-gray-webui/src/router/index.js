import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/* Router Modules */

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  /* eslint-disable */
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: {title: 'Dashboard', icon: 'dashboard', affix: true}
      }
    ]
  },
  {
    path: '/',
    component: Layout
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: {title: 'Profile', icon: 'user', noCache: true}
      },
      {
        path: 'modifyPassword',
        component: () => import('@/views/profile/modifyPassowrd'),
        name: 'Modify Password',
        meta: {title: 'Modify Password', icon: 'user', noCache: true}
      }
    ]
  },
  {
    path: '/authority',
    name: 'Authority',
    component: Layout,
    hidden: true,
    alwaysShow: false, // will always show the root menu
    redirect: '/ns',
    meta: {
      title: 'Authority',
      icon: '',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'users',
        component: () => import('@/views/authority/users'),
        name: '用户权限',
        meta: {title: '用户权限', icon: '', noCache: true}
      }
    ]
  },
  {
    path: '/ns',
    name: 'Namespace',
    component: Layout,
    alwaysShow: true, // will always show the root menu
    redirect: '/ns',
    meta: {
      title: 'Namespace',
      icon: 'table',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/ns/list'),
        name: 'NS列表',
        meta: {title: 'NS列表', icon: 'table', noCache: true}
      }
    ]
  },
  {
    path: '/policy',
    name: '策略',
    component: Layout,
    alwaysShow: true, // will always show the root menu
    redirect: '/policy',
    meta: {
      title: '策略',
      icon: 'table',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'grayPolicys',
        component: () => import('@/views/gray-policy/list'),
        name: '灰度策略',
        meta: {title: '灰度策略', icon: 'table', noCache: true}
      },
      {
        path: 'grayPolicys/decision/:policyId',
        component: () => import('@/views/gray-decision/complex-table'),
        name: '灰度决策',
        meta: {title: '灰度决策', noCache: true},
        hidden: true
      },
      {
        path: 'handles',
        component: () => import('@/views/gray-policy/list'),
        name: '处理动作',
        meta: {title: '处理动作', icon: 'table', noCache: true}
      },
      {
        path: 'handleRules',
        component: () => import('@/views/gray-policy/list'),
        name: '处理规则',
        meta: {title: '处理规则', icon: 'table', noCache: true}
      }
    ]
  },
  {
    path: '/gray/service',
    name: '灰度服务',
    component: Layout,
    alwaysShow: true, // will always show the root menu
    redirect: '/gray/services',
    meta: {
      title: '灰度服务',
      icon: 'table',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/gray-service/list'),
        name: '服务列表',
        meta: {title: '服务列表', icon: 'table', noCache: true}
      },
      {
        path: 'owners',
        component: () => import('@/views/gray-service/owners'),
        name: '服务Owner',
        meta: {title: '服务Owner', icon: 'table', noCache: true}
      },
      {
        path: 'authority',
        hidden: true,
        component: () => import('@/views/gray-service/authority'),
        name: '服务权限',
        meta: {title: '服务权限', icon: 'table', noCache: true, activeMenu: '/gray/service'}
      },
      {
        path: 'discovery-instances:id(.+)',
        component: () => import('@/views/gray-instance/discovery-instances'),
        name: '服务实例',
        meta: {title: '服务实例', noCache: true, activeMenu: '/grayService'},
        hidden: true
      }
    ]
  },
  {
    path: '/gray/user',
    name: '用户管理',
    component: Layout,
    alwaysShow: true, // will always show the root menu
    redirect: '/gray/user',
    meta: {
      title: '用户管理',
      icon: 'table',
      roles: ['admin', 'editor'] // you can set roles in root nav
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/user/list'),
        name: '用户列表',
        meta: {title: '用户列表', icon: 'table', noCache: true}
      }
    ]
  },
  {
    path: '/gray/opreaterecord',
    name: '记录审计',
    component: Layout,
    alwaysShow: true, // will always show the root menu
    redirect: '/gray/opreaterecord',
    meta: {
      title: '记录审计',
      icon: 'table',
      roles: ['admin'] // you can set roles in root nav
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/operate-record/list'),
        name: '操作记录',
        meta: {title: '操作记录', icon: 'table', noCache: true}
      }
    ]
  },
  {
    path: '/gray/instance',
    component: Layout,
    redirect: '/grayInstance',
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/gray-instance/complex-table'),
        name: '灰度实例',
        meta: {title: '灰度实例', icon: 'tree-table', noCache: true, activeMenu: '/gray/service'}
      }
    ]
  },
  {
    path: '/gray/trackor',
    component: Layout,
    redirect: '/graytrackor',
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/gray-trackor/complex-table'),
        name: '灰度追踪',
        meta: {title: '灰度追踪', icon: 'table', noCache: true, activeMenu: '/gray/service'}
      }
    ]
  }
  /* eslint-disable */

]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
