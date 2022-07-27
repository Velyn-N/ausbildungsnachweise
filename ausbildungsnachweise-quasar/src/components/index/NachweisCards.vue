<template>
  <q-card :class="($q.platform.is.mobile) ? 'col-12' : 'col-4'" class="q-ma-sm" v-if="apiUserStore.isLoggedIn()">
    <q-card-section>
      <h6>Meine Ausbildungsnachweise
      <span class="absolute-right q-pt-lg" v-if="apiUserStore.apiUser.isAzubi">
        <q-btn @click="createModalOpen = true">
          <q-icon name="fas fa-plus" />
        </q-btn>
      </span>
      </h6>

    </q-card-section>
    <q-card-section>
      <q-list v-for="nachweis in nachweisStore.myNachweise">
        <q-expansion-item group="myNachweisList">
          <template v-slot:header>
            <span class="full-width row"
                  :class="(nachweis.signedByAzubi && nachweis.signedByAusbilder) ? 'text-green' :
                    (apiUserStore.apiUser.isAzubi && nachweis.signedByAzubi) ? 'text-blue' :
                    (apiUserStore.apiUser.isAusbilder && nachweis.signedByAzubi && !nachweis.signedByAusbilder) ? 'text-yellow' : ''">
              {{ nachweis.startDate }} - {{ nachweis.endDate }} ({{ (apiUserStore.apiUser.isAzubi) ? nachweis.ausbilderId : nachweis.azubiId }})
            </span>
          </template>
          <table class="full-width">
            <tr>
              <td colspan="2" class="full-width text-right q-pr-lg">
                <q-icon name="fas fa-search" class="clickable"
                        title="Details anzeigen"
                        @click="detailNachweisOpen = !detailNachweisOpen; detailNachweis = nachweis.id" />
              </td>
            </tr>
            <tr>
              <td>
                <q-checkbox v-model="nachweis.signedByAzubi" label="Von AzuBi gezeichnet"
                            @click="nachweisStore.saveNachweis(nachweis)"
                            :disable="!apiUserStore.apiUser.isAzubi || nachweis.signedByAusbilder"
                            :title="(nachweis.azubiSignDate) ? 'Letzte Änderung: ' + nachweis.azubiSignDate : ''"
                />
              </td>
              <td>
                <q-checkbox v-model="nachweis.signedByAusbilder" label="Von Ausbilder gezeichnet"
                            @click="nachweisStore.saveNachweis(nachweis)"
                            :disable="!apiUserStore.apiUser.isAusbilder || !nachweis.signedByAzubi"
                            :title="(nachweis.ausbilderSignDate) ? 'Letzte Änderung: ' + nachweis.ausbilderSignDate : ''"
                />
              </td>
            </tr>
          </table>
        </q-expansion-item>
      </q-list>
    </q-card-section>
  </q-card>

  <q-dialog full-width full-height v-model="createModalOpen">
    <CreateNachweisDialog />
  </q-dialog>

  <q-dialog full-width full-height v-model="detailNachweisOpen">
    <NachweisDetailDialog :nachweisid="detailNachweis" />
  </q-dialog>
</template>

<script setup>
import CreateNachweisDialog from "components/index/CreateNachweisDialog.vue";
import NachweisDetailDialog from "components/index/NachweisDetailDialog.vue";
import {useApiUser} from "stores/apiUserStore";
import {useNachweisStore} from "stores/nachweisStore";
import {ref} from "vue";

const apiUserStore = useApiUser()
const nachweisStore = useNachweisStore()

const createModalOpen = ref(false)
const detailNachweisOpen = ref(false)
const detailNachweis = ref(null)

nachweisStore.loadMyNachweise()
</script>
