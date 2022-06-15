<template>
  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-4'" class="q-ma-sm activityCard"
          v-if="apiUserStore.apiUser.isAzubi">
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
              <span style="display: none">{{ currentSum = sumActivityDurations(activityDisplayList[day]) }}</span>
              <span class="text-center col-6"
                    :class="(currentSum < 8) ? 'text-red' : (currentSum > 8) ? 'text-yellow' : 'text-green'">
                {{ currentSum }}h ges.
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
                <td colspan="3">
                  <hr>
                </td>
              </tr>
            </thead>
            <tbody>
              <tr v-for="activity in activityDisplayList[day]">
                <td>{{ activity.activity }}</td>
                <td>{{ activity.durationHours }}h</td>
                <td>
                  <q-icon name="fas fa-pencil" class="clickable" @click="editingActivity = activity; editDialogOpen = true" />
                  <q-icon name="fas fa-trash" class="clickable q-ml-sm" @click="deletionActivity = activity; deleteConfirmOpen = true" />
                </td>
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



  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-3'" class="q-ma-sm" v-if="apiUserStore.apiUser.isAzubi">
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
              <q-input input-class="text-center" v-model.number="newActivity.durationHours" label="Dauer (in Stunden)" />
            </td>
          </tr>
          <tr>
            <td class="text-left">
              <q-btn @click="activityStore.addActivity(newActivity, updateActivities)">
                <q-icon name="fas fa-plus"></q-icon>
              </q-btn>
            </td>
          </tr>
        </tbody>
      </table>
    </q-card-section>
  </q-card>



  <q-dialog v-model="editDialogOpen">
    <q-card style="min-width: 50vw">
      <form onsubmit="return false" v-if="editingActivity !== null" >
        <q-card-section class="text-center">
          <span style="font-size: 150%">Aktivität vom <b>{{editingActivity.date}}</b> bearbeiten</span>
        </q-card-section>
        <q-card-section class="full-width column wrap">
          <q-input v-model="editingActivity.activity" label="Aktivität" type="textarea" class="q-mb-sm" />
          <q-input input-class="text-center q-ma-sm" v-model.number="editingActivity.durationHours" type="number" label="Dauer (in Stunden)" class="q-mb-md" />
          <q-btn type="submit" @click="activityStore.saveActivity(editingActivity, updateActivities)" class="full-width justify-center" v-close-popup>
            <q-icon name="fas fa-save"></q-icon>
          </q-btn>
        </q-card-section>
      </form>
    </q-card>
  </q-dialog>

  <q-dialog v-model="deleteConfirmOpen">
    <q-card style="min-width: 25vw" class="text-center">
      <q-card-section>
        Soll diese Aktivität wirklich gelöscht werden?
      </q-card-section>
      <q-card-section>
        <q-btn @click="activityStore.deleteActivity(deletionActivity.id, updateActivities)" v-close-popup>
          Aktivität löschen
        </q-btn>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup>
import {useApiUser} from "stores/apiUserStore";
import {useActivityStore} from "stores/activityStore";
import {useQuasar} from "quasar";
import {ref} from "vue";

const apiUserStore = useApiUser()
const activityStore = useActivityStore()
const $q = useQuasar()

const loading = ref(true)
const activityDisplayList = ref(null)
const newActivity = ref(getPlaceholderActivity())
/**
 * Variable (re-)assigned during v-for in Template
 */
const currentSum = ref(0)

const editingActivity = ref(null)
const editDialogOpen = ref(false)

const deletionActivity = ref(null)
const deleteConfirmOpen = ref(false)

activityStore.loadMyActivities(updateActivities)

function updateActivities() {
  activityDisplayList.value = mapActivitiesToDay(activityStore.myActivities)
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
</script>

<style>
.activityCard {
  max-height: 80vh;
  overflow-y: scroll;
}

.clickable {
  cursor: pointer;
}
</style>
