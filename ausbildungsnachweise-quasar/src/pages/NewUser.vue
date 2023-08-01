<template>
  <q-card class="q-ma-md text-center">
    <q-card-section>
      <router-link to="/">
        Go Home
      </router-link>
    </q-card-section>
    <div v-if="!finished">
      <q-card-section>
        <h6>Anlegen eines Admin-Benutzers</h6>
      </q-card-section>
      <q-card-section class="text-center">
        <q-input v-model="initialAdminUser.fullName" label="Voller Name" />
        <q-input v-model="initialAdminUser.email" label="E-Mail" />
        <q-input v-model="initialAdminUser.password" label="Password" type="password" />
      </q-card-section>
      <q-card-section v-if="userCount > 0">
        <q-checkbox v-model="initialAdminUser.isAzubi" label="Azubi" @update:model-value="handleCheckboxChange('azubi')" />
        <q-checkbox v-model="initialAdminUser.isAusbilder" label="Ausbilder" @update:model-value="handleCheckboxChange('ausbilder')" />
        <q-checkbox v-model="initialAdminUser.isAdmin" label="Admin" />
      </q-card-section>
      <q-card-section>
        <q-btn @click="createInitialAdminUser"
               label="Admin-Benutzer erstellen" />
      </q-card-section>
    </div>
  </q-card>
</template>

<script setup>
import {reactive, ref} from "vue";
import {ApiUser} from "components/ApiUser";
import {api} from "boot/axios";
import {useQuasar} from "quasar";

const $q = useQuasar()

const finished = ref(false)
const userCount = ref(-1)

api.get("/rest/user/count").then(resp => {
  userCount.value = resp.data
})

function handleCheckboxChange(source) {
  if (initialAdminUser.isAzubi && initialAdminUser.isAusbilder) {
    if (source === "ausbilder") {
      initialAdminUser.isAzubi = false
    } else  {
      initialAdminUser.isAusbilder = false
    }
  }
}


const initialAdminUser = reactive(new ApiUser())
function createInitialAdminUser() {
  api.post("/rest/user/new", initialAdminUser).then(resp => {
    if (resp.data) {
      $q.notify("Der neue Nutzer wurde angelegt.")
      finished.value = true
    } else {
      $q.notify("Das hat nicht geklappt. Guck in den Applikationslog für Fehlernachrichten.")
    }
  })
}
</script>
