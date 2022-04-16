<template>
  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-5'" class="q-ma-sm activityCard" v-if="apiUserStore.apiUser.isAzubi">
    <q-card-section>
      <h6>Meine Aktivitäten</h6>
    </q-card-section>
    <q-card-section v-if="!loading && activityDisplayList !== null">
      <q-list v-for="day in Object.keys(activityDisplayList)">
        <q-expansion-item group="daylist">
          <template v-slot:header>
            <span class="full-width row">
              <span class="text-left col-6">
                {{ day }}
              </span>
              <span class="text-center col-6">
                {{ sumActivityDurations(activityDisplayList[day]) }}h ges.
              </span>
            </span>
          </template>
          <table class="full-width text-center">
            <thead class="bi-border-bottom">
            <tr>
              <td>Aktivität</td>
              <td>Dauer</td>
            </tr>
            <tr>
              <td colspan="2">
                <hr>
              </td>
            </tr>
            </thead>
            <tbody v-for="activity in activityDisplayList[day]">
              <tr>
                <td>{{ activity.activity }}</td>
                <td>{{ activity.durationHours }}</td>
              </tr>
            </tbody>
          </table>
        </q-expansion-item>
      </q-list>
    </q-card-section>
    <q-card-section v-else>
      <q-icon name="fas fa-spinner fa-spin" />
    </q-card-section>
  </q-card>



  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-5'" class="q-ma-sm" v-if="apiUserStore.apiUser.isAzubi">
    <q-card-section>
      <h6>Neue Aktivität anlegen</h6>
    </q-card-section>
    <q-card-section>
      <table class="full-width">
        <tbody>
          <tr>
            <td rowspan="3">
              <q-date v-model="newActivity.date" minimal mask="DD.MM.YYYY" />
            </td>
            <td>
              <q-input v-model="newActivity.activity" label="Aktivität" type="textarea" />
            </td>
          </tr>
          <tr>
            <td>
              <q-input input-class="text-center" v-model.number="newActivity.durationHours" type="number" label="Dauer (in Stunden)" />
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <q-btn @click="addActivity()">
                <q-icon name="fas fa-plus"></q-icon>
              </q-btn>
            </td>
          </tr>
        </tbody>
      </table>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {useApiUser} from "stores/apiUserStore";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import {ref} from "vue";

const apiUserStore = useApiUser()
const $q = useQuasar()

const loading = ref(true)
const myActivities = ref(null)
const activityDisplayList = ref(null)

const newActivity = ref(getPlaceholderActivity())

loadMyActivities()

function loadMyActivities() {
  api.get("/rest/activities/myactivities")
  .then((response) => {
    if (response.data !== undefined && response.data !== null) {
      myActivities.value = response.data
      updateActivities()
    } else {
      myActivities.value = []
    }
  })
  .catch((err) => {
    $q.notify("Es gab einen Fehler beim laden der Aktivitäten")
    console.log(err)
  })
}

function updateActivities() {
  activityDisplayList.value = mapActivitiesToDay(myActivities.value)
  loading.value = false
}

function getPlaceholderActivity() {
  return {date: null, activity: null, durationHours: null}
}

function mapActivitiesToDay(activities) {
  let dailys = []
  for (let index in activities) {
    let activity = activities[index]
    if (dailys[activity.date] === undefined) {
      dailys[activity.date] = []
    }
    dailys[activity.date].push(activity)
  }
  return dailys
}

function sumActivityDurations(activities) {
  let total = 0
  activities.forEach((act) => total += act.durationHours)
  return total
}



function addActivity() {
  loading.value = true
  myActivities.value.push(newActivity.value)
  console.log(myActivities)

  api.post("/rest/activities/add", newActivity.value)
  .then(() => {
    loadMyActivities()
    updateActivities()
  })
  .catch((err) => {
    $q.notify("Fehler beim Speichern der Aktivitäten!")
    console.log(err)
  })

  newActivity.value = getPlaceholderActivity()
}
</script>

<style>
.activityCard {
  max-height: 80vh;
  overflow-y: scroll;
}
</style>
