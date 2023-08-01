<template>
  <q-page class="flex flex-center q-pa-sm">
    <div class="fit row wrap justify-center items-center content-center text-center">

      <NachweisCards />

      <div v-if="!apiUserStore.isLoggedIn()">
        <h4 class="q-mb-md">Du bist nicht eingeloggt!</h4>
        Nutze den Link oben rechts um dich einzuloggen.
      </div>

    </div>
  </q-page>
</template>

<script setup>
import {useApiUser} from "stores/apiUserStore";
import NachweisCards from "components/index/NachweisCards.vue";

const apiUserStore = useApiUser()
</script>
