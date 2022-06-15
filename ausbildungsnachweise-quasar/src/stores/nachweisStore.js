import {defineStore} from "pinia";
import {useQuasar} from "quasar";
import {api} from "boot/axios";

const $q = useQuasar()

const useNachweisStore = defineStore({
  id: 'nachweise',
  state: () => ({
    myNachweise: []
  }),
  actions: {
    loadMyNachweise(callback) {
      api.get("/rest/nachweise/mynachweise")
        .then((response) => {
          if (response.data !== undefined && response.data !== null) {
            this.myNachweise = response.data
          } else {
            this.myNachweise = []
          }
        })
        .catch((err) => {
          $q.notify("Es gab einen Fehler beim Laden der Nachweise")
          console.error(err)
        })
    },
    createNachweis(nachweis, callback) {
      api.post("/rest/nachweise/create", nachweis)
        .then(() => {
          this.loadMyNachweise(callback)
        })
        .catch((err) => {
          $q.notify("Fehler beim Erstellen eines Nachweises")
          console.error(err)
        })
    },
    saveNachweis(nachweis, callback) {
      api.post("/rest/nachweise/change", nachweis)
        .then(() => {
          this.loadMyNachweise(callback)
        })
        .catch((err) => {
          $q.notify("Fehler beim Speichern eines Nachweises")
          console.error(err)
        })
    }
  }
})

export {useNachweisStore}
