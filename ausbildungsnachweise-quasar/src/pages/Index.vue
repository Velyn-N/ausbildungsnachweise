<template>
  <q-page class="flex flex-center q-pa-sm">
    <div class="fit row wrap justify-center items-center content-center text-center">

      <ActivityCards />

      <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-5'" class="q-ma-sm">
        <q-card-section>
          <h6>Ausbildungsnachweise</h6>
        </q-card-section>
        <q-card-section>
          Content
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script setup>
import ActivityCards from "components/index/ActivityCards.vue";
</script>
