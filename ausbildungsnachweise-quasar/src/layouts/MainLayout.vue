<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Ausbildungsnachweise
        </q-toolbar-title>

        <div v-if="apiUserStore.apiUser.isAdmin || userCount === 0" class="q-mr-md">
          <router-link to="/new-user">
            Neuen Benutzer erstellen
          </router-link>
        </div>
        <div v-if="apiUserStore.isLoggedIn()" class="q-mr-md">
          <q-btn icon="fas fa-key" @click="pwChangeDialogOpen = true" />
        </div>
        <div v-if="apiUserStore.apiUser.email.length > 0">
          Logged in as {{ apiUserStore.apiUser.email }}. <a class="link" @click="logout">Logout?</a>
        </div>
        <div v-else>
          Du bist nicht eingeloggt. <a class="link" @click="showLoginDialog = !showLoginDialog">Einloggen?</a>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container style="background-color: #222222">
      <router-view />
    </q-page-container>

    <q-footer class="row q-pt-sm q-pl-md q-pr-md q-pb-sm">
      <div class="col text-left">
        <a class="text-grey-4" href="https://github.com/Velyn-N/ausbildungsnachweise" target="_blank">
          Ausbildungsnachweise
        </a>
        by
        <a class="text-grey-4" href="https://github.com/Velyn-N" target="_blank">
          Velyn-N/NMA
        </a>
      </div>
      <div class="col text-right">
        Build with
        <a class="text-grey-4" href="https://quasar.dev/" target="_blank">
          Quasar
        </a>
        and
        <a class="text-grey-4" href="https://quarkus.io/" target="_blank">
          Quarkus
        </a>
      </div>
    </q-footer>
  </q-layout>

  <q-dialog v-model="showLoginDialog">
    <q-card>
      <form onsubmit="return false">
        <q-card-section>
          <div class="row">
            <div class="q-mr-md">
              <q-input input-class="text-center" v-model="loginData.email" label="E-Mail" autofocus name="email"/>
            </div>
            <div class="q-ml-md">
              <q-input v-model="loginData.password" :type="isPwdInput ? 'password' : 'text'" label="Passwort" name="password">
                <template v-slot:append>
                  <q-icon :name="isPwdInput ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwdInput = !isPwdInput"/>
                </template>
              </q-input>
            </div>
          </div>
        </q-card-section>
        <q-card-section class="text-center">
          <q-btn type="submit" @click="login" v-close-popup label="Login" />
        </q-card-section>
      </form>
    </q-card>
  </q-dialog>

  <q-dialog v-model="pwChangeDialogOpen">
    <q-card style="min-width: 30vw;">
      <form onsubmit="return false">
        <q-card-section class="text-center">
          <h4>Passwort ändern</h4>
        </q-card-section>
        <q-card-section class="text-center">
          <div class="column">
            <q-input v-model="oldPw"
                     :type="showOldPw ? 'text' : 'password'"
                     label="Altes Passwort"
                     name="old-password">
              <template v-slot:append>
                <q-icon :name="showOldPw ? 'visibility' : 'visibility_off'"
                        class="cursor-pointer"
                        @click="showOldPw = !showOldPw"/>
              </template>
            </q-input>
            <q-input v-model="newPw"
                     :type="showNewPw ? 'text' : 'password'"
                     label="Neues Passwort"
                     name="new-password">
              <template v-slot:append>
                <q-icon :name="showNewPw ? 'visibility' : 'visibility_off'"
                        class="cursor-pointer"
                        @click="showNewPw = !showNewPw"/>
              </template>
            </q-input>
            <q-input v-model="newPwRepeat"
                     :type="showNewPwRepeat ? 'text' : 'password'"
                     label="Neues Passwort (Wiederholen)"
                     name="repeat-new-password">
              <template v-slot:append>
                <q-icon :name="showNewPwRepeat ? 'visibility' : 'visibility_off'"
                        class="cursor-pointer"
                        @click="showNewPwRepeat = !showNewPwRepeat"/>
              </template>
            </q-input>
          </div>
        </q-card-section>
        <q-card-section class="text-center">
          <q-btn type="submit" @click="updatePassword" label="Passwort ändern" />
        </q-card-section>
      </form>
    </q-card>
  </q-dialog>
</template>

<script setup>
import {ref, reactive} from "vue";
import {api} from "boot/axios";
import {useQuasar} from "quasar";
import {useApiUser} from "stores/apiUserStore";

const $q = useQuasar()
const apiUserStore = useApiUser()

let showLoginDialog = ref(false)
const loginData = reactive({email: "", password: ""})
let isPwdInput = ref(true)

function login() {
  api.post("/rest/user/login", loginData).then((resp) => {
    if (resp.data !== undefined && resp.data.length > 0) {
      apiUserStore.reloadApiUser(resp.data)
      $q.notify("Du wurdest eingeloggt.")
      setTimeout(() => location.reload(), 500)
    }
  }).catch((err) => {
    $q.notify("Es ist ein Fehler beim einloggen aufgetreten: " + err)
  })
  loginData.password = ""
  isPwdInput.value = true
}

function logout() {
  apiUserStore.logout()
}



const userCount = ref(-1)

api.get("/rest/user/count").then(resp => {
  userCount.value = resp.data
})



const pwChangeDialogOpen = ref(false)
const oldPw = ref()
const showOldPw = ref(false)
const newPw = ref()
const showNewPw = ref(false)
const newPwRepeat = ref()
const showNewPwRepeat = ref(false)

function updatePassword() {
  if (newPw.value !== newPwRepeat.value) {
    $q.notify("Die Passwörter müssen übereinstimmen.")
    return
  }
  api.post("rest/user/change-password", {oldPassword: oldPw, newPassword: newPw}).then(resp => {
    if (resp.data) {
      $q.notify("Dein Passwort wurde erfolgreich geändert.")
      pwChangeDialogOpen.value = false
    } else {
      $q.notify("Das Passwort konnte nicht geändert werden.")
    }
  }).catch(err => {
    $q.notify("Das Passwort konnte nicht geändert werden: " + err)
  })
}
</script>

<style>
.link {
  cursor: pointer;
}
</style>
