import Cookies from 'js-cookie'

const TokenKey = 'jakarta_client_token'
const UsernameKey = 'jakarta_client_username'

export function setUsername (username) {
  return Cookies.set(UsernameKey, username)
}

export function getUsername () {
  return Cookies.get(UsernameKey)
}

export function getToken () {
  return Cookies.get(TokenKey)
}

export function setToken (token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken () {
  return Cookies.remove(TokenKey)
}
