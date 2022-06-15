<template>
  <q-card>
    <q-card-section class="text-center">
      <h5>Einen neuen Nachweis erstellen</h5>
    </q-card-section>
    <q-card-section>
      <form class="fit row wrap justify-center items-center content-center text-center">
        <div :class="($q.platform.is.mobile) ? 'col-12' : 'col-3'">
          <span class="subheadline">Dauer</span>
          <br>
          <q-date v-model="dateRange" range minimal mask="DD.MM.YYYY"
                  @update:model-value="activityStore.loadMyActivities(loadActivitiesInDateRange)" />
        </div>
        <div :class="($q.platform.is.mobile) ? 'col-12' : 'col-3'">
          <table class="full-width text-center" v-if="selectedActivities.length > 0">
            <thead class="bi-border-bottom" style="text-decoration: underline">
              <tr>
                <td></td>
                <td><b>Tag</b></td>
                <td><b>Aktivität</b></td>
                <td><b>Dauer</b></td>
              </tr>
            </thead>
            <tbody v-for="(activity, index) in selectedActivities">
              <tr v-if="currDate !== activity.date">
                <td colspan="4">
                  <span class="q-pb-xl invisible">{{ currDate = activity.date }}</span>
                </td>
              </tr>
              <tr>
                <td>
                  <q-icon name="fas fa-times" class="clickable text-red"
                          title="Diese Aktivität nicht berücksichtigen"
                          @click="selectedActivities.splice(index, 1)"/>
                </td>
                <td>{{ activity.date }}</td>
                <td>{{ activity.activity }}</td>
                <td>{{ activity.durationHours }}h</td>
              </tr>
              <tr v-if="index === selectedActivities.length">
                <td colspan="4">
                  <hr>
                </td>
              </tr>
            </tbody>
          </table>
          <span v-else>
            Wähle eine Dauer aus, um die Aktivitäten des Zeitraums einsehen zu können!
          </span>
        </div>
        <div :class="($q.platform.is.mobile) ? 'col-12' : 'col-3'" class="column">
          <q-select label="Ausbilder" v-model="ausbilder"
                    :options="allAusbilder"
                    :option-value="ab => ab.id"
                    :option-label="ab => ab.fullName" />
          <q-input label="Ausbildungsjahr" v-model="ausbildungsJahr" />
          <q-input label="Abteilung" v-model="abteilung" />
          <q-btn class="q-ma-md bg-secondary" label="Nachweis erstellen" @click="createNachweis" />
        </div>
      </form>
    </q-card-section>
  </q-card>
</template>

<script setup>
import {useActivityStore} from "stores/activityStore";
import {useNachweisStore} from "stores/nachweisStore";
import {onMounted, ref} from "vue";
import {date, useQuasar} from "quasar";
import {api} from "boot/axios";
import {useApiUser} from "stores/apiUserStore";

const $q = useQuasar()
const activityStore = useActivityStore()
const apiUserStore = useApiUser()
const nachweisStore = useNachweisStore()

const dateRange = ref(null)
const selectedActivities = ref([])
/**
 * Variable (re-)assigned during v-for in Template
 */
const currDate = ref(null)

function loadActivitiesInDateRange() {
  if (dateRange.value === null) {
    selectedActivities.value = []
    return;
  }

  let rangeDates = []
  let startDate = makeJsDate(dateRange.value.from)
  let endDate = makeJsDate(dateRange.value.to)
  for (let d = startDate;
       date.getDateDiff(endDate, d, 'days') >= 0;
       d = date.addToDate(date.clone(d), {days: 1})) {
    rangeDates.push(date.formatDate(d, "DD.MM.YYYY"))
  }
  selectedActivities.value = activityStore.myActivities.filter(it => rangeDates.includes(it.date))
}

function makeJsDate(dateString) {
  let parts = dateString.split(".")
  return new Date(parts[2], parts[1]-1, parts[0])
}

const allAusbilder = ref(null)
const ausbilder = ref(null)
const ausbildungsJahr = ref(null)
const abteilung = ref(null)

onMounted(loadAusbilderUsers)

function loadAusbilderUsers() {
  api.get("/rest/user/ausbilder/list")
    .then((response) => {
      if (response.data !== undefined) {
        allAusbilder.value = response.data
      }
    })
  .catch((err) => {
    $q.notify("Es ist ein Fehler aufgetreten:" + err)
    console.log(err)
  })
}

function createNachweis() {
  if (selectedActivities.value.length < 1) {
    $q.notify("Es müssen Aktivitäten im Nachweis enthalten sein!")
    return
  }
  if (ausbilder.value === undefined) {
    $q.notify("Es muss ein Ausbilder angegeben werden!")
    return
  }
  if (dateRange === undefined) {
    $q.notify("Es muss ein Datenbereich ausgewählt sein!")
    return
  }
  if (date.getDateDiff(dateRange.value.from, dateRange.value.to) > 7) {
    $q.notify("Ein Ausbildungsnachweis darf sich nur auf maximal 7 Tage beziehen!")
    return
  }

  let nwObj = {}
  nwObj.ausbildungsjahr = ausbildungsJahr.value
  nwObj.abteilung = abteilung.value
  nwObj.startDate = dateRange.value.from
  nwObj.endDate = dateRange.value.to
  nwObj.azubiId = apiUserStore.apiUser.id
  nwObj.ausbilderId = ausbilder.value.id
  nwObj.activities = selectedActivities.value

  nachweisStore.createNachweis(nwObj)
}
</script>

<style>
.subheadline {
  font-size: 125%;
}
</style>
