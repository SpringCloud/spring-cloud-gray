import { getDefaultNamespace } from '@/api/ns'
import { setDefaultNamespace2Cookie, removeDefaultNamespaceByCookie } from '@/utils/ns'

const state = {
  defaultNamespace: null
}

const mutations = {
  SET_DEFAULT_NAMESPACE: (state, ns) => {
    state.defaultNamespace = ns
  }
}

const actions = {
  setDefault({ commit }, ns) {
    commit('SET_DEFAULT_NAMESPACE', ns)
    setDefaultNamespace2Cookie(ns)
  },

  // get user info
  getDefault({ commit, state }) {
    return state.defaultNamespace
    // return new Promise((resolve) => {
    //   resolve(state.defaultNamespace)
    // })
  },

  initDefault({ commit }) {
    return new Promise((resolve, reject) => {
      getDefaultNamespace().then(response => {
        const { data } = response
        commit('SET_DEFAULT_NAMESPACE', data)
        setDefaultNamespace2Cookie(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  clear({ commit }) {
    commit('SET_DEFAULT_NAMESPACE', '')
    removeDefaultNamespaceByCookie()
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
