import {defineStore} from "pinia"
import {ApiUser} from "components/ApiUser";
import {LocalStorage} from "quasar";
import {api} from "boot/axios";

const STORAGE_KEY = "jwt";

const useApiUser = defineStore({
  id: 'apiUser',
  state: () => ({
    apiUser: new ApiUser()
  }),
  actions: {
    reloadApiUser(jwt) {
      if (jwt !== undefined && jwt !== null) {
        LocalStorage.set(STORAGE_KEY, jwt)
        api.defaults.headers.common['Authentication'] = jwt
      }
      this.fetchUser()
    },
    logout() {
      api.defaults.headers.common['Authentication'] = undefined
      LocalStorage.set(STORAGE_KEY, undefined)
      this.fetchUser()
    },
    fetchUser() {
      console.log("Fetching User...")
      api.get("/rest/user/self")
        .then((response) => {
          if (response.data !== undefined && response.data !== null) {
            Object.assign(this.apiUser, response.data)
          } else {
            Object.assign(this.apiUser, new ApiUser())
          }
        })
        .catch((err) => {
          console.log(err)
        })
    }
  }
})

let apiUserStorage = useApiUser()
if (LocalStorage.has(STORAGE_KEY)) {
  try {
    let jwt = LocalStorage.getItem(STORAGE_KEY)
    apiUserStorage.reloadApiUser(jwt)
  } catch (e) {
    apiUserStorage.reloadApiUser()
  }
} else {
  apiUserStorage.reloadApiUser()
}

export {useApiUser};
