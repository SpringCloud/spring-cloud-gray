import store from '../store'

export function getDefaultNamespace() {
  // return store.dispatch('ns/getDefault')
  return store.state.ns.defaultNamespace
  // return 'test'
}

export function setDefaultNamespace(ns) {
  // store.state.ns.defaultNamespace = ns
  // store.commit('SET_DEFAULT_NAMESPACE', ns)
  store.dispatch('ns/setDefault', ns)
  console.log(getDefaultNamespace())
}
