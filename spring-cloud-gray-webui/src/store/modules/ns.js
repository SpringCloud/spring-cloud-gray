import { getDefaultNamespace } from '@/api/ns'

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
        console.log('AAAAA' + data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
