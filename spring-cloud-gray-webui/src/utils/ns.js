import Cookies from 'js-cookie'
import store from '../store'

const defaultNS = 'Default-NS'

export function getDefaultNamespace() {
  // return store.dispatch('ns/getDefault')
  return store.state.ns.defaultNamespace || getDefaultNamespaceByCookie() || ''
  // return 'test'
}

export function setDefaultNamespace(ns) {
  // store.state.ns.defaultNamespace = ns
  // store.commit('SET_DEFAULT_NAMESPACE', ns)
  store.dispatch('ns/setDefault', ns)
}

export function setDefaultNamespace2Cookie(ns) {
  Cookies.set(defaultNS, ns)
}

export function removeDefaultNamespaceByCookie() {
  Cookies.remove(defaultNS)
}

export function getDefaultNamespaceByCookie() {
  Cookies.get(defaultNS)
}
