const getters = {
  token: state => state.user.token,
  name: state => state.user.name,
  tel: state => state.user.tel,
  email: state => state.user.email,
  allInfo: state => state.user
}
export default getters
