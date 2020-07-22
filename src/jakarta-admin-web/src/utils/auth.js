import Cookies from 'js-cookie'

const TokenKey = 'jakarta_admin_token'
const UsernameKey = 'jakarta_username'

export function setUsername(username) {
  return Cookies.set(UsernameKey, username)
}

export function getUsername() {
  return Cookies.get(UsernameKey)
}

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
