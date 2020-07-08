import { login, logout, getInfo, updateInfo } from '@/api/user'
import { getToken, setToken, removeToken, setUsername } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    email: '',
    tel: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_TEL: (state, tel) => {
    state.tel = tel
  }
}

const actions = {
  // 用户登陆
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        // 根据返回的数据设置TOKEN、成功登陆的用户名
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        setUsername(username.trim())
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 用户修改个人信息
  updateInfo({ commit }, userInfo) {
    const { name, email, tel } = userInfo
    return new Promise((resolve, reject) => {
      updateInfo({
        username: name.trim(), email: email.trim(), telephone: tel.trim()
      }).then(response => {
        const { data } = response
        // 根据返回的数据重新设置TOKEN、用户名
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        setUsername(name.trim())
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 用户修改个人密码
  updatePass({ commit }, passInfo) {
    const { old, newPass, name } = passInfo
    return new Promise((resolve, reject) => {
      updateInfo({
        old_password: old, new_password: newPass, username: name
      }).then(() => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        // 根据返回的数据设置名字、电话、邮箱
        const { username, email, telephone } = response.data
        if (!username) {
          return reject('Verification failed, please Login again.')
        }
        commit('SET_NAME', username)
        commit('SET_EMAIL', email)
        commit('SET_TEL', telephone)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

