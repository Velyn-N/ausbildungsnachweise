<template>
  <q-card class="q-ma-md text-center">
    <q-card-section>
      <router-link to="/">
        Go Home
      </router-link>
    </q-card-section>
    <div v-if="!finished && (apiUser.isLoggedIn() || userCount === 0)">
      <q-card-section>
        <h6>Anlegen eines neuen Benutzers</h6>
      </q-card-section>
      <q-card-section class="text-center">
        <q-input v-model="newUser.fullName" label="Voller Name" />
        <q-input v-model="newUser.email" label="E-Mail" />
        <q-input v-model="newUser.password" label="Password" type="password" />
      </q-card-section>
      <q-card-section v-if="userCount > 0">
        <q-checkbox v-model="newUser.isAzubi" label="Azubi" @update:model-value="handleCheckboxChange('azubi')" />
        <q-checkbox v-model="newUser.isAusbilder" label="Ausbilder" @update:model-value="handleCheckboxChange('ausbilder')" />
        <q-checkbox v-model="newUser.isAdmin" label="Admin" />
      </q-card-section>
      <q-card-section>
        <q-btn @click="createInitialAdminUser"
               label="Benutzer erstellen" />
      </q-card-section>
    </div>
  </q-card>
</template>

<script setup>
import {reactive, ref} from "vue";
import {ApiUser} from "components/ApiUser";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import {useApiUser} from "stores/apiUserStore";

const $q = useQuasar()
const apiUser = useApiUser()

const finished = ref(false)
const userCount = ref(-1)

api.get("/rest/user/count").then(resp => {
  userCount.value = resp.data
})

function handleCheckboxChange(source) {
  if (newUser.isAzubi && newUser.isAusbilder) {
    if (source === "ausbilder") {
      newUser.isAzubi = false
    } else  {
      newUser.isAusbilder = false
    }
  }
}


const newUser = reactive(new ApiUser())
function createInitialAdminUser() {
  console.log(newUser)
  api.post("/rest/user/new", newUser).then(resp => {
    if (resp.data) {
      $q.notify("Der neue Nutzer wurde angelegt.")
      finished.value = true
    } else {
      $q.notify("Das hat nicht geklappt. Guck in den Applikationslog für Fehlernachrichten.")
    }
  })
}
</script>
