<template>
  <q-card>
    <q-card-section class="text-center">
      <h5>Ausbildungsnachweise - Detailansicht</h5>
    </q-card-section>
    <q-card-section>
      <div class="fit justify-center text-center full-width">
        <div v-if="nachweis !== null">
          <hr>
          <div class="row q-mt-lg">
            <div class="col">
              <b>Azubi:</b><br>
              {{ nachweis.azubiId }}
            </div>
            <div class="col">
              <b>Ausbilder:</b><br>
              {{ nachweis.ausbilderId }}
            </div>
          </div>
          <div class="row q-mt-lg">
            <div class="col">
              <b>Ausbildungsjahr:</b><br>
              {{ nachweis.ausbildungsjahr }}
            </div>
            <div class="col">
              <b>Abteilung:</b><br>
              {{ nachweis.abteilung }}
            </div>
          </div>
          <div class="row q-mt-md q-mb-lg">
            <div class="col">
              <span>
                <b>Letzte Änderung am Zeichnungsstatus (Azubi):</b><br>
                {{ (nachweis.azubiSignDate) ? nachweis.azubiSignDate : "-" }}
              </span>
            </div>
            <div class="col">
              <span>
                <b>Letzte Änderung am Zeichnungsstatus (Ausbilder):</b><br>
                {{ (nachweis.ausbilderSignDate) ? nachweis.ausbilderSignDate : "-" }}
              </span>
            </div>
          </div>
          <hr>
          <div class="row">
            <table class="full-width text-center q-mt-lg" v-if="nachweis.activities.length > 0">
              <thead class="bi-border-bottom" style="text-decoration: underline">
                <tr>
                  <td><b>Tag</b></td>
                  <td><b>Aktivität</b></td>
                  <td><b>Dauer</b></td>
                </tr>
              </thead>
              <tbody v-for="(activity, index) in nachweis.activities">
                <tr v-if="currDate !== activity.date">
                  <td colspan="3">
                    <span class="q-pb-xl invisible">{{ currDate = activity.date }}</span>
                  </td>
                </tr>
                <tr>
                  <td>{{ activity.date }}</td>
                  <td>{{ activity.activity }}</td>
                  <td>
                    <MinuteDisplay :minutes="activity.durationHours" />
                  </td>
                </tr>
                <tr v-if="index === nachweis.activities.length">
                  <td colspan="4">
                    <hr>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-if="nachweis === null">
          Es gibt keinen Nachweis mit dieser ID.
        </div>
      </div>
    </q-card-section>
  </q-card>
</template>

<script setup>
import MinuteDisplay from "components/time/MinuteDisplay.vue";
import {useNachweisStore} from "stores/nachweisStore";
import {computed} from "vue";

const props = defineProps(['nachweisid'])
const nachweisStore = useNachweisStore()

const nachweis = computed(() => {
  let results = nachweisStore.myNachweise.filter(it => it.id === props.nachweisid)
  return (results.length === 1) ? results[0] : null
})
</script>
