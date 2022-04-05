<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          Ausbildungsnachweise
        </q-toolbar-title>

        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>

    <q-page-container class="bg-dark">
      <router-view />
    </q-page-container>
    <q-footer class="row q-pt-sm q-pl-md q-pr-md q-pb-sm">
      <div class="col text-left">
        <a class="text-grey-4" href="https://github.com/Velyn-N/ausbildungsnachweise">Ausbildungsnachweise</a>
        by
        <a class="text-grey-4" href="https://github.com/Velyn-N">Velyn-N</a>
      </div>
      <div class="col text-right">
        Right
      </div>
    </q-footer>
  </q-layout>
</template>

<script setup>
</script>
