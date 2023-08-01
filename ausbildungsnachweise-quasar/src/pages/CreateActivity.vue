<template>
  <q-card v-if="apiUserStore.apiUser.isAzubi">
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
            <MinuteInput class="text-center" label="Dauer" v-model="newActivity.durationHours" />
          </td>
        </tr>
        <tr>
          <td class="text-left">
            <q-btn @click="activityStore.addActivity(newActivity)">
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
import MinuteInput from "components/time/MinuteInput.vue";
import {useApiUser} from "stores/apiUserStore";
import {useActivityStore} from "stores/activityStore";
import {ref} from "vue";

const apiUserStore = useApiUser()
const activityStore = useActivityStore()

const newActivity = ref(getPlaceholderActivity())

function getPlaceholderActivity() {
  return {date: null, activity: null, durationHours: null}
}
</script>
