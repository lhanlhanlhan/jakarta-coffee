const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  name: state => state.user.name,
  tel: state => state.user.tel,
  email: state => state.user.email
}
export default getters
