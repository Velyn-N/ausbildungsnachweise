import {defineStore} from "pinia";
import {useQuasar} from "quasar";
import {api} from "boot/axios";

const $q = useQuasar()

const useActivityStore = defineStore({
  id: 'activities',
  state: () => ({
    myActivities: []
  }),
  actions: {
    loadMyActivities(callback) {
      api.get("/rest/activities/myactivities")
        .then((response) => {
          if (response.data !== undefined && response.data !== null) {
            this.myActivities = response.data
          } else {
            this.myActivities = []
          }
          if (callback !== undefined) callback()
        })
        .catch((err) => {
          $q.notify("Es gab einen Fehler beim laden der Aktivitäten")
          console.error(err)
        })
    },
    addActivity(activity, callback) {
      api.post("/rest/activities/add", activity)
        .then(() => {
          this.loadMyActivities(callback)
        })
        .catch((err) => {
          $q.notify("Fehler beim Speichern der neuen Aktivität!")
          console.error(err)
        })
    },
    saveActivity(activity, callback) {
      api.post("/rest/activities/change", activity)
        .then(() => {
          this.loadMyActivities(callback)
        })
        .catch((err) => {
          $q.notify("Fehler beim Bearbeiten der Aktivität.")
          console.error(err)
        })
    },
    deleteActivity(activityId, callback) {
      api.post("/rest/activities/delete", activityId)
        .then(() => {
          this.loadMyActivities(callback)
        })
        .catch((err) => {
          $q.notify("Fehler beim Löschen der Aktivität.")
          console.error(err)
        })
    }
  }
})

export {useActivityStore}
