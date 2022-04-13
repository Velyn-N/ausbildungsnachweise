import { boot } from 'quasar/wrappers'
import axios from 'axios'

// In Dev Mode we access Port :8080 where we start our Quarkus App on
const api = (process.env.DEV) ? axios.create({ baseURL: 'http://localhost:8080' }) : axios.create()

export default boot(({ app }) => {
  app.config.globalProperties.$axios = axios
  app.config.globalProperties.$api = api
})

export { api }
