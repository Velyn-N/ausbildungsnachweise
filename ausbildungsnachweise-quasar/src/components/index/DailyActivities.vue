<template>
  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-5'" class="q-ma-sm" v-if="apiUserStore.apiUser.isAzubi">
    <q-card-section>
      <h6>Tägliche Aktivitäten</h6>
    </q-card-section>
    <q-card-section>
      Content
    </q-card-section>
  </q-card>
</template>

<script setup>
import {useApiUser} from "stores/apiUserStore";

const apiUserStore = useApiUser()
</script>
